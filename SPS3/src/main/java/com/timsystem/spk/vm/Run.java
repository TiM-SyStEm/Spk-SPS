package com.timsystem.spk.vm;

import com.timsystem.spk.compiler.lib.IntegerBytesConvert;
import com.timsystem.spk.vm.lib.Loop;
import com.timsystem.spk.vm.runtime.Natives;
import com.timsystem.spk.vm.runtime.SPKVMCore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static com.timsystem.spk.vm.Instructions.*;
import static java.util.stream.Collectors.toList;

public class Run {

    private int ip; // instruction [pointer
    private Bytecode bytecode;

    private HashMap<String, Object> globals, locals;
    private Stack<Object> stack;
    private Stack<Integer> callStack;
    private Stack<Loop> loopStack;
    private Stack<Loop> localsStack;
    private ArrayList<Stack<Object>> chunks;


    private boolean scope;
    public static Scanner SPKVM_INPUT = new Scanner(System.in);

    public Run(Bytecode bytecode) {
        this.bytecode = bytecode;
        this.ip = 0;

        this.globals = new HashMap<>();
        this.locals = new HashMap<>();
        this.stack = new Stack<>();
        this.callStack = new Stack<>();
        this.loopStack = new Stack<>();
        this.localsStack = new Stack<>();
        SPKVMCore.inject();
    }

    public void run() {
        execution_loop:
        while (true) {
            //System.out.println(ip);
            if (ip >= bytecode.getBytecode().size())
                break execution_loop;
            byte instruction = next();
            switch (instruction) {
                case OP_HALT -> {
                    break execution_loop;
                }
                case OP_PUSH -> push(readConstant());
                case OP_DUP -> push(peek());
                case OP_POP -> pop();
                case OP_FRAME -> readFrame();
                case OP_FLIP -> Collections.reverse(stack);
                case OP_SWAP -> readSwap();
                case OP_OUT -> readOut();
                case OP_INP -> readInput();
                case OP_SIGN -> readSign();
                case OP_BINARY -> readBinary();
                case OP_CHUNKS -> readChunks();
                case OP_CURCH -> readCurch();
                case OP_JMP -> readJmp();
                case OP_PUSH_SCOPE -> scope = true;
                case OP_POP_SCOPE -> scope = false;
                case OP_JE,
                        OP_JGN, OP_JLN, OP_JGE,
                        OP_JLE, OP_JG, OP_JNE,
                        OP_JEV, OP_JUE, OP_JL -> readUnifiedJump(instruction);
                case OP_JIT, OP_JIF -> readBooleanJump(instruction);
                case OP_CLR -> stack.clear();
                case OP_CALL -> readCall();
                case OP_RET -> readRet();
                case OP_FRGET -> readFrGet();
                case OP_LOOP -> readLoop();
                case OP_CALL_NATIVE -> readCallNative();
                case OP_CREATE_VAR -> readCreateVar();
                case OP_GET_VAR -> readGetVar();
                case OP_EDIT_VAR -> readEditVar();
                case OP_DEL_VAR -> readDelVar();
                default -> {
                    throw new SPKException("FailureInstructionSet", "unimplemented/undefined instruction " + instruction, line());
                }
            }
        }
    }

    public void readDelVar() {
        String name = (String) readConstant();
        stack.push((scope ? locals : globals).remove(name));
    }

    public void readGetVar() {
        String name = (String) readConstant();
        push((scope ? locals : globals).get(name));
    }

    public void readEditVar() {
        readCreateVar(false);
    }

    public void readCreateVar() {
        readCreateVar(true);
    }

    public void readCreateVar(boolean flipCondition) {
        String name = (String) readConstant();
        if (!scope) {
            if (flipCondition != globals.containsKey(name)) {
                globals.put(name, pop());
            } else throw new SPKException("VariableRedefinition", "trying to redefine global variable " + name, line());
        } else {
            if (flipCondition != locals.containsKey(name)) {
                locals.put(name, pop());
            } else throw new SPKException("VariableRedefinition", "trying to redefine local variable " + name, line());
        }
    }

