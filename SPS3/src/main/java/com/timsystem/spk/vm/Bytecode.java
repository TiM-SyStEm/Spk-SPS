package com.timsystem.spk.vm;

import com.timsystem.spk.compiler.lib.IntegerBytesConvert;

import java.util.ArrayList;

public class Bytecode {

    private ArrayList<Byte> bytecode;
    private ArrayList<Object> constants;

    private ArrayList<Integer> lines;

    public Bytecode() {
        this.bytecode = new ArrayList<>();
        this.constants = new ArrayList<>();
        this.lines = new ArrayList<>();
    }

    public void writeInstruction(byte instruction, int line) {
        bytecode.add(instruction);
        lines.add(line);
    }

    public int writeConstant(Object constant, int line) {
        writeInstruction(Instructions.OP_PUSH, line);
        constants.add(constant);
        int index = -1;
        if (constants.contains(constant)) {
            index = constants.indexOf(constant);
        } else {
            index = constants.size() - 1;
        }
        byte[] intBytes = IntegerBytesConvert.int2ByteArr(index);
        for (byte b : intBytes) {
            writeInstruction(b, line);
        }
        return constants.size() - 1;
    }

    public void writeBinary(char operation, int line) {
        writeInstruction(Instructions.OP_BINARY, line);
        writeInstruction((byte) operation, line);
    }

    public ArrayList<Byte> getBytecode() {
        return bytecode;
    }

    public void setBytecode(ArrayList<Byte> bytecode) {
        this.bytecode = bytecode;
    }

    public ArrayList<Object> getConstants() {
        return constants;
    }

    public void setConstants(ArrayList<Object> constants) {
        this.constants = constants;
    }

    public ArrayList<Integer> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Integer> lines) {
        this.lines = lines;
    }
}
