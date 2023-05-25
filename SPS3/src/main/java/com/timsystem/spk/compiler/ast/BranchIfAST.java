package com.timsystem.spk.compiler.ast;

public class BranchIfAST implements AST {

    private AST expr;
    private AST body;
    private AST elseBody;

    public static int BRANCH_IF_COUNTER = 0;

    public BranchIfAST(AST expr, AST body, AST elseBody) {
        this.expr = expr;
        this.body = body;
        this.elseBody = elseBody;
    }

    @Override
    public String compile() {
        StringBuilder builder = new StringBuilder();
        builder.append(expr.compile()).append("\n");
        builder.append("jit branch_if_true_body").append(BRANCH_IF_COUNTER).append("\n");
        if (elseBody != null) {
            builder.append(elseBody.compile()).append("\n\n");
            builder.append("jmp after_branch_if").append(BRANCH_IF_COUNTER).append("\n");
        }
        builder.append("branch_if_true_body").append(BRANCH_IF_COUNTER).append(":\n");
        builder.append(body.compile()).append("\n");
        builder.append("after_branch_if").append(BRANCH_IF_COUNTER).append(":\n");
        BRANCH_IF_COUNTER++;
        return builder.toString();
    }
}
