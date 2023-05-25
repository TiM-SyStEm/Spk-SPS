package com.timsystem.spk.compiler.lib;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AddProcessor {

    public static String process(String script) {
        try {
            String[] lines = script.split("\n");
            StringBuilder result = new StringBuilder();
            for (String line : lines) {
                line = line.trim();
                String[] parts = line.split(" ");
                if (parts[0].equals("Add")) {
                    String[] pathParts = Arrays.copyOfRange(parts, 1, parts.length);
                    String path = String.join("/", pathParts) + ".spk";
                    result.append(process(Files.readString(Path.of(path)))).append("\n\n");
                } else {
                    result.append(line).append("\n");
                }
            }
            return result.toString();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
