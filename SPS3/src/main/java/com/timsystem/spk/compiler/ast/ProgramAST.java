package com.timsystem.spk.compiler.ast;

import com.timsystem.spk.vm.Bytecode;
import com.timsystem.spk.vm.Instructions;

import java.util.ArrayList;

public class ProgramAST implements AST {

    private ArrayList<AST> program;

    public ProgramAST(ArrayList<AST> program) {
        this.program = program;
    }

    @Override
    public void compile(Bytecode bytecode) {
        for (AST ast : program) {
            ast.compile(bytecode);
        }
        bytecode.writeInstruction(Instructions.OP_HALT, -1);
    }

    public ArrayList<AST> getProgram() {
        return program;
    }

    public void setProgram(ArrayList<AST> program) {
        this.program = program;
    }

    @Override
    public String toString() {
        return "ProgramAST{" +
                "program=" + program +
                '}';
    }
}
