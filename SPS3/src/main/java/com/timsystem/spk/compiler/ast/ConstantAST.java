package com.timsystem.spk.compiler.ast;

import com.timsystem.spk.vm.Bytecode;

public class ConstantAST implements AST {

    private Object constant;
    private int line;

    public ConstantAST(Object constant, int line) {
        this.constant = constant;
        this.line = line;
    }

    @Override
    public void compile(Bytecode bytecode) {
        bytecode.writeConstant(constant, line);
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
