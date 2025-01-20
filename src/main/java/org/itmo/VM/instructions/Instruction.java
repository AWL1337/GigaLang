package org.itmo.VM.instructions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Instruction {
    private final InstructionType type; // тип команды
    private String name; // имя переменной
    private final Long value;
    private List<String> arguments;

    public Instruction(InstructionType type, String name, Long value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }
}
