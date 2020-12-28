package com.github.mikhasd.wasm.model;

import java.util.Arrays;

public class Data {
    private final int memoryIdx;
    private final byte[] expression;
    private final byte[] data;

    public Data(int memoryIdx, byte[] expression, byte[] data) {
        this.memoryIdx = memoryIdx;
        this.expression = expression;
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "memoryIdx=" + memoryIdx +
                ", expression=" + Arrays.toString(expression) +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
