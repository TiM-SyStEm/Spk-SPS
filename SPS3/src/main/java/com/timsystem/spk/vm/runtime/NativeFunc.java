package com.timsystem.spk.vm.runtime;

import com.timsystem.spk.vm.Run;

import java.util.Stack;

public interface NativeFunc {
    void func(Run runner);
}
