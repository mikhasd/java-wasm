package com.github.mikhasd.wasm.gen;

public interface ByteOutput {
    void write(byte b);
    void write(byte[] bytes);
}
