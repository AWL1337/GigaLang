package org.itmo.VM;

import lombok.Data;
import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.instructions.InstructionType;
import org.itmo.VM.memory.FunctionInfo;
import org.itmo.VM.memory.Manager;
import org.itmo.VM.memory.object.MemoryObject;
import org.itmo.VM.memory.object.ObjectType;

import java.util.*;

@Data
public class VirtualMachine {
    private final Manager manager = new Manager();

    private final Map<String, FunctionInfo> functions = new HashMap<>();

    private final Stack<Integer> callStack = new Stack<>();
    private final Stack<Stack<Long>> stackStack = new Stack<>();

    private Integer pc = 0;

    public VirtualMachine(Stack<Long> stack) {
        stackStack.push(stack);
    }

    public VirtualMachine() {
        this(new Stack<>());
    }

    public void interpret(List<Instruction> instructions) {
        searchLabels(instructions);
        pc = 0;
        while (pc < instructions.size()) {
            Instruction instruction = instructions.get(pc);
            pc++;
            var handler = execute(instruction);
            handler.run();
        }
    }

    private Stack<Long> getCurrentStack() {
        return stackStack.peek();
    }

    private void searchLabels(List<Instruction> instructions) {
        boolean funFlag = false;
        FunctionInfo info = null;
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);

            if (instruction.getType().equals(InstructionType.LABEL)) {
                manager.makeLabel(instruction.getName(), i);
            }

            if (instruction.getType().equals(InstructionType.FUN)) {
                if (funFlag) {
                    throw new IllegalArgumentException("You cannot declare any function inside a function");
                }
                funFlag = true;
                info = new FunctionInfo(instruction.getName(), i + 1, instruction.getArguments());
                functions.put(instruction.getName(), info);
            }

            if (instruction.getType().equals(InstructionType.END_FUN)) {
                funFlag = false;
                Objects.requireNonNull(info).setEnd(i + 1);
            }
        }
    }

    private Runnable execute(Instruction instruction) {
        return switch (instruction.getType()) {
            case STORE -> () -> storeOp(instruction.getName());
            case LOAD_VAR -> () -> loadVariable(instruction.getName());
            case PUSH -> () -> push(instruction.getValue());
            case ADD -> this::addOp;
            case SUB -> this::subOp;
            case MUL -> this::mulOp;
            case DIV -> this::divOp;
            case MOD -> this::modOp;
            case AND -> this::andOp;
            case OR -> this::orOp;
            case NOT -> this::notOp;
            case EQ -> this::eqOp;
            case NE -> this::neOp;
            case LT -> this::ltOp;
            case GT -> this::gtOp;
            case LE -> this::leOp;
            case GE -> this::geOp;
            case ARRAY_CREATE -> () -> arrayCreate(instruction.getName());
            case ARRAY_LOAD -> () -> arrayLoadOp(instruction.getName());
            case ARRAY_STORE -> () -> arrayStoreOp(instruction.getName());
            case LABEL -> () -> {};
            case JUMP -> () -> jumpOp(instruction.getName());
            case JUMP_IF_FALSE -> () -> jumpIfFalseOp(instruction.getName());
            case FUN -> () -> funOp(instruction.getName());
            case CALL -> () -> callOp(instruction.getName());
            case RETURN -> this::returnOp;
            case END_FUN -> this::funEndOp;
            default -> throw new IllegalArgumentException("Unknown instruction type: " + instruction.getType());
        };
    }

    private void push(Long value) {
        getCurrentStack().push(value);
    }

    // сохранить переменную в стеке
    private void storeOp(String name) {
        var value = new MemoryObject(ObjectType.LONG, getCurrentStack().pop());
        manager.allocate(name, value);
    }

    // получить значение переменной по имени
    private void loadVariable(String name) {
        Long value = manager.getLong(name);
        getCurrentStack().push(value);
    }

    // сложение
    private void addOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        getCurrentStack().push(left + right);
    }

    // вычитание
    private void subOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        getCurrentStack().push(left - right);
    }

    // умножение
    private void mulOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        getCurrentStack().push(left * right);
    }

    // деление
    private void divOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        if (right == 0) {
            throw new ArithmeticException("division by zero");
        }
        getCurrentStack().push(left / right);
    }

    // нахождение остатка
    private void modOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        getCurrentStack().push(left % right);
    }

    // логическая операция and
    private void andOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        getCurrentStack().push((left != 0 && right != 0) ? 1L : 0L);
    }

    // логическая операция or
    private void orOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        getCurrentStack().push((left != 0 || right != 0) ? 1L : 0L);
    }

    // логическая операция not
    private void notOp() {
        Long value = getCurrentStack().pop();
        getCurrentStack().push((value == 0) ? 1L : 0L);
    }

    private void eqOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        getCurrentStack().push(left.equals(right) ? 1L : 0L);
    }

    private void neOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        getCurrentStack().push(left.equals(right) ? 0L : 1L);
    }


    private void ltOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        getCurrentStack().push((left < right) ? 1L : 0L);
    }

    private void gtOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        getCurrentStack().push((left > right) ? 1L : 0L);
    }

    private void leOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        getCurrentStack().push((left <= right) ? 1L : 0L);
    }

    private void geOp() {
        Long right = getCurrentStack().pop();
        Long left = getCurrentStack().pop();
        getCurrentStack().push((left >= right) ? 1L : 0L);
    }


    //создание массива
    private void arrayCreate(String name) {
        Long size = getCurrentStack().pop();
        manager.allocate(name, new MemoryObject(ObjectType.ARRAY, new Long[size.intValue()]));
    }

    // запись в массив
    private void arrayStoreOp(String arrayName) {
        Long value = getCurrentStack().pop();
        long index = getCurrentStack().pop();
        manager.writeToArray(arrayName, (int)index, value);
    }

    // чтение из массива
    private void arrayLoadOp(String arrayName) {
        long index = getCurrentStack().pop();
        getCurrentStack().push(manager.readFromArray(arrayName, (int)index));
    }

    private void jumpOp(String label) {
        pc = manager.resolveLabel(label);
    }

    private void jumpIfFalseOp(String label) {
        Long value = getCurrentStack().pop();
        if (value == 0L) {
            jumpOp(label);
        }
    }

    private void funOp(String functionName) {
        pc = functions.get(functionName).getEnd();
    }

    private void callOp(String functionName) {
        manager.createScope();
        var stack = getCurrentStack();
        var info = functions.get(functionName);
        info.getParameters().reversed().forEach(arg ->
            manager.allocate(arg, new MemoryObject(ObjectType.LONG, stack.pop()))
        );
        stackStack.push(new Stack<>());
        callStack.push(pc + 1);
        pc = info.getStart();
    }

    private void returnOp() {
        manager.deleteScope();
        var stack = getCurrentStack();
        stackStack.pop();
        getCurrentStack().push(stack.pop());
        pc = callStack.pop();
    }

    private void funEndOp() {
        manager.deleteScope();
        stackStack.pop();
        pc = callStack.pop();
    }

}
