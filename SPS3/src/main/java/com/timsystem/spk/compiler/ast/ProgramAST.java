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
    public String compile() {
        String acc = "#build sps\n";
        for (AST ast : program) {
            acc += ast.compile();
        }
        acc += "HALT";
        return acc;
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
