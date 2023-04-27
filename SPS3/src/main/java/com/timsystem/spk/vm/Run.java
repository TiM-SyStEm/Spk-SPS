package com.timsystem.spk.vm;

import com.timsystem.spk.compiler.lib.IntegerBytesConvert;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Run {
    private Bytecode bytecode;
    private Stack<Object> stack;
    private byte current;
    private boolean isWork;
    private int pos = 0;
    private ArrayList<Byte> bytes;
    private int chunkSize = 0;
    private int line;
    private ArrayList<Stack<Object>> chunks;
    private int loopingCount = 0;
    private int loopCounter = 0;
    private int saveAddress = -1;
    private boolean procedureFlag;

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
        isWork = true;
        while(true){
            if(!isWork) break;
            else{
                current = bytes.get(pos);
                switch (current) {
                    case Instructions.OP_PUSH -> {
                        // PUSH
                        readPush();
                    }
                    case Instructions.OP_DUP -> {
                        // DUP
                        readDup();
                    }
                    case Instructions.OP_POP -> {
                        // POP
                        readPop();
                    }
                    case Instructions.OP_FRAME -> {
                        // FRAME
                        readFrame();
                    }
                    case Instructions.OP_FLIP -> {
                        // FLIP
                        readFlip();
                    }
                    case Instructions.OP_SWAP -> {
                        // SWAP (swap end and pre-end)
                        readSwap();
                    }
                    case Instructions.OP_OUT -> {
                        // OUT
                        readOut();
                    }
                    case Instructions.OP_INP -> {
                        // INP
                        readInp();
                    }
                    case Instructions.OP_HALT -> {
                        // HALT
                        isWork = false;
                    }
                    case Instructions.OP_SIGN ->
                        readSign();
                    case Instructions.OP_BINARY ->
                        readBinary();
                    case Instructions.OP_CHUNKS ->
                        // CHUNKS
                        readChunks();
                    case Instructions.OP_CURCH->
                        // CURCH
                        // choose current chunk
                        readCurch();
                    case Instructions.OP_CHUSZ->
                        // CUSZ
                        // choose chunk size
                        readChusz();
                    case Instructions.OP_JMP->
                        // JMP
                        // jump to label
                        readJump();
                    case Instructions.OP_JE->
                        // JE
                        // jump to label if end equal pre-end
                        readJE();
                    case Instructions.OP_JNE->
                        // JNE
                        // jump to label if end not equal pre-end
                        readJNE();
                    case Instructions.OP_JL->
                        // JL
                        // jump to label if end less than pre-end
                        readJL();
                    case Instructions.OP_JG->
                        // JG
                        // jump to label if end greater than pre-end
                        readJG();
                    case Instructions.OP_JLE->
                        // JLE
                        // jump to label if end greater than pre-end or equal
                        readJLE();
                    case Instructions.OP_JGE->
                        // JGE
                        // jump to label if end less than pre-end or equal
                        readJGE();
                    case Instructions.OP_JLN->
                        // JLN
                        // end less and not greater and not equal pre-end
                        readJLN();
                    case Instructions.OP_JGN->
                        // JGN
                        // end greater and not less and not equal pre-end
                        readJGN();
                    case Instructions.OP_JEV->
                        // JEV
                        // even number
                        readJEV();
                    case Instructions.OP_JUE->
                        // JUE
                        // uneven number
                        readJUE();
                    case Instructions.OP_LOOP->
                        // LOOP
                        // from end of stack times and label
                        readLOOP();
                    case Instructions.OP_CALL->
                        // CALL
                        // from address
                        readCALL();
                    case Instructions.OP_RET->
                        // RET
                        // return from procedure
                        readRET();
                    default -> {
                        next();
                    }
                }
                /*for(Object o : stack){
                    System.out.println(o);
                    System.out.println("===========");
                }*/
            }
        }
    }
    private void readCALL(){
        next();
        if(saveAddress != pos){
            saveAddress = pos;
            procedureFlag = true;
            pos = peekIntOf4bites();
        }
        else saveAddress = -1;
    }
    private void readRET(){
        if(procedureFlag){
            pos = saveAddress;
            procedureFlag = false;
        }
    }
    private void readLOOP(){
        // LOOP [constant]
        next();

        if(loopingCount == 0 && loopCounter == 0){
            loopingCount = ((Number)stack.peek()).intValue();
        }
        loopCounter++;
        if(loopingCount > loopCounter){
            pos = peekIntOf4bites();
        }
        else if(loopingCount-1 == loopCounter){
            loopingCount = 0;
            loopCounter = 0;
        }
    }
    private void readJump(){
        // JMP [constant]
        next();
        pos = peekIntOf4bites();
        //System.out.println(pos);
    }
    private void readJE(){
        // JE [constant]
        next();
        if(stack.peek().equals(stack.get(stack.size()-2))){
            pos = peekIntOf4bites();
        }
    }
    private void readJNE(){
        // JNE [constant]
        next();
        if(!stack.peek().equals(stack.get(stack.size()-2))){
            pos = peekIntOf4bites();
        }
    }
    private void readJL(){
        // JL [constant]
        next();
        if((float)stack.peek() < (float)stack.get(stack.size()-2)){
            pos = peekIntOf4bites();
        }
    }
    private void readJG(){
        // JG [constant]
        next();
        if((float)stack.peek() > (float)stack.get(stack.size()-2)){
            pos = peekIntOf4bites();
        }
    }
    private void readJLE(){
        // JLE [constant]
        next();
        if((float)stack.peek() < (float)stack.get(stack.size()-2) || ((float)stack.peek()) == (float)stack.get(stack.size()-2)){
            pos = peekIntOf4bites();
        }
    }
    private void readJGE(){
        // JGE [constant]
        next();
        if((float)stack.peek() > (float)stack.get(stack.size()-2) || ((float)stack.peek()) == (float)stack.get(stack.size()-2)){
            pos = peekIntOf4bites();
        }
    }
    private void readJLN(){
        // JLN [constant]
        next();
        if((float)stack.peek() < (float)stack.get(stack.size()-2) && ((float)stack.peek()) != (float)stack.get(stack.size()-2)){
            pos = peekIntOf4bites();
        }
    }
    private void readJGN(){
        // JLN [constant]
        next();
        if((float)stack.peek() > (float)stack.get(stack.size()-2) && ((float)stack.peek()) != (float)stack.get(stack.size()-2)){
            pos = peekIntOf4bites();
        }
    }
    private void readJEV(){
        // JEV [constant]
        next();
        if((float)stack.peek()%2 == 0){
            pos = peekIntOf4bites();
        }
    }
    private void readJUE(){
        // JUE [constant]
        next();
        if((float)stack.peek()%2 != 0){
            pos = peekIntOf4bites();
        }
    }
    private void readCurch(){
        // CURCH [constant]
        next();
        if(chunks != null){
            stack = chunks.get((int)retConstant(peek()));
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
    private void readChusz(){
        next();
        chunkSize = (int)retConstant(peek());
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
        // change the sign of the last value of stack
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
    private Object retConstant(byte i){
        byte[] indexBytes = new byte[] {
                i,
                peek(1),
                peek(2),
                peek(3)
        };
        int _int = IntegerBytesConvert.byteArr2Int(indexBytes);
        next();
        next();
        next();
        next();
        return bytecode.getConstants().get(_int);
    }
    private int peekIntOf4bites(){
        byte[] indexBytes = new byte[] {
                peek(),
                peek(1),
                peek(2),
                peek(3)
        };
        int _int = IntegerBytesConvert.byteArr2Int(indexBytes);
        next();
        next();
        next();
        next();
        return _int;
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