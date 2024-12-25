package org.itmo.VM.memory.object;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MemoryObject {
    private final ObjectType type;
    private final Object value;
    private Boolean isAlive = false;
}
