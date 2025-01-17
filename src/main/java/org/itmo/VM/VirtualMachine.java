package org.itmo.VM;

import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.memory.Manager;
import org.itmo.VM.memory.object.MemoryObject;
import org.itmo.VM.memory.object.ObjectType;

import java.util.List;
import java.util.Stack;


public class VirtualMachine {
    private final Manager manager = new Manager();

    public final Stack<Long> stack = new Stack<>();

    private Integer pc = 0;

    public void interpret(List<Instruction> instructions) {
        while (pc < instructions.size()) {
            Instruction instruction = instructions.get(pc);
            pc++;
        }
    }

    public void execute(Instruction instruction) {
        switch (instruction.getType()) {
            case STORE:
                StoreOp(instruction.getName());
                break;

            case LOAD_VAR:
                LoadVariable(instruction.getName());
                break;

            case PUSH:
                Push(instruction.getValue());
                break;
            case ADD:
                AddOp();
                break;

            case SUB:
                SubOp();
                break;

            case MUL:
                MulOp();
                break;

            case DIV:
                DivOp();
                break;

            case MOD:
                ModOp();
                break;

            case AND:
                AndOp();
                break;

            case OR:
                OrOp();
                break;

            case NOT:
                NotOp();
                break;

            case ARRAY_CREATE:
                ArrayCreate(instruction.getName());
                break;

            case ARRAY_LOAD:
                ArrayLoadOp(instruction.getName(), instruction.getValue());
                break;

            case ARRAY_STORE:
                ArrayStoreOp(instruction.getName(), instruction.getValue());
                break;

            default:
                throw new IllegalArgumentException("Unknown instruction type: " + instruction.getType());
        }
    }

    private void Push(Long value) {
        stack.push(value);
    }

    // сохранить переменную в стеке
    private void StoreOp(String name) {
        var value = new MemoryObject(ObjectType.LONG, stack.pop());
        manager.allocate(name, value);
    }

    // получить значение переменной по имени
    private void LoadVariable(String name) {
        Long value = manager.getLong(name);
        stack.push(value);
    }

    // сложение
    private void AddOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push(left + right);
    }

    // вычитание
    private void SubOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push(left - right);
    }

    // умножение
    private void MulOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push(left * right);
    }

    // деление
    private void DivOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        if (right == 0) {
            throw new ArithmeticException("division by zero");
        }
        stack.push(left / right);
    }

    // нахождение остатка
    private void ModOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push(left % right);
    }

    // логическая операция and
    private void AndOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push((left != 0 && right != 0) ? 1L : 0L);
    }

    // логическая операция or
    private void OrOp() {
        Long right = stack.pop();
        Long left = stack.pop();
        stack.push((left != 0 || right != 0) ? 1L : 0L);
    }

    // логическая операция not
    private void NotOp() {
        Long value = stack.pop();
        stack.push((value == 0) ? 1L : 0L);
    }

    //создание массива
    private void ArrayCreate(String name) {
        Long size = stack.pop();
        manager.allocate(name, new MemoryObject(ObjectType.ARRAY, new Long[size.intValue()]));
    }

    // запись в массив
    private void ArrayStoreOp(String arrayName, long index) {
        Long value = stack.pop();
        manager.writeToArray(arrayName, (int)index, value);
    }

    // чтение из массива
    private void ArrayLoadOp(String arrayName, long index) {
        stack.push(manager.readFromArray(arrayName, (int)index));
    }
}
