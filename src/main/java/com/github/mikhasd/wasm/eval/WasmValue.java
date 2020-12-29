package com.github.mikhasd.wasm.eval;

import com.github.mikhasd.wasm.model.Type;

public class WasmValue<T> {
    private final Type type;
    private final T value;

    public WasmValue(Type type, T value) {
        this.type = type;
        this.value = value;
    }

    public Type type(){
        return this.type;
    }

    public T value(){
        return this.value;
    }

    @Override
    public String toString() {
        return "{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
