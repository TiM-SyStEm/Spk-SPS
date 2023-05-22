package com.timsystem.spk.compiler.ast;

import com.timsystem.spk.vm.Bytecode;
import com.timsystem.spk.vm.Instructions;

public class UnaryAST implements AST {

    private AST expr;
    private char operation;
    private int line;

    public UnaryAST(AST expr, char operation, int line) {
        this.expr = expr;
        this.operation = operation;
        this.line = line;
    }

    @Override
    public String compile() {
        expr.compile();
        String acc = "";
        switch (operation) {
            case '-' -> acc = "SIGN";
        }
        return acc;
    }

    @Override
    public String toString() {
        return "UnaryAST{" +
                "expr=" + expr +
                ", operation=" + operation +
                ", line=" + line +
                '}';
    }
}
