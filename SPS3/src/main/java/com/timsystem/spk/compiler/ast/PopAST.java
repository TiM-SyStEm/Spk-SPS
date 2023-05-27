package com.timsystem.spk.compiler.ast;

public class PopAST implements AST {

    private AST ast;

    public PopAST(AST ast) {
        this.ast = ast;
    }

    @Override
    public String compile() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(ast.compile()).append("\n");
        buffer.append("POP\n");
        return buffer.toString();
    }
}
