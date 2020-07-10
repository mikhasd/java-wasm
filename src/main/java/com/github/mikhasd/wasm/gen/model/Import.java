package com.github.mikhasd.wasm.gen.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

public class Import implements CodeEmitter {

    private final String module;
    private final String name;
    private final byte type;
    private final int index;

    private Import(String module, String name, byte type, int index) {
        this.module = module;
        this.name = name;
        this.type = type;
        this.index = index;
    }

    @Override
    public void emitCode(CodeWriter output) {
        output.writeString(module);
        output.writeString(name);
        output.writeByte(type);
        output.writeInteger(index);
    }

    public static Import function(String module, String name, int index) {
        return new Import(module, name, (byte) 0x00, index);
    }

    public static Import table(String module, String name, int index) {
        return new Import(module, name, (byte) 0x01, index);
    }

    public static Import memory(String module, String name, int index) {
        return new Import(module, name, (byte) 0x02, index);
    }

    public static Import global(String module, String name, int index) {
        return new Import(module, name, (byte) 0x03, index);
    }
}
