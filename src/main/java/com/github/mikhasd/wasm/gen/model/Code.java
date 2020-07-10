package com.github.mikhasd.wasm.gen.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;
import com.github.mikhasd.wasm.gen.OutputStreamCodeWriter;

import java.io.ByteArrayOutputStream;

public class Code implements CodeEmitter {

    private final Function function;

    public Code(Function function) {
        this.function = function;
    }

    @Override
    public void emitCode(CodeWriter output) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamCodeWriter codeWriter = new OutputStreamCodeWriter(baos);
        function.emitCode(codeWriter);
        byte[] bytes = baos.toByteArray();
        output.writeInteger(bytes.length);
        output.writeBytes(bytes);
    }
}
