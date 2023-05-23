package com.timsystem.spk.compiler.ast;

public class StdOutAST implements AST{
    private AST value;
    private int line;
    public StdOutAST(AST value, int line){
        this.value = value;
        this.line = line;
    }

    @Override
    public String compile() {
        String acc = value.compile() + "\n";
        acc += "OUT\n";
        return acc;
    }
}
