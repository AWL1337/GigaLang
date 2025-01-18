package org.itmo.VM.memory.GC;

import lombok.AllArgsConstructor;
import org.itmo.VM.memory.Manager;
import org.itmo.VM.memory.object.MemoryObject;

import java.util.List;

@AllArgsConstructor
public class GarbageCollector {
    private Manager manager;

    public void collect() {
        var garbage = mark();
        sweep(garbage);
    }

    private List<MemoryObject> mark() {
        manager.getGlobalMemory()
                .forEach((s, i) ->
                        i.getObject().setIsAlive(true));

        manager.getCurrentScope()
                .forEach((s, i) ->
                        i.getObject().setIsAlive(true));

        return manager.getHeap().stream().filter(o -> !o.getIsAlive()).toList();
    }

    private void sweep(List<MemoryObject> garbage) {
        manager.getHeap().removeAll(garbage);
    }
}
