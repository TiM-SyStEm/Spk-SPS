package com.timsystem.spk.compiler.ast;

public class StringAST implements AST{
    private String value;
    private int line;
    public StringAST(String value, int line){
        this.value = value;
        this.line = line;
    }
    @Override
    public String compile() {
        return "PUSH inline \"" + value + "\"\n";
    }
}
