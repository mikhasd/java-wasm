package com.github.mikhasd.wasm.model;

public class ExportSegment {
    private final String name;
    private final ExternalKind kind;
    private final int index;

    public ExportSegment(String name, ExternalKind kind, int index) {
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

    public String name() {
        return this.name;
    }

    public ExternalKind type() {
        return this.kind;
    }

    public int index() {
        return this.index;
    }
}
