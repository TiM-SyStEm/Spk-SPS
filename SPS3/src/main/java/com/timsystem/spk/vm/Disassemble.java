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
                    byte[] indexBytes = new byte[]{
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
                    byte[] indexBytes = new byte[]{
                            bytes.get(i + 1),
                            bytes.get(i + 2),
                            bytes.get(i + 3),
                            bytes.get(i + 4)
                    };
                    i += 4;
                    constantInstruction("OP_GET_VAR", bytecode, IntegerBytesConvert.byteArr2Int(indexBytes));
                }
                case Instructions.OP_CREATE_VAR -> {
                    byte[] indexBytes = new byte[]{
                            bytes.get(i + 1),
                            bytes.get(i + 2),
                            bytes.get(i + 3),
                            bytes.get(i + 4)
                    };
                    i += 4;
                    constantInstruction("OP_CREATE_VAR", bytecode, IntegerBytesConvert.byteArr2Int(indexBytes));
                }
                case Instructions.OP_JMP -> {
                    jumpInstruction("OP_JMP", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_JE -> {
                    jumpInstruction("OP_JE", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_JNE -> {
                    jumpInstruction("OP_JNE", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_JL -> {
                    jumpInstruction("OP_JL", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_JG -> {
                    jumpInstruction("OP_JG", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_JLE -> {
                    jumpInstruction("OP_JLE", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_JGE -> {
                    jumpInstruction("OP_JGE", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_JLN -> {
                    jumpInstruction("OP_JLN", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_JGN -> {
                    jumpInstruction("OP_JGN", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_JEV -> {
                    jumpInstruction("OP_JEV", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_JUE -> {
                    jumpInstruction("OP_JUE", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_LOOP -> {
                    jumpInstruction("OP_LOOP", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_CALL -> {
                    jumpInstruction("OP_CALL", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_RET -> immediateInstruction("OP_RET");
                case Instructions.OP_AND -> immediateInstruction("OP_AND");
                case Instructions.OP_OR -> immediateInstruction("OP_OR");
                case Instructions.OP_XOR -> immediateInstruction("OP_XOR");
                case Instructions.OP_NOT -> immediateInstruction("OP_NOT");
                case Instructions.OP_JIT -> {
                    jumpInstruction("OP_JIT", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_JIF -> {
                    jumpInstruction("OP_JIF", bytecode.getBytecode(), i);
                    i += 4;
                }
                case Instructions.OP_FRGET -> {
                    constantInstruction("OP_FRGET", bytecode, i);
                    i += 4;
                }
                case Instructions.OP_CLR -> {
                    immediateInstruction("OP_CLR");
                }
                case Instructions.OP_PROC -> {
                    // why?
                }
                default -> {
                    System.out.printf("NO SPECIFICATION FOR " + instruction + " OPCODE!\n");
                }
            }
            previousLine = line;
        }
    }

    private static void jumpInstruction(String name, ArrayList<Byte> bytes, int i) {
        byte[] indexBytes = new byte[]{
                bytes.get(i + 1),
                bytes.get(i + 2),
                bytes.get(i + 3),
                bytes.get(i + 4)
        };
        int jumpAddress = IntegerBytesConvert.byteArr2Int(indexBytes);
        System.out.printf("%s %s\n", name, jumpAddress);
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
        System.out.printf("%s %s %s %s\n", name, constant.toString(), constant.getClass().getSimpleName(), String.valueOf(index));
    }

}
