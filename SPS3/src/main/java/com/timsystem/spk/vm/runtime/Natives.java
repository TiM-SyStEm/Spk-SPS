package com.timsystem.spk.vm.runtime;

import com.timsystem.spk.vm.Run;

import java.util.HashMap;

public class Natives {
    public static HashMap<String, NativeFunc> natives = new HashMap<>();
    public static void Add(String name, NativeFunc nf){
        natives.put(name, nf);
    }
    public static void Run(String name, Run run){
        natives.get(name).func(run);
    }
}