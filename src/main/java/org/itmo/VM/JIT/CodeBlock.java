package org.itmo.VM.JIT;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.itmo.VM.instructions.Instruction;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CodeBlock {
    private Integer start;
    private Integer entry = 0;
    private Integer end;
    private List<Runnable> compiled;
    private final List<Instruction> instructions = new ArrayList<>();

    public void enter() {
        entry++;
    }
}

