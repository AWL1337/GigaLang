package org.itmo.VM.instructions;

public enum InstructionType {
    ADD, SUB, MUL, DIV, POW, MOD, AND, OR, NOT,
    STORE, READ, ARRAY_STORE, ARRAY_LOAD,ARRAY_CREATE, LOAD_VAR, PUSH,
    FOR, WHILE, FUNCTION_CALL, FUNCTION_DEF, RETURN, POP
}
