package com.github.mikhasd.wasm.model.instruction;

import com.github.mikhasd.wasm.gen.CodeWriter;

public class GetLocal extends Instruction{

    private static final GetLocal[] CACHED;

    static {
        CACHED = new GetLocal[10];
        for (int i = 0; i < 10; i++) {
            CACHED[i] = new GetLocal(i);
        }
    }

    private final int index;

    private GetLocal(int index) {
        super(Instruction.OPCODE_GET_LOCAL);
        this.index = index;
    }

    static GetLocal withIndex(int index) {
        if (index < 0)
            throw new IllegalArgumentException("Locals must be positive: " + index);
        if (index < 10)
            return CACHED[index];
        return new GetLocal(index);
    }

    @Override
    public void emitCode(CodeWriter output) {
        super.emitCode(output);
        output.writeInteger(index);
    }
}
