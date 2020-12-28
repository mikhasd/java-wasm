package com.github.mikhasd.wasm.parse;

import com.github.mikhasd.wasm.model.ExternalKind;
import com.github.mikhasd.wasm.model.ImportSectionEntryType;
import com.github.mikhasd.wasm.model.Limits;
import com.github.mikhasd.wasm.model.Type;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class WasmReader extends BufferReader {

    WasmReader(ByteBuffer buffer) {
        super(buffer);
    }

    byte[] magic() {
        byte[] buf = new byte[4];
        getBuffer().get(buf, 0, 4);
        return buf;
    }

    int version() {
        return this.readI32();
    }

    byte sectionIdx() {
        return this.readU7();
    }

    public Type type() {
        return Type.valueOf(this.readU7());
    }

    public Limits readLimits() {
        var flags = this.readU8();
        var bounded = flags == 0x01;
        var initial = this.readUnsignedLeb128();
        if (bounded) {
            var maximum = this.readUnsignedLeb128();
            return new Limits(initial, maximum);
        } else {
            return new Limits(initial);
        }
    }

    public ImportSectionEntryType readExternalKind() {
        var importCode = this.readU8();
        var externalKind = ExternalKind.valueOf(importCode);
        switch (externalKind) {
            case Function:
                return new ImportSectionEntryType.Function(this.readUnsignedLeb128());
            case Table:
                return new ImportSectionEntryType.Table(this.type(), this.readLimits());
            case Memory:
                return new ImportSectionEntryType.Memory(this.readLimits());
            case Global:
                return new ImportSectionEntryType.Global(this.type(), this.readU8() != 0x00);
            case Module:
                return new ImportSectionEntryType.Module(this.readUnsignedLeb128());
            case Instance:
                return new ImportSectionEntryType.Instance(this.readUnsignedLeb128());
        }
        return null;
    }

    public final String readString() {
        int length = readUnsignedLeb128();
        byte[] buffer = new byte[length];
        getBuffer().get(buffer, 0, length);
        return new String(buffer, StandardCharsets.UTF_8);
    }
}
