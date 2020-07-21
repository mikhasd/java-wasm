package com.github.mikhasd.wasm.gen.model;

import com.github.mikhasd.wasm.gen.CodeWriter;

public class TableSection extends Section {
    private final Vector<Table> tables;

    public TableSection(Vector<Table> tables) {
        super(Sections.Table);
        this.tables = tables;
    }


    @Override
    void writeSectionBody(CodeWriter output) {
        this.tables.emitCode(output);
    }
}
