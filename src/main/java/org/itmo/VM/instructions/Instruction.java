package org.itmo.VM.instructions;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Instruction {
    private final InstructionType type;

    private final List<Instruction> body;
}
