package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

public class Function implements CodeEmitter {

    private final IndexVector locals;
    private final byte[] encodedInstructions;

    public Function(IndexVector locals, byte[] encodedInstructions) {
        this.locals = locals;
        this.encodedInstructions = encodedInstructions;
    }

    @Override
    public void emitCode(CodeWriter output) {
        locals.emitCode(output);
        output.writeBytes(this.encodedInstructions);
    }

    public static Function create(IndexVector locals, byte[] encodedInstructions){
        return new Function(locals, encodedInstructions);
    }
}