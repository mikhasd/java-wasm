package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.parse.WasmReader;
import com.github.mikhasd.wasm.parse.section.*;

public enum SectionType {
    Custom(SectionType.CUSTOM_IDX),
    Type(SectionType.TYPE_IDX, TypeSectionReader::new),
    Import(SectionType.IMPORT_IDX, ImportSectionReader::new),
    Function(SectionType.FUNCTION_IDX, FunctionSectionReader::new),
    Table(SectionType.TABLE_IDX, TableSectionReader::new),
    Memory(SectionType.MEMORY_IDX, MemorySectionReader::new),
    Global(SectionType.GLOBAL_IDX, GlobalSectionReader::new),
    Export(SectionType.EXPORT_IDX, ExportSectionReader::new),
    Start(SectionType.START_IDX),
    Element(SectionType.ELEMENT_IDX, ElementSectionReader::new),
    Code(SectionType.CODE_IDX, CodeSectionReader::new),
    Data(SectionType.DATA_IDX, DataSectionReader::new);

    public static final byte CUSTOM_IDX = 0x00;
    public static final byte TYPE_IDX = 0x01;
    public static final byte IMPORT_IDX = 0x02;
    public static final byte FUNCTION_IDX = 0x03;
    public static final byte TABLE_IDX = 0x04;
    public static final byte MEMORY_IDX = 0x05;
    public static final byte GLOBAL_IDX = 0x06;
    public static final byte EXPORT_IDX = 0x07;
    public static final byte START_IDX = 0x08;
    public static final byte ELEMENT_IDX = 0x09;
    public static final byte CODE_IDX = 0x0A;
    public static final byte DATA_IDX = 0x0B;

    private byte idx;
    private ReaderFactory readerFactory;

    SectionType(int idx, ReaderFactory readerFactory) {
        this.idx = (byte) idx;
        this.readerFactory = readerFactory;
    }

    SectionType(int idx) {
        this.idx = (byte) idx;
    }

    public static boolean isValidSectionIdx(byte idx) {
        return (CUSTOM_IDX >= idx) || (idx <= DATA_IDX);
    }

    public static SectionType valueOf(byte sectionIdx) {
        for (SectionType section : SectionType.values())
            if (sectionIdx == section.idx)
                return section;

        throw new IllegalArgumentException("Could not find Section with IDX " + sectionIdx);
    }

    public <T> BaseSectionReader<T> newReader(WasmReader input, int length) {
        if (this.readerFactory == null)
            throw new UnsupportedOperationException(String.format("Section reader for %s is not available", this));
        return readerFactory.nu(input, length);
    }

    @FunctionalInterface
    interface ReaderFactory<T> {
        BaseSectionReader<T> nu(WasmReader input, int length);
    }
}
