package com.github.mikhasd.wasm.model.instruction;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

public class Instruction implements CodeEmitter {

    static byte OPCODE_CALL = 0x10;
    static byte OPCODE_GET_LOCAL = 0x20;
    static byte OPCODE_I32_CONST = 0x41;
    static byte OPCODE_I32_ADD = 0x6A;
    static Instruction I32_ADD_INSTRUCTION = new Instruction(OPCODE_I32_ADD);

    private final byte opcode;

    protected Instruction(byte opcode) {
        this.opcode = opcode;
    }

    public static Instruction i32Add() {
        return I32_ADD_INSTRUCTION;
    }

    public static GetLocal getLocal(int index) {
        return GetLocal.withIndex(index);
    }

    public static Instruction call(int index) {
        return new Call(index);
    }

    public static Instruction i32Const(int value) {
        return new I32Const(value);
    }

    @Override
    public void emitCode(CodeWriter output) {
        output.writeByte(this.opcode);
    }
}
