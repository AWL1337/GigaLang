package org.itmo.VM;

import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.instructions.InstructionType;
import org.itmo.VM.memory.Manager;
import org.itmo.VM.memory.object.MemoryObject;
import org.itmo.VM.memory.object.ObjectType;

import java.util.List;
import java.util.Stack;


public class VirtualMachine {
    private final Manager manager = new Manager();

    private final Stack<Long> stack;

    private Integer pc = 0;

    public VirtualMachine(Stack<Long> stack) {
        this.stack = stack;
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
            execute(instruction);
        }
    }

    private void searchLabels(List<Instruction> instructions) {
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            if (instruction.getType().equals(InstructionType.LABEL)) {
                manager.makeLabel(instruction.getName(), i);
            }
        }
    }

    public void execute(Instruction instruction) {
        switch (instruction.getType()) {
            case STORE:
                storeOp(instruction.getName());
                break;

            case LOAD_VAR:
                loadVariable(instruction.getName());
                break;

            case PUSH:
                push(instruction.getValue());
                break;

            case ADD:
                addOp();
                break;

            case SUB:
                subOp();
                break;

            case MUL:
                mulOp();
                break;

            case DIV:
                divOp();
                break;

            case MOD:
                modOp();
                break;

            case AND:
                andOp();
                break;

            case OR:
                orOp();
                break;

            case NOT:
                notOp();
                break;

            case EQ:
                eqOp();
                break;

            case NE:
                neOp();
                break;

            case LT:
                ltOp();
                break;

            case GT:
                gtOp();
                break;

            case LE:
                leOp();
                break;

            case GE:
                geOp();
                break;

            case ARRAY_CREATE:
                arrayCreate(instruction.getName());
                break;

            case ARRAY_LOAD:
                arrayLoadOp(instruction.getName());
                break;

            case ARRAY_STORE:
                arrayStoreOp(instruction.getName());
                break;

            case LABEL:
                break;

            case JUMP:
                jump(instruction.getName());
                break;

            case JUMP_IF_FALSE:
                jumpIfFalse(instruction.getName());
                break;

            default:
                throw new IllegalArgumentException("Unknown instruction type: " + instruction.getType());
        }
    }

    private void push(Long value) {
        stack.push(value);
    }

    // сохранить переменную в стеке
    private void storeOp(String name) {
        var value = new MemoryObject(ObjectType.LONG, stack.pop());
        manager.allocate(name, value);
    }

    // получить значение переменной по имени
    private void loadVariable(String name) {
        Long value = manager.getLong(name);
        stack.push(value);
    }

    // сложение
    private void addOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push(left + right);
    }

    // вычитание
    private void subOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push(left - right);
    }

    // умножение
    private void mulOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push(left * right);
    }

    // деление
    private void divOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        if (right == 0) {
            throw new ArithmeticException("division by zero");
        }
        stack.push(left / right);
    }

    // нахождение остатка
    private void modOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push(left % right);
    }

    // логическая операция and
    private void andOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push((left != 0 && right != 0) ? 1L : 0L);
    }

    // логическая операция or
    private void orOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push((left != 0 || right != 0) ? 1L : 0L);
    }

    // логическая операция not
    private void notOp() {
        Long value = stack.pop();
        stack.push((value == 0) ? 1L : 0L);
    }

    private void eqOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push(left.equals(right) ? 1L : 0L);
    }

    private void neOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push(left.equals(right) ? 0L : 1L);
    }


    private void ltOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push((left < right) ? 1L : 0L);
    }

    private void gtOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push((left > right) ? 1L : 0L);
    }

    private void leOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push((left <= right) ? 1L : 0L);
    }

    private void geOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push((left >= right) ? 1L : 0L);
    }


    //создание массива
    private void arrayCreate(String name) {
        Long size = stack.pop();
        manager.allocate(name, new MemoryObject(ObjectType.ARRAY, new Long[size.intValue()]));
    }

    // запись в массив
    private void arrayStoreOp(String arrayName) {
        Long value = stack.pop();
        long index = stack.pop();
        manager.writeToArray(arrayName, (int)index, value);
    }

    // чтение из массива
    private void arrayLoadOp(String arrayName) {
        long index = stack.pop();
        stack.push(manager.readFromArray(arrayName, (int)index));
    }

    private void jump(String label) {
        pc = manager.resolveLabel(label);
    }

    private void jumpIfFalse(String label) {
        Long value = stack.pop();
        if (value == 0L) {
            jump(label);
        }
    }
}
