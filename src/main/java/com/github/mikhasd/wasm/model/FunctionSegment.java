package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.parse.BufferReader;

import java.util.Arrays;

public class FunctionSegment {
    private final Local[] locals;
    private final byte[] opcodes;

    public FunctionSegment(Local[] locals, byte[] opcodes) {
        this.locals = locals;
        this.opcodes = opcodes;
    }

    public Local[] getLocals() {
        return locals;
    }

    public byte[] opcodes() {
        return this.opcodes;
    }

    @Override
    public String toString() {
        return Arrays.toString(locals);
    }
}
