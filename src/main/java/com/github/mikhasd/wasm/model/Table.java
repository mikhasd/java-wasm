package com.github.mikhasd.wasm.model;

public class Table {
    private final Type type;
    private final Limits limits;

    public Table(Type type, Limits limits) {
        this.type = type;
        this.limits = limits;
    }

    @Override
    public String toString() {
        return "{" +
                "type=" + type +
                ", limits=" + limits +
                '}';
    }
}
