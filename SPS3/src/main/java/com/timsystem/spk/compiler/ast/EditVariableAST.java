package com.timsystem.spk.compiler.ast;

public class EditVariableAST implements AST{
    private AST expr;
    private String name;
    private int line;
    public EditVariableAST(AST expr, String name, int line){
        this.expr = expr;
        this.name = name;
        this.line = line;
    }
    @Override
    public String compile() {
        String acc = expr.compile() + "\n";
        acc += "EDIT_VAR " + name + "\n";
        return acc;
    }
}
