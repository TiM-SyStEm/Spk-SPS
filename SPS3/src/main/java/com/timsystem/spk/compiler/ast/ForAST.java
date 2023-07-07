package com.timsystem.spk.compiler.ast;

public class ForAST implements AST {

    private AST initializer;
    private AST condition;
    private AST termination;
    private AST body;

    public static int FOR_INDEX = 0;

    public ForAST(AST initializer, AST condition, AST termination, AST body) {
        this.initializer = initializer;
        this.condition = condition;
        this.termination = termination;
        this.body = body;
    }

    @Override
    public String compile() {
        int index = FOR_INDEX++;
        StringBuilder buffer = new StringBuilder();
        buffer.append(initializer.compile()).append("\n\n");
        buffer.append("for_condition_label_").append(index).append(":\n");
        buffer.append(condition.compile()).append("\n");
        buffer.append("JIF after_for_label_").append(index).append("\n");
        buffer.append(body.compile()).append("\n\n");
        buffer.append(termination.compile()).append("\n\n");
        buffer.append("JMP for_condition_label_").append(index).append("\n");
        buffer.append("after_for_label_").append(index).append(":\n\n");
        return buffer.toString();
    }
}
