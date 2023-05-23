package com.timsystem.spk.compiler.ast;

public class NullAST implements AST{
    private int line;
    public NullAST(int line){
        this.line = line;
    }

    @Override
    public String compile() {
        return "PUSH inline null\n";
    }
}
