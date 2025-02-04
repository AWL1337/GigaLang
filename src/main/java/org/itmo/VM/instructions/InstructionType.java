package org.itmo.VM.instructions;

public enum InstructionType {
    ADD, SUB, MUL, DIV, MOD, POW, SQRT,
    NOT, AND, OR,
    GT, LT, GE, LE, EQ, NE,
    STORE, READ, ARRAY_STORE, ARRAY_LOAD,ARRAY_CREATE, LOAD_VAR, PUSH,
    LABEL,
    JUMP, JUMP_IF_FALSE,
    FUN, END_FUN, CALL, RETURN,
    PRINT, PRINTLN
}
