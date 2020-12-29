package com.github.mikhasd.wasm.eval;

import com.github.mikhasd.wasm.WasmRuntimeException;

public class EvaluationException extends WasmRuntimeException {
    public EvaluationException(String message) {
        super(message);
    }
}
