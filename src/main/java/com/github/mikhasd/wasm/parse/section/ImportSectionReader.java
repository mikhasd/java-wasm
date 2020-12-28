package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.model.Import;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.BufferReader;
import com.github.mikhasd.wasm.parse.WasmReader;

public class ImportSectionReader extends BaseSectionReader<Import> {

    public ImportSectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected Import readOne() {
        var module = file.readString();
        var name = file.readString();
        var sectionEntryType = file.readExternalKind();
        return new Import(module, name, sectionEntryType);
    }

    @Override
    public void handle(Handler handler) {
        handler.onImportSection(this);
    }
}
