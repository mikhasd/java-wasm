package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.BufferReader;
import com.github.mikhasd.wasm.parse.WasmReader;

public class FunctionSectionReader extends BaseSectionReader<Integer> {

    public FunctionSectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected Integer read() {
        return file.readUnsignedLeb128();
    }

    @Override
    public void handle(Handler handler) {
        handler.onFunctionSection(this);
    }
}
