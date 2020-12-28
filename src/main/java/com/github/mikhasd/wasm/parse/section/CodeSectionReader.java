package com.github.mikhasd.wasm.parse.section;


import com.github.mikhasd.wasm.model.Function;
import com.github.mikhasd.wasm.model.Local;
import com.github.mikhasd.wasm.model.Type;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.WasmReader;

public class CodeSectionReader extends BaseSectionReader<Function> {
    public CodeSectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected Function readOne() {
        final var functionLength = file.readUnsignedLeb128();
        final var functionBodyBuffer = file.slice(functionLength);
        final var localsCount = functionBodyBuffer.readUnsignedLeb128();
        final var locals = new Local[localsCount];
        for (var l = 0; l < localsCount; l++) {
            var count = functionBodyBuffer.readUnsignedLeb128();
            var typeIdx = functionBodyBuffer.readU8();
            var type = Type.valueOf(typeIdx);
            locals[l] = new Local(count, type);
        }
        file.skip(functionLength);

        return new Function(locals, functionBodyBuffer);
    }

    @Override
    public void handle(Handler handler) {
        handler.onFunction(this);
    }
}
