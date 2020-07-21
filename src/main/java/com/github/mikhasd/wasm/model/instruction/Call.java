package com.github.mikhasd.wasm.model.instruction;

import com.github.mikhasd.wasm.gen.CodeWriter;

public class Call extends Instruction {

    private int index;

    Call(int index) {
        super(OPCODE_CALL);
        this.index = index;
    }

    @Override
    public void emitCode(CodeWriter output) {
        super.emitCode(output);
        output.writeInteger(this.index);
    }
}
