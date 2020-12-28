package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.model.Limits;
import com.github.mikhasd.wasm.model.Memory;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.BufferReader;
import com.github.mikhasd.wasm.parse.WasmReader;

public class MemorySectionReader extends BaseSectionReader<Memory> {

    public MemorySectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected Memory readOne() {
        Limits limits = file.readLimits();
        return new Memory(limits);
    }

    @Override
    public void handle(Handler handler) {
        handler.onMemorySection(this);
    }
}
