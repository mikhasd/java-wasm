package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.gen.CodeWriter;

public class DataSection extends Section {

    private final Vector<Data> datas;

    public DataSection(Vector<Data> datas) {
        super(Sections.Data);
        this.datas = datas;
    }

    @Override
    void writeSectionBody(CodeWriter output) {
        this.datas.emitCode(output);
    }
}
