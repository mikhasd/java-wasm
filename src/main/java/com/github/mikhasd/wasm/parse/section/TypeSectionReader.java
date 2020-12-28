package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.model.Type;
import com.github.mikhasd.wasm.model.TypeDefinition;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.WasmReader;

public class TypeSectionReader extends BaseSectionReader<TypeDefinition> {

    public TypeSectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected TypeDefinition readOne() {
        byte typeType = file.readU8();
        switch (typeType) {
            case 0x60:
                return readFunctionType();
            case 0x61:
                return readModuleType();
            case 0x62:
                return readInstanceType();
        }
        return null;
    }

    private TypeDefinition readInstanceType() {
        throw new UnsupportedOperationException();
    }

    private TypeDefinition readModuleType() {
        throw new UnsupportedOperationException();
    }

    private TypeDefinition.FunctionType readFunctionType() {
        int parameterCount = file.readUnsignedLeb128();
        Type[] parameterTypes = new Type[parameterCount];
        for (int j = 0; j < parameterCount; j++)
            parameterTypes[j] = file.type();

        int resultCount = file.readUnsignedLeb128();
        Type[] resultTypes = new Type[resultCount];
        for (int j = 0; j < resultCount; j++)
            resultTypes[j] = file.type();

        return new TypeDefinition.FunctionType(parameterTypes, resultTypes);
    }

    @Override
    public void handle(Handler handler) {
        handler.onTypeSection(this);
    }
}
