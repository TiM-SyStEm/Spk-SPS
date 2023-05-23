package com.timsystem.spk.compiler.ast;

public class DoAST implements AST{
    private AST block;
    private int num;
    private int line;
    public DoAST(AST block, int num, int line){
        this.block = block;
        this.num = num;
        this.line = line;
    }
    @Override
    public String compile() {
        String label = "label_" + (num + 1);
        String acc = label + ":\n";
        acc += block.compile();
        acc += "PUSH inline true\n";
        acc += "LOOP " + label + "\n";
        return acc;
    }
}
