package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.model.Export;
import com.github.mikhasd.wasm.model.ExternalKind;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.WasmReader;

public class ExportSectionReader extends BaseSectionReader<Export> {
    public ExportSectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected Export readOne() {
        String name = file.readString();
        var importCode = file.readU8();
        var kind = ExternalKind.valueOf(importCode);
        var index = file.readUnsignedLeb128();
        return new Export(name, kind, index);
    }

    @Override
    public void handle(Handler handler) {
        handler.onExportSection(this);
    }
}
