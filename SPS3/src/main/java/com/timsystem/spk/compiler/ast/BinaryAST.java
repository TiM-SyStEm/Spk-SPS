package com.timsystem.spk.compiler.ast;

import com.timsystem.spk.vm.Bytecode;

public class BinaryAST implements AST {

    private AST expr1, expr2;
    private char operation;
    private int line;

    public BinaryAST(AST expr1, AST expr2, char operation, int line) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.operation = operation;
        this.line = line;
    }

    @Override
    public String compile() {
        String acc = "";
        acc += expr1.compile() + "\n";
        acc += expr2.compile() + "\n";
        acc += "BINARY '" + operation + "'\n";
        return acc;
    }

    @Override
    public String toString() {
        return "BinaryAST{" +
                "expr1=" + expr1 +
                ", expr2=" + expr2 +
                ", operation=" + operation +
                ", line=" + line +
                '}';
    }
}