    public void readCallNative() {
        Natives.Run((String) readConstant(), this);
    }

    public void readLoop() {
        int ind = readIndex();
        if(loopStack.size() == 0 || ind != loopStack.peek().getLabel()){
            loopStack.push(new Loop(ind, object2number(pop()).intValue()));
            ip = ind;
        }
        else{
            Loop peek = loopStack.peek();
            if(peek.getCounter() < peek.getCount()-1) {peek.setCounter(peek.getCounter()+1); ip = peek.getLabel();}
            else loopStack.pop();
        }
    }

    public void readFrGet() {
        Object get = ((Object[]) stack.pop())[object2number(readConstant()).intValue()];
        push(get);
    }

    public void readCall() {
        callStack.push(ip);
        ip = readIndex();
    }

    public void readRet() {
        ip = callStack.pop();
    }

    public void readBooleanJump(byte instruction) {
        Object predicate = pop();
        int index = readIndex();
        switch (instruction) {
            case OP_JIT -> {
                if ((boolean) predicate)
                    ip = index;
            }
            case OP_JIF -> {
                if (!(boolean) predicate)
                    ip = index;
            }
        }
    }

    public void readUnifiedJump(byte instruction) {
        Object b = pop();
        Object a = instruction != OP_JEV && instruction != OP_JUE ? pop() : null;
        Object result = null;
        double aNumber = 0.0;
        double bNumber = 0.0;
        int index = readIndex();
        if (a instanceof Number || b instanceof Number) {
            aNumber = object2number(a).doubleValue();
            bNumber = object2number(b).doubleValue();
        }
        switch ((char) next()) {
            case OP_JE -> result = a.equals(b);
            case OP_JNE -> result = !a.equals(b);
            case OP_JL -> result = aNumber < bNumber;
            case OP_JG -> result = aNumber > bNumber;
            case OP_JLE -> result = aNumber <= bNumber;
            case OP_JGE -> result = aNumber >= bNumber;
            case OP_JLN -> result = (aNumber < bNumber) || (aNumber != bNumber);
            case OP_JGN -> result = (aNumber > bNumber) || (aNumber != bNumber);
            case OP_JEV -> result = aNumber % 2.0 == 0.0;
            case OP_JUE -> result = aNumber % 2.0 != 0.0;
        }
        if ((boolean) result) {
            ip = index;
        }
    }

    public void readJmp() {
        ip = readIndex();
    }

    public void readCurch() {
        if (chunks != null) {
            stack = chunks.get(object2number(readConstant()).intValue());
        } else
            throw new SPKException("NoChunks", "there are no chunks left", line());
    }

    public void readChunks() {
        this.chunks = new ArrayList<>();
        Stack<Object> tempStack = new Stack<>();
        int chunkSize = object2number(pop()).intValue();
        for (int i = 0; i < stack.size(); i++) {
            if (i > chunkSize) {
                tempStack.push(stack.get(i));
            } else if (i == chunkSize) {
                tempStack.push(stack.get(i));
                chunks.add(tempStack);
            } else {
                tempStack.clear();
            }
        }
    }

