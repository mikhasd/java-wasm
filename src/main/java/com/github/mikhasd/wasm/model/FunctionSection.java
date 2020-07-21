package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.gen.CodeWriter;

public class FunctionSection extends Section {

    private final IndexVector types;

    public FunctionSection(IndexVector types) {
        super(Sections.Function);
        this.types = types;
    }

    @Override
    void writeSectionBody(CodeWriter output) {
        types.emitCode(output);
    }
}
