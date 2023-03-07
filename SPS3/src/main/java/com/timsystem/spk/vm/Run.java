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
    private int chunkSize = 0;
    private int line;
    private ArrayList<Stack<Object>> chunks;

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
                    case Instructions.OP_PUSH -> {
                        // PUSH
                        commandMode = false;
                        readPush();
                    }
                    case Instructions.OP_DUP -> {
                        // DUP
                        commandMode = false;
                        readDup();
                    }
                    case Instructions.OP_POP -> {
                        // POP
                        commandMode = false;
                        readPop();
                    }
                    case Instructions.OP_FRAME -> {
                        // FRAME
                        commandMode = false;
                        readFrame();
                    }
                    case Instructions.OP_FLIP -> {
                        // FLIP
                        commandMode = false;
                        readFlip();
                    }
                    case Instructions.OP_SWAP -> {
                        // SWAP (swap end and pre-end)
                        commandMode = false;
                        readSwap();
                    }
                    case Instructions.OP_OUT -> {
                        // OUT
                        commandMode = false;
                        readOut();
                    }
                    case Instructions.OP_INP -> {
                        // INP
                        commandMode = false;
                        readInp();
                    }
                    case Instructions.OP_HALT ->
                        // HALT
                        commandMode = false;
                    case Instructions.OP_SIGN ->
                        // NEG
                        readSign();
                    case Instructions.OP_BINARY ->
                        // NEG
                        readBinary();
                    case Instructions.OP_CHUNKS ->
                        // CHUNKS
                        readChunks();
                    case Instructions.OP_CURCH->
                        // CURCH
                        // choose current chunk
                        readCurch();
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

    private void readCurch() {
        // CURCH [constant]
        next();
        if(chunks != null){
            stack = chunks.get((int)bytecode.getConstants().get(peek()));
        }
        else{
            throw new SPKException("NoChunks", "there are no chunks", line);
        }
    }
    private void readChunks() {
        // CHUNKS
        next();
        chunks = new ArrayList<>();
        // Convert std stack to chunkising stack
        Stack<Object> st = new Stack<>();
        for(int i = 0; i < stack.size()-1; i++){
            if(i > chunkSize){
                st.push(stack.get(i));
            }
            else if(i == chunkSize){
                st.push(stack.get(i));
                chunks.add(st);
            }
            else{
                st = new Stack<>();
            }
        }
        chunkSize = (int)stack.peek();
    }

    private void readBinary() {
        // BINARY [char]
        next();
        if((char)peek() == '+'){
            // SUM
            int o1 = (int)stack.peek();
            int o2 = (int)stack.get(stack.size()-2);
            int r = o1+o2;
            if(checkChunk(stack.size()+1))
                stack.push(r);
            else{
                throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
            }
        }
        else if((char)peek() == '-'){
            // SUB
            int o1 = (int)stack.peek();
            int o2 = (int)stack.get(stack.size()-2);
            int r = o1-o2;
            if(checkChunk(stack.size()+1))
                stack.push(r);
            else{
                throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
            }
        }
        else if((char)peek() == '*'){
            // MUL
            int o1 = (int)stack.peek();
            int o2 = (int)stack.get(stack.size()-2);
            int r = o1*o2;
            if(checkChunk(stack.size()+1))
                stack.push(r);
            else{
                throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
            }
        }
        else if((char)peek() == '/'){
            // DIVIDE
            int o1 = (int)stack.peek();
            int o2 = (int)stack.get(stack.size()-2);
            float r = (float)o1 / o2;
            if(checkChunk(stack.size()+1))
                stack.push(r);
            else{
                throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
            }
        }
        else if((char)peek() == '%'){
            // MOD
            int o1 = (int)stack.peek();
            int o2 = (int)stack.get(stack.size()-2);
            int r = o1%o2;
            if(checkChunk(stack.size()+1))
                stack.push(r);
            else{
                throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
            }
        }
        else if((char)peek() == '#'){
            // DIV
            int o1 = (int)stack.peek();
            int o2 = (int)stack.get(stack.size()-2);
            int r = o1/o2;
            if(checkChunk(stack.size()+1))
                stack.push(r);
            else{
                throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
            }
        }
    }

    private void readSign() {
        // POSITIVE
        // change the sigh of the last value of stack
        next();
        stack.push(-(int)stack.peek());
    }

    private void readPush(){
        // PUSH [constant]
        next();
        if(checkChunk(stack.size()+1))
            stack.push(retConstant(peek()));
        else{
            throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
        }
    }
    private void readDup(){
        // DUP
        next();
        if(checkChunk(stack.size()+1))
            stack.push(stack.peek());
        else{
            throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
        }
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
        //bytecode.getConstants().set(peek(), stack.toArray());
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
        if(checkChunk(stack.size()+1))
            stack.push(text);
        else{
            throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
        }
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
    private boolean checkChunk(int i){
        if(chunkSize != 0){
            return i <= chunkSize;
        }
        else return true;
    }
}