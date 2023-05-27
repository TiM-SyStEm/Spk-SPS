package com.timsystem.spk.compiler.ast;

public class IncVariableAST implements AST{
    private String name;
    private int line;
    public IncVariableAST(String name, int line){
        this.name = name;
        this.line = line;
    }
    @Override
    public String compile() {
        String acc = "GET_VAR " + name + "\n";
        acc += "PUSH inline 1\n";
        acc += "BINARY '+'\n";
        acc += "EDIT_VAR " + name + "\n";
        return acc;
    }
}
