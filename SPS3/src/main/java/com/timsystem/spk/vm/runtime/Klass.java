package com.timsystem.spk.vm.runtime;

import java.util.HashMap;
import java.util.Map;

public class Klass implements Container {
    private String name;
    private Map<String, Container> cont;

    public Klass(String name, Map<String, Container> cont) {
        this.name = name;
        this.cont = cont;
    }
    public void put(String k, Container v){
        cont.put(k, v);
    }
    public Container get(String i){
        return cont.get(i);
    }
    @Override
    public String getName() {
        return name;
    }
}
