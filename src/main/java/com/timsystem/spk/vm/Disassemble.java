package com.timsystem.spk.vm;

import com.timsystem.spk.compiler.lib.IntegerBytesConvert;

import java.util.ArrayList;

public class Disassemble {

    public static void disassemble(String name, Bytecode bytecode) {
        System.out.printf("Disassemble of %s:\n", name);
        ArrayList<Byte> bytes = bytecode.getBytecode();
        ArrayList<Integer> lines = bytecode.getLines();
        int previousLine = -1;
        for (int i = 0; i < bytes.size(); i++) {
            byte instruction = bytes.get(i);
            int line = lines.get(i);
            System.out.printf("%04d %s ", i, line == previousLine ? "|" : line);
            switch (instruction) {
                case Instructions.OP_PUSH -> {
                    byte[] indexBytes = new byte[] {
                            bytes.get(i + 1),
                            bytes.get(i + 2),
                            bytes.get(i + 3),
                            bytes.get(i + 4)
                    };
                    i += 4;
                    constantInstruction("OP_PUSH", bytecode, IntegerBytesConvert.byteArr2Int(indexBytes));
                }
                case Instructions.OP_DUP -> {
                    immediateInstruction("OP_DUP");
                }
                case Instructions.OP_POP -> {
                    immediateInstruction("OP_POP");
                }
                case Instructions.OP_FRAME -> {
                    System.out.printf("NO SPECIFICATION FOR FRAME OPCODE\n");
                }
                case Instructions.OP_SWAP -> {
                    immediateInstruction("OP_SWAP");
                }
                case Instructions.OP_FLIP -> {
                    immediateInstruction("OP_FLIP");
                }
                case Instructions.OP_OUT -> {
                    immediateInstruction("OP_OUT");
                }
                case Instructions.OP_INP -> {
                    immediateInstruction("OP_INP");
                }
                case Instructions.OP_HALT -> {
                    immediateInstruction("OP_HALT");
                }
                case Instructions.OP_BINARY -> {
                    characterInstruction("OP_BINARY", bytecode, i);
                    i++; // operator char
                }
                case Instructions.OP_NEGATE -> {
                    immediateInstruction("OP_NEGATE");
                }
                case Instructions.OP_GET_VAR -> {
                    byte[] indexBytes = new byte[] {
                            bytes.get(i + 1),
                            bytes.get(i + 2),
                            bytes.get(i + 3),
                            bytes.get(i + 4)
                    };
                    i += 4;
                    constantInstruction("OP_GET_VAR", bytecode, IntegerBytesConvert.byteArr2Int(indexBytes));
                }
                case Instructions.OP_CREATE_VAR -> {
                    byte[] indexBytes = new byte[] {
                            bytes.get(i + 1),
                            bytes.get(i + 2),
                            bytes.get(i + 3),
                            bytes.get(i + 4)
                    };
                    i += 4;
                    constantInstruction("OP_CREATE_VAR", bytecode, IntegerBytesConvert.byteArr2Int(indexBytes));
                }
                default -> {
                    System.out.printf("NO SPECIFICATION FOR " + instruction + " OPCODE!\n");
                }
            }
            previousLine = line;
        }
    }

    private static void characterInstruction(String name, Bytecode bytecode, int index) {
        char operation = (char) (bytecode.getBytecode().get(index + 1) & 0xFF);
        System.out.printf("%s '%s'\n", name, String.valueOf(operation));
    }
    private static void immediateInstruction(String name) {
        System.out.printf("%s\n", name);
    }
    private static void constantInstruction(String name, Bytecode bytecode, int index) {
        Object constant = bytecode.getConstants().get(index);
        System.out.printf("%s %s %s\n", name, constant.toString(), String.valueOf(index));
    }

}
