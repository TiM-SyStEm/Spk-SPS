package com.timsystem.spk.vm;

import com.timsystem.spk.compiler.lib.IntegerBytesConvert;
import com.timsystem.spk.vm.lib.JavaTuple;
import com.timsystem.spk.vm.runtime.Natives;
import com.timsystem.spk.vm.runtime.SPKVMCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Run {
    private Bytecode bytecode;
    public static Stack<Object> stack;
    private byte current;
    private boolean isWork;
    private int pos = 0;
    private ArrayList<Byte> bytes;
    private int chunkSize = 0;
    private int line;
    private ArrayList<Stack<Object>> chunks;
    private int loopingCount = 0;
    private int loopCounter = 0;
    private Stack<Integer> callsStack;
    private HashMap<String,Object> globals;
    private HashMap<String,Object> locals;
    private boolean scope;

    public Run(Bytecode bytecode){
        this.bytecode = bytecode;
    }
    public void run(){
        // initialize bytes
        bytes = bytecode.getBytecode();
        // initialize stack
        stack = new Stack<>();
        callsStack = new Stack<>();
        // initialize global varibles
        globals = new HashMap<>();
        // initialize locals
        locals = new HashMap<>();
        // initialize SPKVMCore functions
        new SPKVMCore();
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
                    case Instructions.OP_JIT->
                        // JIT
                        // jump if end == true
                        readJIT();
                    case Instructions.OP_JIF->
                        // JIF
                        // jump if end == false
                        readJIF();
                    case Instructions.OP_FRGET->
                        // FRGET
                        // get from frame by index and push value
                        readFRGET();
                    case Instructions.OP_CLR->
                        // CLR
                        // clear stack
                        readCLR();
                    case Instructions.OP_CALL_NATIVE->
                        // CALL_NATIVE
                        // call the native function from Java
                        readCALLNATIVE();
                    case Instructions.OP_CREATE_VAR->
                        // CREATE_VAR
                        // create variable
                        readCREATE_VAR();
                    case Instructions.OP_GET_VAR->
                        // GET_VAR
                        // push value of variable
                        readGET_VAR();
                    case Instructions.OP_PUSH_SCOPE->
                        // PUSH_SCOPE
                        scope = true;
                    case Instructions.OP_POP_SCOPE->
                        // PUSH_SCOPE
                        scope = false;
                    case Instructions.OP_EDIT_VAR->
                        // EDIT_VAR
                        readEDIT_VAR();
                    case Instructions.OP_DEL_VAR->
                        // DEL_VAR
                        readDEL_VAR();
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
    private void readDEL_VAR(){
        next();
        String name = (String)retConstant(peek());
        if(!scope)
            stack.push(globals.remove(name));
        else stack.push(locals.remove(name));
    }
    private void readEDIT_VAR(){
        next();
        String name = (String)retConstant(peek());
        if(!scope) {
            Object test = globals.get(name);
            if(test != null) {
                globals.put((String)name, stack.peek());
            }
            else {
                //ERROR
            }
        }
        else {
            Object test = locals.get(name);
            if(test != null) {
                locals.put((String)name, stack.peek());
            }
            else {
                //ERROR
            }
        }
        stack.pop();
    }
    private void readGET_VAR(){
        next();
        String name = (String)retConstant(peek());
        if(!scope)
            stack.push(globals.get(name));
        else stack.push(locals.get(name));
    }
    private void readCREATE_VAR(){
        next();
        String name = (String)retConstant(peek());
        if(!scope) {
            Object test = globals.get(name);
            if(test == null) {
                globals.put((String)name, stack.peek());
            }
            else {
                //ERROR
            }
        }
        else {
            Object test = locals.get(name);
            if(test == null) {
                locals.put((String)name, stack.peek());
            }
            else {
                //ERROR
            }
        }
        stack.pop();
    }
    private void readCALLNATIVE(){
        next();
        String name = (String)retConstant(peek());
        Natives.Run(name);
    }
    private void readJIT(){
        // JIT [addr]
        next();
        if((boolean) stack.peek()){
            pos = peekIntOf4bites();
        }
        stack.pop();
    }
    private void readJIF(){
        // JIF [addr]
        next();
        if(!((boolean) stack.peek())){
            pos = peekIntOf4bites();
        }
        stack.pop();
    }
    private void readCALL(){
        next();
        callsStack.push(pos);
        pos = peekIntOf4bites();
    }
    private void readRET(){
        pos = callsStack.pop();
    }
    private void readLOOP(){
        // LOOP [constant]
        next();

        if(loopingCount == 0 && loopCounter == 0){
            if(stack.peek() instanceof Boolean && (boolean) stack.peek()){
                loopingCount = -1;
            }
            else{
                loopingCount = ((Number)stack.peek()).intValue();
                stack.pop();
            }
        }
        if(loopingCount != -1){
            loopCounter++;
            if(loopingCount > loopCounter){
                pos = peekIntOf4bites();
            }
            else if(loopingCount-1 == loopCounter){
                loopingCount = 0;
                loopCounter = 0;
            }
        }
        else{
            pos = peekIntOf4bites();
        }
    }
    private void readJump(){
        // JMP [addr]
        next();
        pos = peekIntOf4bites();
        //System.out.println(pos);
    }
    private void readJE(){
        // JE [addr]
        next();
        if(stack.peek().equals(stack.get(stack.size()-2))){
            pos = peekIntOf4bites();
        }
        stack.pop();
        stack.pop();
    }
    private void readJNE(){
        // JNE [addr]
        next();
        if(!stack.peek().equals(stack.get(stack.size()-2))){
            pos = peekIntOf4bites();
        }
        stack.pop();
        stack.pop();
    }
    private void readJL(){
        // JL [addr]
        next();
        if((float)stack.peek() < (float)stack.get(stack.size()-2)){
            pos = peekIntOf4bites();
        }
        stack.pop();
        stack.pop();
    }
    private void readJG(){
        // JG [addr]
        next();
        if((float)stack.peek() > (float)stack.get(stack.size()-2)){
            pos = peekIntOf4bites();
        }
        stack.pop();
        stack.pop();
    }
    private void readJLE(){
        // JLE [addr]
        next();
        if((float)stack.peek() < (float)stack.get(stack.size()-2) || ((float)stack.peek()) == (float)stack.get(stack.size()-2)){
            pos = peekIntOf4bites();
        }
        stack.pop();
        stack.pop();
    }
    private void readJGE(){
        // JGE [addr]
        next();
        if((float)stack.peek() > (float)stack.get(stack.size()-2) || ((float)stack.peek()) == (float)stack.get(stack.size()-2)){
            pos = peekIntOf4bites();
        }
        stack.pop();
        stack.pop();
    }
    private void readJLN(){
        // JLN [addr]
        next();
        if((float)stack.peek() < (float)stack.get(stack.size()-2) && ((float)stack.peek()) != (float)stack.get(stack.size()-2)){
            pos = peekIntOf4bites();
        }
        stack.pop();
        stack.pop();
    }
    private void readJGN(){
        // JLN [addr]
        next();
        if((float)stack.peek() > (float)stack.get(stack.size()-2) && ((float)stack.peek()) != (float)stack.get(stack.size()-2)){
            pos = peekIntOf4bites();
        }
        stack.pop();
        stack.pop();
    }
    private void readJEV(){
        // JEV [addr]
        next();
        if((float)stack.peek()%2 == 0){
            pos = peekIntOf4bites();
        }
        stack.pop();
        stack.pop();
    }
    private void readJUE(){
        // JUE [addr]
        next();
        if((float)stack.peek()%2 != 0){
            pos = peekIntOf4bites();
        }
        stack.pop();
        stack.pop();
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
        chunkSize = double2int(stack.peek());
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
    }
    private void readBinary() {
        // BINARY [char]
        next();
        if(stack.peek() instanceof Boolean){
            if(stack.get(stack.size()-2) instanceof Boolean){
                if((char)peek() == '*'){
                    // BOOLEAN AND
                    boolean r = (boolean)stack.peek() && (boolean)stack.get(stack.size()-2);
                    stack.pop();
                    stack.pop();
                    if(checkChunk(stack.size()+1))
                        stack.push(r);
                    else{
                        throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
                    }
                }
                else if((char)peek() == '+'){
                    // BOOLEAN OR
                    boolean r = (boolean)stack.peek() || (boolean)stack.get(stack.size()-2);
                    stack.pop();
                    stack.pop();
                    if(checkChunk(stack.size()+1))
                        stack.push(r);
                    else{
                        throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
                    }
                }
                else if((char)peek() == '^'){
                    // BOOLEAN XOR
                    boolean r = (boolean)stack.peek() ^ (boolean)stack.get(stack.size()-2);
                    stack.pop();
                    stack.pop();
                    if(checkChunk(stack.size()+1))
                        stack.push(r);
                    else{
                        throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
                    }
                }
            }
            else{
                //ERROR
            }
        }
        else if((char)peek() == '^'){
            double o1 = (double)stack.peek();
            double o2 = (double)stack.get(stack.size()-2);
            double r = Math.pow(o1, o2);
            stack.pop();
            stack.pop();
            if(checkChunk(stack.size()+1))
                stack.push(r);
            else{
                throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
            }
        }
        else if((char)peek() == '+'){
            // SUM
            double o1 = (double)stack.peek();
            double o2 = (double)stack.get(stack.size()-2);
            double r = o2+o1;
            stack.pop();
            stack.pop();
            if(checkChunk(stack.size()+1))
                stack.push(r);
            else{
                throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
            }
        }
        else if((char)peek() == '-'){
            // SUB
            double o1 = (double)stack.peek();
            double o2 = (double)stack.get(stack.size()-2);
            double r = o2-o1;
            stack.pop();
            stack.pop();
            if(checkChunk(stack.size()+1))
                stack.push(r);
            else{
                throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
            }
        }
        else if((char)peek() == '*'){
            // MUL
            double o1 = (double)stack.peek();
            double o2 = (double)stack.get(stack.size()-2);
            double r = o2*o1;
            stack.pop();
            stack.pop();
            if(checkChunk(stack.size()+1))
                stack.push(r);
            else{
                throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
            }
        }
        else if((char)peek() == '/'){
            // DIVIDE
            double o1 = (double)stack.peek();
            double o2 = (double)stack.get(stack.size()-2);
            double r = o2 / o1;
            stack.pop();
            stack.pop();
            if(checkChunk(stack.size()+1))
                stack.push(r);
            else{
                throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
            }
        }
        else if((char)peek() == '%'){
            // MOD
            double o1 = (double)stack.peek();
            double o2 = (double)stack.get(stack.size()-2);
            double r = o2 % o1;
            stack.pop();
            stack.pop();
            if(checkChunk(stack.size()+1))
                stack.push(r);
            else{
                throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
            }
        }
        next();
    }

    private void readSign() {
        // POSITIVE
        // change the sign of the last value of stack
        next();
        if(stack.peek() instanceof Boolean){
            boolean r = !(boolean)stack.peek();
            stack.pop();
            stack.push(r);
        }
        else{
            int num = double2int(stack.peek());
            stack.pop();
            stack.push(-num);
        }
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
        int size = double2int(retConstant(peek()));
        Stack<Object> nst = new Stack<>();
        for(int i = 0; i < size; i++){
            nst.push(stack.get(stack.size()-1-i));
        }
        stack.push(nst);
        for(int i = 0; i < size; i++) {
            stack.pop();
        }
    }
    private void readFRGET(){
        // FRGET [constant]
        Object get = ((Object[])stack.peek())[(int)retConstant(peek())];
        stack.push(get);
        stack.pop();
    }
    private void readCLR(){
        // CLR
        stack.clear();
    }
    private void readFlip(){
        // FLIP
        next();
        Stack<Object> nst = new Stack<>();
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
        if(stack.get(stack.size()-1) == "\0"){
            System.out.println("Null");
        }
        else System.out.println(stack.get(stack.size()-1));
        stack.pop();
    }
    private void readInp(){
        // INP
        next();
        // IO operation
        Scanner in = new Scanner(System.in);
        System.out.print(stack.get(stack.size()-1));
        String text = in.nextLine();
        stack.pop();
        if(checkChunk(stack.size()+1))
            stack.push(text);
        else{
            throw new SPKException("ChunkOverflow", "max size of chunk is '"+chunkSize+"'", line);
        }
    }
    private int double2int(Object o){
        if(o instanceof Integer){
            return (int)o;
        }
        Double dd = (Double)o;
        return dd.intValue();
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