package com.github.mikhasd.wasm.eval;

import com.github.mikhasd.wasm.WasmException;

public class NotExportedException extends WasmException {
    public NotExportedException(String name) {
        super(name);
    }
}
