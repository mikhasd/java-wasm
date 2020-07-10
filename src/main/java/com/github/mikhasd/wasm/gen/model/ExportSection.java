package com.github.mikhasd.wasm.gen.model;

import com.github.mikhasd.wasm.gen.CodeWriter;

public class ExportSection extends Section {

    private final Vector<Export> exports;

    public ExportSection(Vector<Export> exports) {
        super(Sections.Export);
        this.exports = exports;
    }

    @Override
    void writeSectionBody(CodeWriter output) {
        exports.emitCode(output);
    }
}
