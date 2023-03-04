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
            if(current == 1 && commandMode){
                // PUSH
                commandMode = false;
                readPush();
            }
            else if(current == 2 && commandMode){
                // DUP
                commandMode = false;
                readDup();
            }
            else if(current == 3 && commandMode){
                // POP
                commandMode = false;
                readPop();
            }
            else if(current == 4 && commandMode){
                // FRAME
                commandMode = false;
                readFrame();
            }
            else if(current == 5 && commandMode){
                // FLIP
                commandMode = false;
                readFlip();
            }
            else if(current == 6 && commandMode){
                // SWAP (swap end and pre-end)
                commandMode = false;
                readSwap();
            }
            else if(current == 7 && commandMode){
                // OUT [constant]
                commandMode = false;
                readOut();
            }
            else if(current == 8 && commandMode){
                // INP [constant]
                commandMode = false;
                readInp();
            }
            else if(current == 9 && commandMode){
                // HALT
                commandMode = false;
                break;
            }
            else{
                if(current == 0 && !commandMode){
                    commandMode = true;
                }
                next();
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
        // OUT [constant]
        next();
        // IO operation
        System.out.println(bytecode.getConstants().get(peek()).toString());
    }
    private void readInp(){
        // INP  [constant]
        next();
        // IO operation
        Scanner in = new Scanner(System.in);
        System.out.print(bytecode.getConstants().get(peek()).toString());
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
