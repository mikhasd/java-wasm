package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;
import com.github.mikhasd.wasm.gen.OutputStreamCodeWriter;

import java.io.ByteArrayOutputStream;

public abstract class Section implements CodeEmitter {

    private Sections section;

    Section(Sections sections) {
        this.section = sections;
    }

    abstract void writeSectionBody(CodeWriter output);

    @Override
    public final void emitCode(CodeWriter output) {
        output.writeByte(this.section.getIdx());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamCodeWriter codeWriter = new OutputStreamCodeWriter(baos);
        this.writeSectionBody(codeWriter);
        byte[] bytes = baos.toByteArray();
        output.writeInteger(bytes.length);
        output.writeBytes(bytes);
    }
}
