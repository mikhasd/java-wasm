package com.github.mikhasd.wasm.gen;

public interface CodeWriter {
    void writeByte(byte value);
    void writeBytes(byte[] value);
    void writeInteger(int value);
    void writeI32(int value);
    void writeI64(long value);
    void writeF32(float value);
    void writeF64(double value);
    void writeString(String value);
}
