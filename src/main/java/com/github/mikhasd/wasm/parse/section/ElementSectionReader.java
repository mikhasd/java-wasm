package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.model.ElementSegment;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.WasmReader;

import java.util.ArrayList;

public class ElementSectionReader extends BaseSectionReader<ElementSegment> {
    public ElementSectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected ElementSegment read() {
        var tableIndex = file.readI32();
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
        var funcVecSize = file.readI32();
        var funcIdxs = new int[funcVecSize];
        for (int k = 0; k < funcVecSize; k++) {
            funcIdxs[k] = file.readI32();
        }

        return new ElementSegment(tableIndex, expressionBuffer, funcIdxs);
    }

    @Override
    public void handle(Handler handler) {
        handler.onElementSection(this);
    }
}
