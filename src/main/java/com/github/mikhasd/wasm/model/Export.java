package com.github.mikhasd.wasm.model;

public class Export {
    private final String name;
    private final ExternalKind kind;
    private final int index;

    public Export(String name, ExternalKind kind, int index) {
        this.name = name;
        this.kind = kind;
        this.index = index;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", kind=" + kind +
                ", index=" + index +
                '}';
    }
}
