package com.timsystem.spk.compiler.ast;

public class ForAST implements AST{
    private AST variable;
    private AST condition;
    private AST counter;
    private int num;
    private int line;
    public ForAST(AST variable, AST condition, AST counter, int num, int line){
        this.variable = variable;
        this.condition = condition;
        this.counter = counter;
        this.num = num;
        this.line = line;
    }
    @Override
    public String compile() {
        String acc = "PUSH_SCOPE\n";
        String label = "label_" + (num + 1);
        String label2 = "label_" + (num + 2);
        acc += variable.compile();
        acc += label + ":\n";
        acc += condition.compile();
        acc += "JIF " + label2 + "\n";
        acc += counter.compile();
        acc += label2 + ":\n";
        acc += "POP_SCOPE\n";
        return acc;
    }
}
