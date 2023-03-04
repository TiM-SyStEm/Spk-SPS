package com.timsystem.spk.vm;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Run {
    private Bytecode bytecode;
    private Stack<Object> stack;
    private byte current;
    private boolean commandMode;
    private int pos = 0;
    private ArrayList<Byte> bytes;

    public Run(Bytecode bytecode){
        this.bytecode = bytecode;
    }
    public void run(){
        // initialize bytes
        bytes = bytecode.getBytecode();
        // initialize stack
        stack = new Stack<>();
        read();
    }
    public void read(){
        commandMode = true;
        while(pos < bytes.size()){
            current = bytes.get(pos);
            if(commandMode){
                switch (current) {
                    case 1 -> {
                        // PUSH
                        commandMode = false;
                        readPush();
                    }
                    case 2 -> {
                        // DUP
                        commandMode = false;
                        readDup();
                    }
                    case 3 -> {
                        // POP
                        commandMode = false;
                        readPop();
                    }
                    case 4 -> {
                        // FRAME
                        commandMode = false;
                        readFrame();
                    }
                    case 5 -> {
                        // FLIP
                        commandMode = false;
                        readFlip();
                    }
                    case 6 -> {
                        // SWAP (swap end and pre-end)
                        commandMode = false;
                        readSwap();
                    }
                    case 7 -> {
                        // OUT [constant]
                        commandMode = false;
                        readOut();
                    }
                    case 8 -> {
                        // INP [constant]
                        commandMode = false;
                        readInp();
                    }
                    case 9 ->
                        // HALT
                            commandMode = false;
                    default -> {
                        next();
                    }
                }
            }
            if (!commandMode) {
                commandMode = true;
            }
        }
    }
    private void readPush(){
        // PUSH [constant]
        next();
        stack.push(retConstant(peek()));
    }
    private void readDup(){
        // DUP
        next();
        stack.push(stack.peek());
    }
    private void readPop(){
        // POP
        next();
        if(peek() == 0)
            stack.pop();
        else{
            // POP [constant]
            bytecode.getConstants().set(peek(), stack.pop());
        }
    }
    private void readFrame(){
        // FRAME [constant]
        next();
        bytecode.getConstants().set(peek(), stack.toArray());
    }
    private void readFlip(){
        // FLIP
        next();
        Stack<Object> nst = new Stack<Object>();
        for(int j = 0; j < stack.size(); j++){
            nst.push(stack.get(stack.size()-1-j));
        }
        stack = nst;
    }
    private void readSwap(){
        // SWAP
        next();
        Object obj1 = stack.get(stack.size()-1);
        Object obj2 = stack.get(stack.size()-2);
        stack.set(stack.size()-1, obj2);
        stack.set(stack.size()-2, obj1);
    }
    private void readOut(){
        // OUT
        next();
        // IO operation
        System.out.println(stack.get(stack.size()-1));
    }
    private void readInp(){
        // INP
        next();
        // IO operation
        Scanner in = new Scanner(System.in);
        System.out.print(stack.get(stack.size()-1));
        String text = in.nextLine();
        stack.push(text);
    }
    private byte peek(){
        return bytes.get(pos);
    }
    private byte peek(int relative){
        return bytes.get(pos+relative);
    }
    private Object retConstant(int i){
        return bytecode.getConstants().get(i);
    }
    private void next(){
        pos++;
    }
}
