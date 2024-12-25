package org.itmo.VM.instructions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.itmo.VM.memory.object.MemoryObject;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Instruction {
    private final InstructionType type; // тип команды
    private final List<Instruction> body; // команды внутри цикла/функция
    private final String name; // имя переменной
    private final String leftName; //
    private final String rightName; //
    private final MemoryObject object;
    private final MemoryObject leftOperand;
    private final MemoryObject rightOperand;
}
