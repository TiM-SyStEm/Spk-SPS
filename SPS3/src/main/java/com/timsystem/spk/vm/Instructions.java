package com.timsystem.spk.vm;

public class Instructions {

    public static final byte OP_PUSH = 1,
                        OP_DUP = 2,
                        OP_POP = 3,
                        OP_FRAME = 4,
                        OP_FLIP = 5,
                        OP_SWAP = 6,
                        OP_OUT = 7,
                        OP_INP = 8,
                        OP_HALT = 9,
                        OP_BINARY = 10,
                        OP_NEGATE = 11,
                        OP_CREATE_VAR = 12, // variable creation
                        OP_GET_VAR = 13, // retrieve variable value
                        OP_SIGN = 14,
                        OP_CHUNKS = 15,
                        OP_CURCH = 16,
                        OP_CHUSZ = 17,
                        OP_JMP = 18,
                        OP_JE = 19,
                        OP_JNE = 20,
                        OP_JL = 21,
                        OP_JG = 22,
                        OP_JLE = 23,
                        OP_JGE = 24,
                        OP_JLN = 25,
                        OP_JGN = 26,
                        OP_JEV = 27,
                        OP_JUE = 28,
                        OP_LOOP = 29;
}
