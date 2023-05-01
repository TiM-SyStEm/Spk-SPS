package com.timsystem.spk.vm.runtime;

import com.timsystem.spk.vm.Run;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SPKVMCore {
    public SPKVMCore(){
        inject();
    }
    private void inject(){
        Natives.Add("FileRead", stack -> {
            try {
                String text = Files.readString(Path.of((String)stack.peek()));
                Run.stack.push(text);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        Natives.Add("FileWrite", stack -> {
            String text = (String)stack.peek();
            try (FileWriter writer = new FileWriter((String)stack.get(stack.size()-2), false)) {
                writer.append(text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
