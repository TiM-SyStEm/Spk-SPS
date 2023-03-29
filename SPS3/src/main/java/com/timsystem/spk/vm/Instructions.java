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
                        OP_GET_VAR = 13; // retrieve variable value

}
