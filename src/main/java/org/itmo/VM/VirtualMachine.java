package org.itmo.VM;

import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.instructions.InstructionType;
import org.itmo.VM.memory.Manager;
import org.itmo.VM.memory.object.MemoryObject;
import org.itmo.VM.memory.object.ObjectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class VirtualMachine {
    private final Manager manager = new Manager();

    public final Stack<Long> stack = new Stack<>();

    private final Map<String, List<Instruction>> instructionCache = new HashMap<>();

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
                ArrayLoadOp(instruction.getName(), instruction.getIndex());
                break;

            case ARRAY_STORE:
                ArrayStoreOp(instruction.getName(), instruction.getIndex());
                break;

            case FOR:
                ForLoop(instruction.getBody());
                break;

            case WHILE:
                WhileLoop(instruction.getBody());
                break;

            case FUNCTION_DEF:
                DefineFunction(instruction.getName(), instruction.getBody());
                break;

            case FUNCTION_CALL:
                CallFunction(instruction.getName());
                break;

            case RETURN:
                ReturnOp();
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
    private void ArrayStoreOp(String arrayName, int index) {
        Long value = stack.pop();
        manager.writeToArray(arrayName, index, value);
    }

    // чтение из массива
    private void ArrayLoadOp(String arrayName, int index) {
        stack.push(manager.readFromArray(arrayName, index));
    }

    // цикл for
    private void ForLoop(List<Instruction> body) {
        Long limit = stack.pop();
        Long start = stack.pop();

        String cacheKey = "FOR:" + body.hashCode();
        List<Instruction> cachedBody = instructionCache.computeIfAbsent(cacheKey, k -> body);

        for (long i = start; i < limit; i++) {
            manager.createScope();
            stack.push(i);

            for (Instruction instr : cachedBody) {
                execute(instr);
            }

            manager.deleteScope();
        }
    }

    // цикл while
    private void WhileLoop(List<Instruction> body) {
        String cacheKey = "WHILE:" + body.hashCode();
        List<Instruction> cachedBody = instructionCache.computeIfAbsent(cacheKey, k -> body);

        while (stack.pop() != 0) {
            manager.createScope();

            for (Instruction instr : cachedBody) {
                execute(instr);
            }

            manager.deleteScope();
        }
    }

    // определение функции
    private void DefineFunction(String functionName, List<Instruction> body) {
        instructionCache.put("FUNC:" + functionName, body);
    }

    // вызов функции
    private void CallFunction(String functionName) {
        List<Instruction> body = instructionCache.get("FUNC:" + functionName);
        if (body == null) {
            throw new IllegalArgumentException("function not defined: " + functionName);
        }

        manager.createScope();
        for (Instruction instr : body) {
            if (instr.getType() == InstructionType.RETURN) {
                execute(instr);
                break;
            } else {
                execute(instr);
            }
        }
    }

    // возврат из функции
    private void ReturnOp() {
        Long returnValue = stack.pop();
        manager.deleteScope();
        stack.push(returnValue);
    }

}
