package com.github.mikhasd.wasm.parse.section;


import com.github.mikhasd.wasm.model.FunctionSegment;
import com.github.mikhasd.wasm.model.Local;
import com.github.mikhasd.wasm.model.Type;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.WasmReader;

public class CodeSectionReader extends BaseSectionReader<FunctionSegment> {
    public CodeSectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected FunctionSegment read() {
        var functionLength = file.readUnsignedLeb128();
        var functionStart = file.position();
        var localsCount = file.readUnsignedLeb128();
        var locals = new Local[localsCount];
        for (var l = 0; l < localsCount; l++) {
            var count = file.readUnsignedLeb128();
            var typeIdx = file.readU8();
            var type = Type.valueOf(typeIdx);
            locals[l] = new Local(count, type);
        }
        var functionEnd = file.position();
        var codeLength = functionLength - (functionEnd - functionStart);
        byte[] opcodes = file.readBytes(codeLength);

        return new FunctionSegment(locals, opcodes);
    }

    @Override
    public void handle(Handler handler) {
        handler.onFunction(this);
    }
}
