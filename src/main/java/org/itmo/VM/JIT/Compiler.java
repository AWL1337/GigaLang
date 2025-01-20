package org.itmo.VM.JIT;

import lombok.RequiredArgsConstructor;
import org.itmo.VM.VirtualMachine;
import org.itmo.VM.instructions.Instruction;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class Compiler {
    private final VirtualMachine vm;


    public void compile(CodeBlock codeBlock) {
        var instructions = codeBlock.getInstructions();
        instructions = deadCode(instructions);
        instructions = variableCompression(instructions);

        codeBlock.setCompiled(instructions.stream().map(vm::execute).toList());
    }

    private List<Instruction> deadCode(List<Instruction> instructions) {

        return instructions;
    }

    private List<Instruction> variableCompression(List<Instruction> instructions) {

        return instructions;
    }
}
