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
        BRANCH_IF_COUNTER++;
        int index = BRANCH_IF_COUNTER;
        builder.append(expr.compile()).append("\n");
        builder.append("jit branch_if_true_body").append(index).append("\n");
        if (elseBody != null) {
            builder.append(elseBody.compile()).append("\n\n");
        }
        builder.append("jmp after_branch_if").append(index).append("\n");
        builder.append("branch_if_true_body").append(index).append(":\n");
        builder.append(body.compile()).append("\n");
        builder.append("after_branch_if").append(index).append(":\n");
        return builder.toString();
    }
}
