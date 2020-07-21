package com.github.mikhasd.wasm.gen.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

public class Data implements CodeEmitter {
    private final int memoryIdx;
    private final byte[] expression;
    private final byte[] data;

    public Data(int memoryIdx, byte[] expression, byte[] data) {
        this.memoryIdx = memoryIdx;
        this.expression = expression;
        this.data = data;
    }

    @Override
    public void emitCode(CodeWriter output) {
        output.writeInteger(this.memoryIdx);
        output.writeBytes(this.expression);
        output.writeBytes(this.data);
    }
}
