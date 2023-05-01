package com.timsystem.spk.vm.lib;

public class JavaTuple {
    private final Object key;
    private final Object value;

    public JavaTuple(Object key, Object value) {
        super();
        this.key = key;
        this.value = value;
    }
    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
