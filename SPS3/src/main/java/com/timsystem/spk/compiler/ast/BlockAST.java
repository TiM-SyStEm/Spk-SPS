package com.timsystem.spk.compiler.ast;

import java.util.ArrayList;

public class BlockAST implements AST{
    private ArrayList<AST> block;
    public BlockAST(ArrayList<AST> block){
        this.block = block;
    }
    @Override
    public String compile() {
        StringBuilder block_comp = new StringBuilder();
        for(AST ast : block){
            block_comp.append(ast.compile());
        }
        return block_comp.toString();
    }
}
