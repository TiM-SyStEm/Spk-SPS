package com.timsystem.spk.compiler.ast;

import com.timsystem.spk.vm.Bytecode;
import com.timsystem.spk.vm.Instructions;

public class DefineVariableAST implements AST {

    private AST expr;
    private String name;
    private int line;

    public DefineVariableAST(AST expr, String name, int line) {
        this.expr = expr;
        this.name = name;
        this.line = line;
    }

    @Override
    public void compile(Bytecode bytecode) {
        expr.compile(bytecode);
        bytecode.writeInstruction(Instructions.OP_CREATE_VAR, line);
        bytecode.writeRawConstant(name, line);
    }

    public AST getExpr() {
        return expr;
    }

    public void setExpr(AST expr) {
        this.expr = expr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "DefineVariableAST{" +
                "expr=" + expr +
                ", name='" + name + '\'' +
                ", line=" + line +
                '}';
    }
}
