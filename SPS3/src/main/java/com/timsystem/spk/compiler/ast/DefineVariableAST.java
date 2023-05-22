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
    public String compile() {
        String acc = "";
        acc += expr.compile();
        acc += "CREATE_VAR " + name + "\n";
        return acc;
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
