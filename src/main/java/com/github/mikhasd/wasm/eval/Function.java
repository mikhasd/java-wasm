package com.github.mikhasd.wasm.eval;

import com.github.mikhasd.wasm.model.Local;
import com.github.mikhasd.wasm.model.TypeDefinition;
import com.github.mikhasd.wasm.model.TypeDefinition.FunctionType;

public class Function {
    private final FunctionType type;
    private final Local[] locals;
    private final byte[] opcodes;

    public Function(FunctionType type, Local[] locals, byte[] opcodes) {
        this.type = type;
        this.locals = locals;
        this.opcodes = opcodes;
    }

    public byte[] opcodes() {
        return opcodes;
    }

    public Local[] locals() {
        return locals;
    }

    public FunctionType type() {
        return type;
    }
}
