package com.timsystem.spk.compiler.ast;

public class StdInputAST implements AST{
    private AST placeholder;
    private int line;

    public StdInputAST(AST placeholder, int line){
        this.placeholder = placeholder;
        this.line = line;
    }
    @Override
    public String compile() {
        String acc = placeholder.compile() + "\n";
        acc += "INP\n";
        return acc;
    }
}
