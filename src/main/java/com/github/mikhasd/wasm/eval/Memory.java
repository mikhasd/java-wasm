package com.github.mikhasd.wasm.eval;

import com.github.mikhasd.wasm.model.Limits;

public class Memory {

    private final int PAGE_SIZE = 65536;
    private final Limits limits;
    private final byte[] bytes;

    public Memory(Limits limits) {
        this.limits = limits;
        this.bytes = new byte[limits.getInitial() + PAGE_SIZE];
    }

    public void copy(byte[] bytes, int offset) {
        System.arraycopy(bytes, 0, this.bytes, offset, bytes.length);
    }
}
