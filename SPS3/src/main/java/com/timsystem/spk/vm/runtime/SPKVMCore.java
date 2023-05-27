package com.timsystem.spk.vm.runtime;

import com.timsystem.spk.vm.Run;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SPKVMCore {
    public static void inject(){
        Natives.Add("FileRead", run -> {
            try {
                String text = Files.readString(Path.of((String) run.pop()));
                run.stack().push(text);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        Natives.Add("FileWrite", run -> {
            String text = (String) run.stack().pop();
            try (FileWriter writer = new FileWriter((String)run.pop(), false)) {
                writer.append(text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
