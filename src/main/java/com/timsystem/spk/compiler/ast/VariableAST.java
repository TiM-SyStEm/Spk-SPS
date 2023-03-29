package com.timsystem.spk.compiler.ast;

import com.timsystem.spk.vm.Bytecode;
import com.timsystem.spk.vm.Instructions;

public class VariableAST implements AST {

    private String variable;
    private int line;

    public VariableAST(String variable, int line) {
        this.variable = variable;
        this.line = line;
    }

    @Override
    public void compile(Bytecode bytecode) {
        bytecode.writeInstruction(Instructions.OP_GET_VAR, line);
        bytecode.writeRawConstant(variable, line);
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "VariableAST{" +
                "variable='" + variable + '\'' +
                ", line=" + line +
                '}';
    }
}
