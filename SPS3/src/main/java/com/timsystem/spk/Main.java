package com.timsystem.spk;

import java.util.ArrayList;
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
        Disassemble.disassemble("AST test", bc);
        String SPAS_CODE = "#build sps\n" +
                "PUSH 0\n" +
                "OUT\n" +
                "HALT\n" +
                ".data:\n" +
                "0 is \"Hello!\"\n";
        Bytecode bc = AssemblerCompiler.compile(SPAS_CODE);
        Run run = new Run(bc);
        run.run();
        Disassemble.disassemble("AST test", bc);*/
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
            System.out.println(RESET);
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
                        buffer.append(part);
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

                }
                default -> {
                    System.out.print("\\u001B[31m");
                    System.out.println("Command not found!");
                }
            }
        }
        private boolean isNumeric(char ch) {
            String str = Character.toString(ch);
            return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
        }
    }
}