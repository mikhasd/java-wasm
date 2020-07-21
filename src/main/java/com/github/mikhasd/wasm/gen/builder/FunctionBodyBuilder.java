package com.github.mikhasd.wasm.gen.builder;

import com.github.mikhasd.wasm.gen.OutputStreamCodeWriter;
import com.github.mikhasd.wasm.model.instruction.Instruction;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FunctionBodyBuilder {
    private List<Instruction> instructions = new ArrayList<>();

    FunctionBodyBuilder() {
    }

    public FunctionBodyBuilder getLocal(int index) {
        instructions.add(Instruction.getLocal(index));
        return this;
    }

    public FunctionBodyBuilder call(FunctionReference fnRef) {
        instructions.add(Instruction.call(fnRef.getIndex()));
        return this;
    }

    public FunctionBodyBuilder i32Add() {
        instructions.add(Instruction.i32Add());
        return this;
    }

    public FunctionBodyBuilder call(FunctionReference.Holder fnRefHolder) {
        instructions.add(Instruction.call(fnRefHolder.getIndex()));
        return this;
    }

    public FunctionBodyBuilder i32Const(int constValue) {
        instructions.add(Instruction.i32Const(constValue));
        return this;
    }

    public byte[] getOpCodes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(this.instructions.size() * 2);
        OutputStreamCodeWriter codeWriter = new OutputStreamCodeWriter(baos);
        for (Instruction instruction : this.instructions) {
            instruction.emitCode(codeWriter);
        }
        baos.write(0x0b);
        return baos.toByteArray();
    }
}
