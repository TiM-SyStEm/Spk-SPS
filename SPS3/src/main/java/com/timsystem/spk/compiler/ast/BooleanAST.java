package com.timsystem.spk.compiler.ast;

public class BooleanAST implements AST{
    private String value;
    private int line;
    public BooleanAST(String value, int line){
        this.value = value;
        this.line = line;
    }

    @Override
    public String compile() {
        return "PUSH inline " + value.toLowerCase() + "\n";
    }
}
