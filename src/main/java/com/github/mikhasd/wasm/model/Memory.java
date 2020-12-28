package com.github.mikhasd.wasm.model;

public class Memory {
    private final Limits limits;

    public Memory(Limits limits) {
        this.limits = limits;
    }

    @Override
    public String toString() {
        return "{" +
                "limits=" + limits +
                '}';
    }
}
