package com.timsystem.spk;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.timsystem.spk.asm.AssemblerCompiler;
import com.timsystem.spk.asm.RawBytecode;
import com.timsystem.spk.vm.Bytecode;
import com.timsystem.spk.vm.Disassemble;
import com.timsystem.spk.vm.Run;
import org.fusesource.jansi.AnsiConsole;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static String getVer() {
        return "SPS3";
    }
    public static final String RESET = "\u001b[0;0;10m";
    public static void main(String[] args) {
        /*Bytecode bc = new Bytecode();
        Parser parser = new Parser(new Lexer("var abc = 2 + 2 var abcd = abc + 1"));
        AST ast = parser.parse();
        ast.compile(bc);
        */
        AnsiConsole.systemInstall();
        CommandLine cmd = new CommandLine();
        cmd.execute();
    }
    public static class CommandLine{
        private String command;
        private String arg;
        private ArrayList<String> flags;
        public void execute(){
            start();
            while (true){
                looping();
            }
        }
        private void start(){
            System.out.println("Special Key " + getVer() + "\n");
        }
        private void looping(){
            System.out.print(RESET);
            command = "";
            arg = "";
            flags = new ArrayList<>();
            StringBuilder buffer = new StringBuilder();
            boolean buildStr = false;

            Scanner in = new Scanner(System.in);
            System.out.print("$> ");
            String text = in.nextLine();
            String[] parts = text.split(" ");
            for(String part : parts){
                if(part.charAt(0) == '-'){
                    flags.add(part.replace("-", ""));
                }
                else if(part.charAt(0) == '"'){
                    buffer.append(part.replace("\"", "") + " ");
                    buildStr = true;
                }
                else if(Objects.equals(command, "")){
                    command = part;
                }
                else if(buildStr){
                    if(part.charAt(part.length()-1) != '"'){
                        buffer.append(part + " ");
                    }
                    else{
                        buffer.append(part.replace("\"", ""));
                        arg = buffer.toString();
                        buildStr = false;
                    }
                }
                else{
                    arg = part;
                }
            }
            // COMMAND CHECK
            switch (command){
                case "build" -> {
                    if(flags.contains("asm")){

                    }
                    else{

                    }
                }
                case "run" -> {
                    if(arg.charAt(arg.length()-1) == 'k' &&
                            arg.charAt(arg.length()-2) == 'p' &&
                            arg.charAt(arg.length()-3) == 's' &&
                            arg.charAt(arg.length()-4) == '.'){

                    }
                    else{

                    }
                }
                case "spas" -> {
                    if(flags.contains("comp")){
                        try{
                            Bytecode bytecode = AssemblerCompiler.compileFile(arg);
                            RawBytecode.saveToFile(bytecode, SPASfile2SPKVM(arg));
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    else if(flags.contains("disasm")){
                        Bytecode bytecode = RawBytecode.readFromFile(arg);
                        Disassemble.disassemble("Disasm SPKVM bytecode:", bytecode);
                    }
                }
                case "spkvm" -> {
                    Bytecode bytecode = RawBytecode.readFromFile(arg);
                    Run run = new Run(bytecode);
                    run.run();
                }
                default -> {
                    System.out.print("\\u001B[31m");
                    System.out.println("Command not found!");
                }
            }
        }
        private String SPASfile2SPKVM(String spas){
            StringBuilder spkvm = new StringBuilder(spas);
            spkvm.setCharAt(spas.length()-1, 'v');
            spkvm.setCharAt(spas.length()-2, 'k');
            spkvm.setCharAt(spas.length()-3, 'p');
            spkvm.setCharAt(spas.length()-4, 's');
            spkvm.append("m");
            return spkvm.toString();
        }
        private boolean isNumeric(char ch) {
            String str = Character.toString(ch);
            return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
        }
    }
}