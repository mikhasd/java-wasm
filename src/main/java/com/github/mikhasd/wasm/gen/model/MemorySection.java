package com.github.mikhasd.wasm.gen.model;

import com.github.mikhasd.wasm.gen.CodeWriter;

public class MemorySection extends Section{
    private final Vector<Memory> memories;

    public MemorySection(Vector<Memory> memories) {
        super(Sections.Memory);
        this.memories = memories;
    }

    @Override
    void writeSectionBody(CodeWriter output) {
        this.memories.emitCode(output);
    }
}
