package com.github.mikhasd.wasm.model;

public interface Section {
    byte CUSTOM_IDX = 0x00;
    byte TYPE_IDX = 0x01;
    byte IMPORT_IDX = 0x02;
    byte FUNCTION_IDX = 0x03;
    byte TABLE_IDX = 0x04;
    byte MEMORY_IDX = 0x05;
    byte GLOBAL_IDX = 0x06;
    byte EXPORT_IDX = 0x07;
    byte START_IDX = 0x08;
    byte ELEMENT_IDX = 0x09;
    byte CODE_IDX = 0x0A;
    byte DATA_IDX = 0x0B;
}
