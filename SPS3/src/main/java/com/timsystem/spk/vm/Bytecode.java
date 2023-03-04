package com.timsystem.spk.vm;

import java.util.ArrayList;

public class Bytecode {

    private ArrayList<Byte> bytecode;
    private ArrayList<Object> constants;

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
}