    public void readBinary() {
        Object b = pop();
        Object a = pop();
        char operation = (char) next();
        if (a instanceof Boolean) {
            boolean aBoolean = (boolean) a;
            boolean bBoolean = (boolean) b;
            boolean result = false;
            switch (operation) {
                case '=' -> {
                    result = aBoolean == bBoolean;
                }
                case BinaryOperators.NOT_EQUAL -> {
                    result = aBoolean != bBoolean;
                }
                case '*' -> {
                    result = aBoolean && bBoolean;
                }
                case '+' -> {
                    result = aBoolean || bBoolean;
                }
                case '^' -> {
                    result = aBoolean ^ bBoolean;
                }
                default -> {
                    throw new SPKException("UnsupportedOperation", "unsupported operation '" + operation + "' for booleans!", line());
                }
            }
            push(result);
            return;
        }
        if (a instanceof String || b instanceof String) {
            String aString = (String) a;
            String bString = (String) b;
            String result = null;
            switch (operation) {
                case BinaryOperators.ADD -> {
                    result = aString + bString;
                }
                default -> {
                    throw new SPKException("UnsupportedOperation", "unsupported operation '" + operation + "' for strings!", line());
                }
            }
            push(result);
            return;
        }
        double aOperand = object2number(a).doubleValue();
        double bOperand = object2number(b).doubleValue();
        double result = 0;
        boolean asLogical = false;
        boolean logicalResult = false;
        switch (operation) {
            case BinaryOperators.ADD -> result = aOperand + bOperand;
            case BinaryOperators.SUB -> result = aOperand - bOperand;
            case BinaryOperators.MUL -> result = aOperand * bOperand;
            case BinaryOperators.DIV -> result = aOperand / bOperand;
            case BinaryOperators.POW -> result = Math.pow(aOperand, bOperand);
            case BinaryOperators.REM -> result = aOperand % bOperand;
            case BinaryOperators.LOWER -> {
                logicalResult = aOperand < bOperand;
                asLogical = true;
            }
            case BinaryOperators.GREATER -> {
                logicalResult = aOperand > bOperand;
                asLogical = true;
            }
            case BinaryOperators.EQUAL_LOWER -> {
                logicalResult = aOperand <= bOperand;
                asLogical = true;
            }
            case BinaryOperators.EQUAL_GREATER -> {
                logicalResult = aOperand >= bOperand;
                asLogical = true;
            }
            case BinaryOperators.NOT_EQUAL -> {
                logicalResult = aOperand != bOperand;
                asLogical = true;
            }
            case BinaryOperators.EQUAL -> {
                logicalResult = aOperand == bOperand;
                asLogical = true;
            }
        }
        push(asLogical ? logicalResult : result);
    }

    public void readSign() {
        Object operand = pop();
        if (operand instanceof Boolean) {
            stack.push(!((boolean) operand));
        } else {
            push(-(object2number(operand).doubleValue()));
        }
    }

    public void readInput() {
        System.out.print(pop());
        push(SPKVM_INPUT.nextLine());
    }

    public void readOut() {
        Object constant = pop();
        System.out.println(constant.equals("\0") ? "Null" : constant.toString());
    }

    public void readSwap() {
        Object b = pop();
        Object a = pop();
        push(b);
        push(a);
    }

    public void readFrame() {
        int size = object2number(readConstant()).intValue();
        Stack<Object> frame = new Stack<>();
        for (int i = 0; i < size; i++) {
            frame.push(pop());
        }
        push(frame);
    }

    public void push(Object obj) {
        stack.push(obj);
    }

    public Object pop() {
        return stack.pop();
    }

    public Object peek() {
        return stack.peek();
    }

    public Object readConstant() {
        return bytecode.getConstants().get(readIndex());
    }

    public int readIndex() {
        byte[] indexBytes = new byte[] {
                next(), next(), next(), next()
        };
        return IntegerBytesConvert.byteArr2Int(indexBytes);
    }

    public byte next() {
        return bytecode.getBytecode().get(ip++);
    }

    private Number object2number(Object expr) {
        if (!(expr instanceof Number)) {
            throw new SPKException("CastError", "cannot cast " + expr.getClass().getSimpleName() + " to a number", line());
        }
        return (Number) expr;
    }

    private int line() {
        return bytecode.getLines().get(ip);
    }

    private String instructionToString(byte instruction) {
        try {
            for (Field opcodeField : getStatics(Instructions.class)) {
                if ((byte) opcodeField.get(null) == instruction) {
                    return opcodeField.getName();
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return "null";
    }

    public static List<Field> getStatics(Class<?> clazz) {
        List<Field> result;

        result = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .collect(toList());

        return result;
    }
    public void scoping(){
        
    }
    public Stack<Object> stack() {
        return stack;
    }
}
