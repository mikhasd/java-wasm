package com.github.mikhasd.wasm.model;

public class Local {
    private final int count;
    private final Type type;

    public Local(int count, Type type) {
        this.count = count;
        this.type = type;
    }

    @Override
    public String toString() {
        return count + ":" + type;
    }
}
