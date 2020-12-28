package com.github.mikhasd.wasm.parse;

import com.github.mikhasd.wasm.WasmException;

public class ParseException extends WasmException {
    public ParseException(String message){
        super(message);
    }
}
