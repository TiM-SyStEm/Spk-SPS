package com.timsystem.spk.compiler.ast;

public class WhileAST implements AST{
    private AST block;
    private int num;
    private AST condition;
    private int line;
    public WhileAST(AST block, int num, AST condition, int line){
        this.block = block;
        this.num = num;
        this.condition = condition;
        this.line = line;
    }
    @Override
    public String compile() {
        String label = "label_" + (num + 1);
        String label2 = "label_" + (num + 2);
        String acc = label + ":\n";
        acc += condition.compile();
        acc += "JIF " + label2 + "\n";
        acc += block.compile();
        acc += "PUSH inline true \n";
        acc += "LOOP " + label + "\n";
        acc += label2 + ":\n";
        return acc;
    }
}
