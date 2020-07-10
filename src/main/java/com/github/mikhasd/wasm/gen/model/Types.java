package com.github.mikhasd.wasm.gen.model;

/**
 * @see <a href="https://webassembly.github.io/spec/core/binary/types.html">Types<a/>
 */
public enum Types {
    ;
    public static final int TYPE_INDEX_I32 = 0x7F;
    public static final int TYPE_INDEX_I64 = 0x7E;
    public static final int TYPE_INDEX_F32 = 0x7D;
    public static final int TYPE_INDEX_F64 = 0x7C;

    public static final int TYPE_RESULT = 0x40;
    public static final int TYPE_FUNCTION = 0x60;
    public static final int TYPE_TABLE = 0x70;


}
