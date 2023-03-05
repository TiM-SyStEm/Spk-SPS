package com.timsystem.spk.compiler.lib;

// https://gist.github.com/macrohuang/4146273

public class IntegerBytesConvert {
    private static final int[] BASE = new int[]{0xff000000, 0x00ff0000, 0x0000ff00, 0x000000ff};
    private static final int SIZE_OF_INT = 4;

    public static byte[] int2ByteArr(int a) {
        byte[] result = new byte[SIZE_OF_INT];
        for (int i = 0; i < SIZE_OF_INT; i++) {
            result[i] = (byte) ((a & BASE[i]) >> ((SIZE_OF_INT - 1 - i) * 8));
        }
        return result;
    }

    public static int byteArr2Int(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Required at least one byte, but receive null or empty.");
        }
        if (bytes[0] > 0x7f) {
            throw new NumberFormatException("An integer's biggest byte can't be more than 0x7f");
        }
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result |= bytes[i] << ((bytes.length - 1 - i) * 8) & BASE[BASE.length - bytes.length + i];
        }
        return result;
    }
}
