package com.github.mikhasd.wasm.parse;

import com.github.mikhasd.wasm.model.SectionType;
import com.github.mikhasd.wasm.parse.section.BaseSectionReader;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Parser {

    private final static byte[] MAGIC = {0x00, 0x61, 0x73, 0x6D};

    public void parse(ByteBuffer buffer, Handler handler) {
        var file = new WasmReader(buffer);
        checkMagic(file);

        var version = file.version();
        handler.onVersion(version);
        try {
            while (true) {
                var sectionIdx = file.sectionIdx();
                var sectionLength = file.readUnsignedLeb128();

                if (sectionIdx == SectionType.CUSTOM_IDX) {
                    handler.onCustomSection(file.slice(sectionLength));
                    file.skip(sectionLength);
                } else if (sectionIdx == SectionType.START_IDX) {
                    handler.onStart(file.readUnsignedLeb128());
                } else if (SectionType.isValidSectionIdx(sectionIdx)) {
                    var sectionType = SectionType.valueOf(sectionIdx);
                    BaseSectionReader<Object> reader = sectionType.newReader(file, sectionLength);
                    reader.handle(handler);
                    reader.drain();
                }

            }
        } catch (BufferUnderflowException bufe) {
            return;
        }
    }

    private void checkMagic(WasmReader file) {
        var magic = file.magic();
        if (!Arrays.equals(magic, MAGIC)) {
            throw new ParseException("Invalid magic");
        }
    }
}
