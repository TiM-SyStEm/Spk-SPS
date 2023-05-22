package com.timsystem.spk.compiler.lib;

public class SPASTranslator {
    public static String getSPASRepresentation(Object constant){
        if(constant instanceof String){
            return "\"" + constant.toString() + "\"";
        }
        else {
            return constant.toString();
        }
    }
}
