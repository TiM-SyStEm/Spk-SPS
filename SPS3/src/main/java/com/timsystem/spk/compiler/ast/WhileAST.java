package com.timsystem.spk.compiler.ast;

public class WhileAST implements AST {

    private AST block;
    private AST condition;
    private int line;

    public static int WHILE_INDEX = 0;

    public WhileAST(AST block, AST condition, int line){
        this.block = block;
        this.condition = condition;
        this.line = line;
    }
    @Override
    public String compile() {
        int index = WHILE_INDEX++;
        String label = "begin_while_" + index;
        String label2 = "end_while_" + index;
        String acc = label + ":\n";
        acc += condition.compile();
        acc += "JIF " + label2 + "\n";
        acc += block.compile();
        acc += "JMP " + label + "\n";
        acc += label2 + ":\n";
        return acc;
    }
}
