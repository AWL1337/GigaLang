package org.itmo.VM.JIT;

import lombok.RequiredArgsConstructor;
import org.itmo.VM.VirtualMachine;
import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.instructions.InstructionType;

import java.util.*;

@RequiredArgsConstructor
public class Compiler {
    private final VirtualMachine vm;


    public void compile(CodeBlock codeBlock) {
        var instructions = codeBlock.getInstructions();
        instructions = deadCode(instructions);
        variableCompression(instructions);

        codeBlock.setCompiled(instructions.stream().map(vm::execute).toList());
    }

    private List<Instruction> deadCode(List<Instruction> instructions) {
        Set<String> usedLabels = new HashSet<>();
        Set<String> liveVariables = new HashSet<>();
        List<Instruction> optimized = new ArrayList<>();

        // Проход в обратном порядке для анализа использования переменных и меток
        ListIterator<Instruction> it = instructions.listIterator(instructions.size());
        while (it.hasPrevious()) {
            Instruction instruction = it.previous();

            switch (instruction.getType()) {
                case JUMP, JUMP_IF_FALSE -> usedLabels.add(instruction.getName());
                case LABEL -> {
                    if (usedLabels.contains(instruction.getName())) {
                        optimized.addFirst(instruction);
                        usedLabels.remove(instruction.getName());
                    }
                }
                case STORE -> {
                    if (liveVariables.contains(instruction.getName())) {
                        optimized.addFirst(instruction);
                        liveVariables.remove(instruction.getName());
                    }
                }
                case LOAD_VAR -> {
                    liveVariables.add(instruction.getName());
                    optimized.addFirst(instruction);
                }
                default -> optimized.addFirst(instruction);
            }
        }

        return optimized;
    }

    private void variableCompression(List<Instruction> instructions) {
        Map<String, String> activeVariables = new HashMap<>();
        Map<String, Integer> lastUsage = new HashMap<>();
        int variableCounter = 0;

        // Проанализировать, где используется каждая переменная
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            if (instruction.getType() == InstructionType.LOAD_VAR || instruction.getType() == InstructionType.STORE) {
                lastUsage.put(instruction.getName(), i);
            }
        }

        // Переписать инструкции с переиспользованием переменных
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);

            if (instruction.getType() == InstructionType.STORE) {
                String originalName = instruction.getName();

                // Найти, можно ли переиспользовать существующую переменную
                String reusedVariable = activeVariables.get(originalName);
                if (reusedVariable == null) {
                    reusedVariable = "v" + variableCounter++;
                    activeVariables.put(originalName, reusedVariable);
                }

                // Если переменная больше не используется, освободить её
                if (lastUsage.get(originalName) == i) {
                    activeVariables.remove(originalName);
                }

                instruction.setName(reusedVariable);
            } else if (instruction.getType() == InstructionType.LOAD_VAR) {
                String originalName = instruction.getName();
                String reusedVariable = activeVariables.get(originalName);

                if (reusedVariable != null) {
                    instruction.setName(reusedVariable);
                }
            }
        }
    }
}
