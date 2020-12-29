package com.github.mikhasd.wasm.model;

import java.util.Arrays;

public class DataSegment {
    private final int memoryIdx;
    private final byte[] expression;
    private final byte[] data;

    public DataSegment(int memoryIdx, byte[] expression, byte[] data) {
        this.memoryIdx = memoryIdx;
        this.expression = expression;
        this.data = data;
    }

    public int getMemoryIdx() {
        return memoryIdx;
    }

    public byte[] getExpression() {
        return expression;
    }

    public byte[] getData() {
        return data;
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
