package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.gen.CodeWriter;

public class CodeSection extends Section {

    private final Vector<Code> codes;

    public CodeSection(Vector<Code> codes) {
        super(Sections.Code);
        this.codes = codes;
    }

    @Override
    void writeSectionBody(CodeWriter output) {
        codes.emitCode(output);
    }
}
