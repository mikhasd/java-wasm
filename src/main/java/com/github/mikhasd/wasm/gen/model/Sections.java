package com.github.mikhasd.wasm.gen.model;

public enum Sections {
    Custom(Sections.CUSTOM_IDX),
    Type(Sections.TYPE_IDX),
    Import(Sections.IMPORT_IDX),
    Function(Sections.FUNCTION_IDX),
    Table(Sections.TABLE_IDX),
    Memory(Sections.MEMORY_IDX),
    Global(Sections.GLOBAL_IDX),
    Export(Sections.EXPORT_IDX),
    Start(Sections.START_IDX),
    Element(Sections.ELEMENT_IDX),
    Code(Sections.CODE_IDX),
    Data(Sections.DATA_IDX);

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

    Sections(int idx) {
        this.idx = (byte) idx;
    }

    public static Sections valueOf(byte sectionIdx) {
        for (Sections section : Sections.values())
            if (sectionIdx == section.idx)
                return section;

        throw new IllegalArgumentException("Could not find Section with IDX " + sectionIdx);
    }

    public byte getIdx() {
        return this.idx;
    }
}

