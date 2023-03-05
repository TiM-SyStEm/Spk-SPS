package com.timsystem.spk;

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

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Bytecode bc = new Bytecode();
        Parser parser = new Parser(new Lexer("1 + 1 + 1"));
        AST ast = parser.parse();
        ast.compile(bc);
        Disassemble.disassemble("AST test", bc);
    }
}