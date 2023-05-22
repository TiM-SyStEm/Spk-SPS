package com.timsystem.spk.asm;

import com.timsystem.spk.vm.Bytecode;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class RawBytecode {

    public static void saveToFile(Bytecode bytecode, String filename) {
        try (FileWriter writer = new FileWriter(filename, false)) {
            writer.append(save(bytecode));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Bytecode readFromFile(String filename) {
        try {
            return read(Files.readString(Path.of(filename)));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String save(Bytecode bytecode) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytecode.getBytecode()) {
            result.append(String.valueOf(b) + " ");
        }
        result.append("\n");
        for (int line : bytecode.getLines()) {
            result.append(line + " ");
        }
        result.append("\n");
        for (Object object : bytecode.getConstants()) {
            result.append(Base64.getEncoder().encodeToString(
                    stringifyExpression(object)
                            .getBytes()) + "\n");
        }
        return result.toString();
    }

    public static Bytecode read(String raw) {
        Bytecode result = new Bytecode();
        String[] lines = raw.split("\n");
        // Parsing opcodes
        String opcodes = lines[0];
        for (String opcode : opcodes.split(" ")) {
            if (opcode.equals("")) continue;
            result.getBytecode().add(Byte.parseByte(opcode));
        }
        // Parsing lines
        String lNumbers = lines[1];
        for (String line : lNumbers.split(" ")) {
            result.getLines().add(Integer.parseInt(line));
        }
        // Parsing constants
        for (int i = 2; i < lines.length; i++) {
            result.getConstants().add(stringToExpression(lines[i]));
        }
        return result;
    }

    public static Object stringToExpression(String expr) {
        expr = new String(Base64.getDecoder().decode(expr));
        String[] parts = expr.split(" ");
        if (parts[0].equals("bool"))
            return parts[1].equals("true");
        if (parts[0].equals("number"))
            return Double.parseDouble(parts[1]);
        return expr;
    }

    public static String stringifyExpression(Object object) {
        if (object instanceof Boolean) {
            return "bool " + object.toString();
        }
        if (object instanceof Number) {
            return "number " + ((Number) object).doubleValue();
        }
        return object.toString();
    }

}
