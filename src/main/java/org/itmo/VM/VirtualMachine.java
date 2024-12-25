package org.itmo.VM;

import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.memory.Manager;
import org.itmo.VM.memory.object.MemoryObject;

import java.util.List;
import java.util.Stack;


public class VirtualMachine {
    private final Manager manager = new Manager();

    private final Stack<Long> stack = new Stack<>();
    private void execute(Instruction instruction) {
        switch (instruction.getType()) {
            case STORE:
               StoreOp(instruction.getName(), instruction.getObject());
            case ADD:
                AddOp(instruction.getLeftOperand(), instruction.getRightOperand(), instruction.getLeftName(), instruction.getRightName());

        }

    }

    // сохранить переменную
    private void StoreOp(String name, MemoryObject object) {
        manager.allocate(name, object);
    }

    // + сложение
    private void AddOp(MemoryObject leftOp, MemoryObject rightOp, String leftName, String rightName) {
        Long left = (leftOp != null) ? (Long)leftOp.getValue() : manager.getLong(leftName);
        Long right = (rightOp != null) ?  (Long)rightOp.getValue() : manager.getLong(rightName);

        stack.push(left + right);
    }
}
