package org.itmo;

import org.itmo.VM.VirtualMachine;
import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.instructions.InstructionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class VirtualMachineTest {
    private VirtualMachine virtualMachine;
    private Stack<Long> stack;

    @BeforeEach
    public void setUp() {
        stack = new Stack<>();

        virtualMachine = new VirtualMachine(stack);
    }

    @Test
    public void pushTest() {
        virtualMachine.execute(new Instruction(InstructionType.PUSH, null, 3L));

        assertEquals(1, stack.size());
        assertEquals(3L, stack.pop());
    }

    @Test
    public void addTest() {
        stack.push(2L);
        stack.push(1L);

        virtualMachine.execute(new Instruction(InstructionType.ADD, null, null));

        assertEquals(1, stack.size());
        assertEquals(3L, stack.pop());
    }

    @Test
    public void subTest() {
        stack.push(1L);
        stack.push(6L);

        virtualMachine.execute(new Instruction(InstructionType.SUB, null, null));

        assertEquals(1, stack.size());
        assertEquals(-5L, stack.pop());
    }

    @Test
    public void andTest() {
        stack.push(1L);
        stack.push(0L);

        virtualMachine.execute(new Instruction(InstructionType.AND, null, null));

        assertEquals(1, stack.size());
        assertEquals(0L, stack.pop());
    }

    @Test
    public void orTest() {
        stack.push(1L);
        stack.push(0L);

        virtualMachine.execute(new Instruction(InstructionType.OR, null, null));

        assertEquals(1, stack.size());
        assertEquals(1L, stack.pop());
    }

    @Test
    public void gtTest() {
        stack.push(56L);
        stack.push(0L);

        virtualMachine.execute(new Instruction(InstructionType.GT, null, null));

        assertEquals(1, stack.size());
        assertEquals(1L, stack.pop());
    }

    @Test
    public void ltTest() {
        stack.push(56L);
        stack.push(0L);

        virtualMachine.execute(new Instruction(InstructionType.LT, null, null));

        assertEquals(1, stack.size());
        assertEquals(0L, stack.pop());
    }

    @Test
    public void leTest() {
        stack.push(56L);
        stack.push(56L);

        virtualMachine.execute(new Instruction(InstructionType.LE, null, null));

        assertEquals(1, stack.size());
        assertEquals(1L, stack.pop());
    }

    @Test
    public void storeLoadVariableTest() {
        stack.push(6L);

        virtualMachine.execute(new Instruction(InstructionType.STORE, "aaa", null));

        assertEquals(0, stack.size());

        virtualMachine.execute(new Instruction(InstructionType.LOAD_VAR, "aaa", null));

        assertEquals(1, stack.size());
        assertEquals(6L, stack.pop());
    }

    @Test
    public void arrayCreateReadTest() {

        List<Instruction> createInstruction = List.of(
                new Instruction(InstructionType.PUSH, null, 3L),
                new Instruction(InstructionType.ARRAY_CREATE, "a", null),
                new Instruction(InstructionType.PUSH, null, 0L),
                new Instruction(InstructionType.PUSH, null, 1L),
                new Instruction(InstructionType.ARRAY_STORE, "a", null),
                new Instruction(InstructionType.PUSH, null, 1L),
                new Instruction(InstructionType.PUSH, null, 2L),
                new Instruction(InstructionType.PUSH, null, 2L),
                new Instruction(InstructionType.MUL, null, null),
                new Instruction(InstructionType.ARRAY_STORE, "a", null),
                new Instruction(InstructionType.PUSH, null, 2L),
                new Instruction(InstructionType.PUSH, null, 5L),
                new Instruction(InstructionType.ARRAY_STORE, "a", null)
        );

        List<Instruction> readInstruction = List.of(
                new Instruction(InstructionType.PUSH, null, 0L),
                new Instruction(InstructionType.ARRAY_LOAD, "a", null),
                new Instruction(InstructionType.PUSH, null, 1L),
                new Instruction(InstructionType.ARRAY_LOAD, "a", null),
                new Instruction(InstructionType.PUSH, null, 2L),
                new Instruction(InstructionType.ARRAY_LOAD, "a", null)
        );

        virtualMachine.interpret(createInstruction);

        assertEquals(0, stack.size());

        virtualMachine.interpret(readInstruction);

        assertEquals(3, stack.size());

        assertEquals(stack, List.of(1L, 4L, 5L));
    }


    @Test
    public void ifFalseTest() {

        List<Instruction> ifStatement = List.of(
                new Instruction(InstructionType.PUSH, null, 0L),
                new Instruction(InstructionType.PUSH, null, 0L),
                new Instruction(InstructionType.GT, null, null),
                new Instruction(InstructionType.JUMP_IF_FALSE, "0", null),
                new Instruction(InstructionType.PUSH, null, 5L),
                new Instruction(InstructionType.LABEL, "0", null)
        );

        virtualMachine.interpret(ifStatement);

        assertEquals(0, stack.size());
    }

    @Test
    public void ifTrueTest() {

        List<Instruction> ifStatement = List.of(
                new Instruction(InstructionType.PUSH, null, 1L),
                new Instruction(InstructionType.PUSH, null, 0L),
                new Instruction(InstructionType.GT, null, null),
                new Instruction(InstructionType.JUMP_IF_FALSE, "0", null),
                new Instruction(InstructionType.PUSH, null, 5L),
                new Instruction(InstructionType.LABEL, "0", null)
        );

        virtualMachine.interpret(ifStatement);

        assertEquals(1, stack.size());
        assertEquals(5L, stack.pop());
    }

    @Test
    public void ifElseTrueTest() {

        List<Instruction> ifStatement = List.of(
                new Instruction(InstructionType.PUSH, null, 1L),
                new Instruction(InstructionType.PUSH, null, 0L),
                new Instruction(InstructionType.GT, null, null),
                new Instruction(InstructionType.JUMP_IF_FALSE, "0", null),
                new Instruction(InstructionType.PUSH, null, 5L),
                new Instruction(InstructionType.JUMP, "1", null),
                new Instruction(InstructionType.LABEL, "0", null),
                new Instruction(InstructionType.PUSH, null, 4L),
                new Instruction(InstructionType.LABEL, "1", null)
        );

        virtualMachine.interpret(ifStatement);

        assertEquals(1, stack.size());
        assertEquals(5L, stack.pop());
    }

    @Test
    public void ifElseFalseTest() {

        List<Instruction> ifStatement = List.of(
                new Instruction(InstructionType.PUSH, null, 0L),
                new Instruction(InstructionType.PUSH, null, 0L),
                new Instruction(InstructionType.GT, null, null),
                new Instruction(InstructionType.JUMP_IF_FALSE, "0", null),
                new Instruction(InstructionType.PUSH, null, 5L),
                new Instruction(InstructionType.JUMP, "1", null),
                new Instruction(InstructionType.LABEL, "0", null),
                new Instruction(InstructionType.PUSH, null, 4L),
                new Instruction(InstructionType.LABEL, "1", null)
        );

        virtualMachine.interpret(ifStatement);

        assertEquals(1, stack.size());
        assertEquals(4L, stack.pop());
    }


}
