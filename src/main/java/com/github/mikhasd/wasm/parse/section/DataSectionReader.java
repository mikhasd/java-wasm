package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.model.DataSegment;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.WasmReader;

import java.util.ArrayList;

public class DataSectionReader extends BaseSectionReader<DataSegment> {
    public DataSectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected DataSegment read() {
        var index = file.readUnsignedLeb128();
        var expression = new ArrayList<Byte>();
        byte instruction;
        do {
            instruction = file.readU8();
            expression.add(instruction);
        } while (instruction != 0x0B);
        var expressionBuffer = new byte[expression.size()];
        for (int j = 0; j < expression.size(); j++) {
            expressionBuffer[j] = expression.get(j);
        }
        var dataLength = file.readUnsignedLeb128();
        var dataBuffer = file.readBytes(dataLength);

        return new DataSegment(index, expressionBuffer, dataBuffer);
    }

    @Override
    public void handle(Handler handler) {
        handler.onDataSection(this);
    }
}
