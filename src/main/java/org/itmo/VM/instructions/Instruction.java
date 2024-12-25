package org.itmo.VM.instructions;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Instruction {
    private final InstructionType type;

    private final List<Instruction> body;
}
