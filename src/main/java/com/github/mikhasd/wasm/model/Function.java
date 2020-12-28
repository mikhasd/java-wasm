package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.parse.BufferReader;

import java.util.Arrays;

public class Function {
    private final Local[] locals;
    private final BufferReader reader;

    public Function(Local[] locals, BufferReader reader) {
        this.locals = locals;
        this.reader = reader;
    }

    @Override
    public String toString() {
        return Arrays.toString(locals);
    }
}
