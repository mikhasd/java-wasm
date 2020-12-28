package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.model.ImportSectionEntryType;
import com.github.mikhasd.wasm.model.Type;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.ParseException;
import com.github.mikhasd.wasm.parse.BufferReader;
import com.github.mikhasd.wasm.parse.WasmReader;

import static com.github.mikhasd.wasm.model.Element.Kind;
import static com.github.mikhasd.wasm.model.ImportSectionEntryType.Function;

public class ElementSectionReader extends BaseSectionReader<Object> {
    public ElementSectionReader(WasmReader file, int length) {
        super(file, length);
    }

    @Override
    protected Object readOne() {
        var flags = file.readI32();

        final Kind kind;
        if ((flags & 0b001) != 0) {
            if ((flags & 0b010) != 0) {
                kind = Kind.declared();
            } else {
                kind = Kind.passive();
            }
        } else {
            var tableIndex = (flags & 0b010) == 0 ? 0 : file.readUnsignedLeb128();
            var length = file.readUnsignedLeb128();
            var code = file.readBytes(length);
            kind = Kind.active(tableIndex, code);
        }

        var exprs = (flags & 0b100) != 0;

        final Type type;
        if ((flags & 0b011) != 0) {
            if (exprs) {
                type = file.type();
            } else {
                ImportSectionEntryType sectionEntryType = file.readExternalKind();
                if (sectionEntryType.isA(Function.class)) {
                    type = Type.FuncRef;
                } else {
                    throw new ParseException("only the function external type is supported in elem segment");
                }
            }
        } else {
            type = Type.FuncRef;
        }

        var count = file.readUnsignedLeb128();

        if (exprs) {

        } else {

        }

        throw new UnsupportedOperationException();
    }

    @Override
    public void handle(Handler handler) {

    }
}
