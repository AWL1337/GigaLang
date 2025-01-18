package org.itmo.VM.memory.object;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
public class Identifier {
    private final String name;
    @EqualsAndHashCode.Exclude
    private MemoryObject object;
}
