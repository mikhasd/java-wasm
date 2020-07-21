package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

public class Table implements CodeEmitter {
    public static final byte ELEMENT_TYPE_TABLE = (byte) 0x70;
    public static final byte BOUNDED_INDICATOR = (byte) 0x01;
    public static final byte UNBOUNDED_INDICATOR = (byte) 0x00;

    private final int min;
    private final int max;
    private final boolean bounded;

    public Table(int min, int max) {
        this.min = min;
        this.max = max;
        this.bounded = true;
    }

    public Table(int min) {
        this.min = min;
        this.max = 0;
        this.bounded = false;
    }

    @Override
    public void emitCode(CodeWriter output) {
        output.writeByte(ELEMENT_TYPE_TABLE);
        if (this.bounded) {
            output.writeByte(BOUNDED_INDICATOR);
            output.writeInteger(this.min);
            output.writeInteger(this.max);
        } else {
            output.writeByte(UNBOUNDED_INDICATOR);
            output.writeInteger(this.min);
        }
    }
}

