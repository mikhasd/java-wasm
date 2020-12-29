package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.model.Table;
import com.github.mikhasd.wasm.model.Type;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.BufferReader;
import com.github.mikhasd.wasm.parse.WasmReader;

public class TableSectionReader extends BaseSectionReader<Table> {

    public TableSectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected Table read() {
        Type type = file.type();
        var limits = file.readLimits();
        return new Table(type, limits);
    }

    @Override
    public void handle(Handler handler) {
        handler.onTableSection(this);
    }
}
