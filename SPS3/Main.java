package com.timsystem.spk;

import com.timsystem.spk.asm.AssemblerCompiler;
import com.timsystem.spk.compiler.Lexer;
import com.timsystem.spk.compiler.Parser;
import com.timsystem.spk.compiler.ast.AST;
import com.timsystem.spk.compiler.ast.BinaryAST;
import com.timsystem.spk.compiler.ast.ConstantAST;
import com.timsystem.spk.compiler.ast.ProgramAST;
import com.timsystem.spk.compiler.lib.Token;
import com.timsystem.spk.compiler.lib.TokenType;
import com.timsystem.spk.vm.Bytecode;
import com.timsystem.spk.vm.Disassemble;
import com.timsystem.spk.vm.Instructions;
import com.timsystem.spk.vm.Run;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*Bytecode bc = new Bytecode();
        Parser parser = new Parser(new Lexer("var abc = 2 + 2 var abcd = abc + 1"));
        AST ast = parser.parse();
        ast.compile(bc);
        Disassemble.disassemble("AST test", bc);*/
        String SPAS_CODE = "#build sps\n" +
                "label:\n" +
                "PUSH 0\n" +
                "OUT\n" +
                "PUSH 1\n" +
                "RET\n" +
                "CALL label\n"+
                "PUSH 1\n" +
                "HALT\n" +
                ".data:\n" +
                "0 is \"Hello!\"\n" +
                "1 is 5";
        Bytecode bc = AssemblerCompiler.compile(SPAS_CODE);
        Run run = new Run(bc);
        run.run();
        Disassemble.disassemble("AST test", bc);
    }
}