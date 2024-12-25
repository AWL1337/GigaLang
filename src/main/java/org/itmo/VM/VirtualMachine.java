package org.itmo.VM;

import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.memory.Manager;

import java.util.List;


public class VirtualMachine {
    private final Manager manager = new Manager();

    public void interpret(List<Instruction> instructions) {

    }

    private void execute(Instruction instruction) {
        switch (instruction.getType()) {
            case ADD:

        }

    }

    private void handleAdd() {}
}
