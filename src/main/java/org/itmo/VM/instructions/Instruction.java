package org.itmo.VM.instructions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Builder;

@Data
@RequiredArgsConstructor
@Builder
public class Instruction {
    private final InstructionType type; // тип команды
    private final String name; // имя переменной
    private final Long value;
}
