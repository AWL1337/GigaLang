package org.itmo.VM.memory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class FunctionInfo {
    private final String name;
    private final Integer start;
    private Integer end;
    private final List<String> parameters;
}
