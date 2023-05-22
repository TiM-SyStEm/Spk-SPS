package com.timsystem.spk.compiler.ast;

import com.timsystem.spk.vm.Bytecode;
import com.timsystem.spk.compiler.lib.SPASTranslator;

public class ConstantAST implements AST {

    private Object constant;
    private int line;

    public ConstantAST(Object constant, int line) {
        this.constant = constant;
        this.line = line;
    }

    @Override
    public String compile() {
        String acc = "PUSH inline " + SPASTranslator.getSPASRepresentation(constant);
        return acc;
    }

    public Object getConstant() {
        return constant;
    }

    public void setConstant(Object constant) {
        this.constant = constant;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "ConstantAST{" +
                "constant=" + constant +
                ", line=" + line +
                '}';
    }
}
