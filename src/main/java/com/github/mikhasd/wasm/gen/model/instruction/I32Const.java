package com.github.mikhasd.wasm.gen.model.instruction;

import com.github.mikhasd.wasm.gen.CodeWriter;

public class I32Const extends Instruction {
    private int value;

    I32Const(int value) {
        super(OPCODE_I32_CONST);
        this.value = value;
    }

    @Override
    public void emitCode(CodeWriter output) {
        super.emitCode(output);
        output.writeInteger(this.value);
    }
}
