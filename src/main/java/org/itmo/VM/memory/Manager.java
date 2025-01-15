package org.itmo.VM.memory;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.itmo.VM.memory.GC.GarbageCollector;
import org.itmo.VM.memory.object.Identifier;
import org.itmo.VM.memory.object.MemoryObject;
import org.itmo.VM.memory.object.ObjectType;

import java.util.*;

@Data
@RequiredArgsConstructor
public class Manager {
    private final Map<String, Identifier> globalMemory = new HashMap<>();

    private final Stack<Map<String, Identifier>> stack = new Stack<>();

    private final Stack<Map<String, Integer>> labeslStack = new Stack<>();

    private final List<MemoryObject> heap = new LinkedList<>();

    private final GarbageCollector garbageCollector = new GarbageCollector(this);

    private final Long MAX_TRIGGER_OBJECTS = 1000L;

    public void init() {
        labeslStack.push(new HashMap<>());
    }

    public void makeLabel(String label, Integer index) {
        labeslStack.peek().put(label, index);
    }

    public Integer resolveLabel(String label) {
        if (!labeslStack.peek().containsKey(label)) {
            throw new IllegalArgumentException("Label " + label + " not found");
        }
        return labeslStack.peek().get(label);
    }

    // создать или изменить переменную по имени
    public void allocate(String name, MemoryObject object) {
        var scope = getCurrentScope();
        Identifier id;

        try {
            id = resolve(name);
        } catch (Exception e) {
            id = new Identifier(name);
        }

        id.setObject(object);
        heap.add(object);

        if (heap.size() > MAX_TRIGGER_OBJECTS) {
            garbageCollector.collect();
        }

        scope.put(name, id);
    }

    // получуить число по имени
    public Long getLong(String name) {
        var id = resolve(name);
        if (id.getObject().getType().equals(ObjectType.ARRAY)) {
            throw new IllegalArgumentException("Can't get long value, " +
                    "object with name: " + name + "is an array");
        }
        return (Long) id.getObject().getValue();
    }


    public void writeToArray(String name, Integer index, Long value) {
        var arr = getArray(name);
        arr[index] = value;
    }

    public Long readFromArray(String name, Integer index) {
        var arr = getArray(name);
        return arr[index];
    }

    public void createScope() {
        stack.push(new HashMap<>());
        labeslStack.push(new HashMap<>());
    }

    public void deleteScope() {
        stack.pop();
        labeslStack.pop();
    }

    private Identifier resolve(String name) {
        if (globalMemory.containsKey(name)) {
            return globalMemory.get(name);
        }

        if (stack.isEmpty()) {
            throw new IllegalArgumentException("Cannot resolve memory object with name: " + name);
        }

        var scope = getCurrentScope();

        if (scope.containsKey(name)) {
            return scope.get(name);
        }

        throw new IllegalArgumentException("Cannot resolve memory object with name: " + name);
    }

    public Map<String, Identifier> getCurrentScope() {
        if (stack.isEmpty()) {
            return globalMemory;
        }
        return stack.peek();
    }

    private Long[] getArray(String name) {
        var id = resolve(name);
        if (id.getObject().getType().equals(ObjectType.LONG)) {
            throw new IllegalArgumentException("Can't get an array, " +
                    "object with name: " + name + "is a long value");
        }
        return (Long[]) id.getObject().getValue();
    }
}
