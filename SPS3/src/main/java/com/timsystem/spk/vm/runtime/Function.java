package com.timsystem.spk.vm.runtime;

import java.util.HashMap;
import java.util.Map;

public class Function implements Container{
    private String name;
    private int args;
    private int code;

    public Function(String name, int args, int code) {
        this.name = name;
        this.args = args;
        this.code = code;
    }
    public void execute(JavaFunc func, Object[] arguments){
        if(arguments.length == args){
            func.execute(arguments);
        }
        else{
            // ERROR
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
