package com.timsystem.spk.vm.lib;

public class Loop {
    private int label;
    private int count;
    private int counter;
    public Loop(int label, int count){
        this.label = label;
        this.count = count;
        this.counter = 0;
    }
    public void setCounter(int c) {
        counter = c;
    }
    public int getCounter() {
        return counter;
    }
    public int getLabel(){
        return label;
    }
    public int getCount(){
        return count;
    }
}
