package com.github.mikhasd.wasm.gen.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

public class Export implements CodeEmitter {

    private final String name;
    private final byte type;
    private final int index;

    private Export(String name, byte type, int index) {
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
