package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

import java.util.Set;

public class Export implements CodeEmitter {

    public static final byte TYPE_FUNCTION = 0x00;
    public static final byte TYPE_TABLE = 0x01;
    public static final byte TYPE_MEMORY = 0x02;
    public static final byte TYPE_GLOBAL = 0x03;
    private static final Set<Byte> VALID_TYPES = Set.of(TYPE_FUNCTION, TYPE_TABLE, TYPE_MEMORY, TYPE_GLOBAL);

    private final String name;
    private final byte type;
    private final int index;

    public Export(String name, byte type, int index) {
        if (!VALID_TYPES.contains(type)) {
            throw new IllegalArgumentException("Invalid export type: " + String.format("%2x", type));
        }
        this.name = name;
        this.type = type;
        this.index = index;
    }

    @Override
    public void emitCode(CodeWriter output) {
        output.writeString(this.name);
        output.writeByte(this.type);
        output.writeInteger(this.index);
    }

    public static Export function(String name, int index){
        return new Export(name, (byte)0x00, index);
    }

    public static Export table(String name, int index){
        return new Export(name, (byte)0x01, index);
    }

    public static Export memory(String name, int index){
        return new Export(name, (byte)0x02, index);
    }

    public static Export global(String name, int index){
        return new Export(name, (byte)0x03, index);
    }
}
