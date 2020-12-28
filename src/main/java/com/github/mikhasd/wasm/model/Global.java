package com.github.mikhasd.wasm.model;

import java.util.Arrays;

public class Global {

    private final Type type;
    private final byte mutability;
    private final byte[] expressionBuffer;

    public Global(Type type, byte mutability, byte[] expressionBuffer) {
        if (mutability != 0x00 && mutability != 0x01) {
            throw new IllegalArgumentException(String.format("Invalid mutability flag: %02x", mutability));
        }
        this.type = type;
        this.mutability = mutability;
        this.expressionBuffer = expressionBuffer;
    }

    @Override
    public String toString() {
        return "{" +
                "type=" + type +
                ", mutability=" + mutability +
                ", expressionBuffer=" + Arrays.toString(expressionBuffer) +
                '}';
    }
}
