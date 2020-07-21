package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

public abstract class VectorSection<T extends CodeEmitter> extends Section {

    private final Vector<T> items;

    VectorSection(Sections sections, Vector<T> items) {
        super(sections);
        this.items = items;
    }

    @Override
    void writeSectionBody(CodeWriter output) {
        items.emitCode(output);
    }
}
