package com.timsystem.spk;

import com.timsystem.spk.vm.Bytecode;
import com.timsystem.spk.vm.Run;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");
        ArrayList<Byte> bts = new ArrayList<>();
        bts.add((byte)1);
        bts.add((byte)0);
        bts.add((byte)0);
        Bytecode bytecode = new Bytecode();
        bytecode.setConstants(new ArrayList<Object>());
        bytecode.setBytecode(bts);
        bytecode.getConstants().add(0);
        new Run(bytecode).run();
    }
}