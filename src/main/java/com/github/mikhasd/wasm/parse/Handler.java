package com.github.mikhasd.wasm.parse;

import com.github.mikhasd.wasm.parse.section.*;

import java.nio.ByteBuffer;

public interface Handler {
    void onVersion(byte[] version);

    void onCustomSection(BufferReader buffer);

    void onStart(int i);

    void onTypeSection(TypeSectionReader reader);

    void onDataSection(DataSectionReader reader);

    void onExportSection(ExportSectionReader reader);

    void onFunctionSection(FunctionSectionReader reader);

    void onGlobalSection(GlobalSectionReader reader);

    void onImportSection(ImportSectionReader reader);

    void onMemorySection(MemorySectionReader reader);

    void onTableSection(TableSectionReader reader);

    void onFunction(CodeSectionReader reader);

    void onElementSection(ElementSectionReader reader);
}
