package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.model.Global;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.BufferReader;
import com.github.mikhasd.wasm.parse.WasmReader;

import java.util.ArrayList;

public class GlobalSectionReader extends BaseSectionReader<Global> {
    public GlobalSectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected Global readOne() {
        var type = file.type();
        var mutability = file.readU8();
        byte instruction;
        var instructionsList = new ArrayList<Byte>();
        do {
            instruction = file.readU8();
            instructionsList.add(instruction);
        } while (instruction != 0x0B);
        var expressionBuffer = new byte[instructionsList.size()];
        for (int j = 0; j < expressionBuffer.length; j++) {
            expressionBuffer[j] = instructionsList.get(j);
        }

        return new Global(type, mutability, expressionBuffer);
    }

    @Override
    public void handle(Handler handler) {
        handler.onGlobalSection(this);
    }
}
