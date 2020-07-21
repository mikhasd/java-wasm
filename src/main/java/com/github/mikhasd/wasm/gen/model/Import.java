package com.github.mikhasd.wasm.gen.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

import java.util.Set;

public class Import implements CodeEmitter {

    public static final byte TYPE_FUNCTION = 0x00;
    public static final byte TYPE_TABLE = 0x01;
    public static final byte TYPE_MEMORY = 0x02;
    public static final byte TYPE_GLOBAL = 0x03;
    private static final Set<Byte> VALID_TYPES = Set.of(TYPE_FUNCTION, TYPE_TABLE, TYPE_MEMORY, TYPE_GLOBAL);

    private final String module;
    private final String name;
    private final byte type;
    private final int index;

    public Import(String module, String name, byte type, int index) {
        if (!VALID_TYPES.contains(type)) {
            throw new IllegalArgumentException("Invalid import type: " + String.format("%2x", type));
        }
        this.module = module;
        this.name = name;
        this.type = type;
        this.index = index;
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

    @Override
    public void emitCode(CodeWriter output) {
        output.writeString(module);
        output.writeString(name);
        output.writeByte(type);
        output.writeInteger(index);
    }
}
