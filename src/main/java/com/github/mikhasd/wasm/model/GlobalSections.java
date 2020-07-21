package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.gen.CodeWriter;

public class GlobalSections extends Section {
    private final Vector<Global> globals;

    public GlobalSections(Vector<Global> globals) {
        super(Sections.Global);
        this.globals = globals;
    }

    @Override
    void writeSectionBody(CodeWriter output) {
        globals.emitCode(output);
    }
}
