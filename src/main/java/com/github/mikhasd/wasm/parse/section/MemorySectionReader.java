package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.model.Limits;
import com.github.mikhasd.wasm.model.MemorySegment;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.WasmReader;

public class MemorySectionReader extends BaseSectionReader<MemorySegment> {

    public MemorySectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected MemorySegment read() {
        Limits limits = file.readLimits();
        return new MemorySegment(limits);
    }

    @Override
    public void handle(Handler handler) {
        handler.onMemorySection(this);
    }
}
