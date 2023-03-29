package com.timsystem.spk.compiler.ast;

import com.timsystem.spk.vm.Bytecode;

public interface AST {

    void compile(Bytecode bytecode);

}
