package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

public class Global implements CodeEmitter {

    private final byte type;
    private final byte mutability;
    private final byte[] expressionBuffer;

    public Global(byte type, byte mutability, byte[] expressionBuffer) {
        if (mutability != 0x00 && mutability != 0x01) {
            throw new IllegalArgumentException(String.format("Invalid mutability flag: %02x", mutability));
        }
        this.type = type;
        this.mutability = mutability;
        this.expressionBuffer = expressionBuffer;
    }

    @Override
    public void emitCode(CodeWriter output) {
        output.writeByte(this.type);
        output.writeByte(this.mutability);
        output.writeBytes(this.expressionBuffer);
    }
}
