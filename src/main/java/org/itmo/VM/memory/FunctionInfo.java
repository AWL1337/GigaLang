package org.itmo.VM.memory;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class FunctionInfo {
    private final String name;
    private final Integer start;
    private final List<String> parameters;
}
