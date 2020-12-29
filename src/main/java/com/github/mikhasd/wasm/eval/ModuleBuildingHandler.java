package com.github.mikhasd.wasm.eval;

import com.github.mikhasd.wasm.model.*;
import com.github.mikhasd.wasm.parse.BufferReader;
import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.section.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

public class ModuleBuildingHandler implements Handler {

    private static final byte[] SUPPORTED_VERSION = {0x01, 0x00, 0x00, 0x00};
    private List<TypeDefinition> types = emptyList();
    private List<MemorySegment> memories = emptyList();
    private List<Global> globals = emptyList();
    private List<DataSegment> datas = emptyList();
    private List<ElementSegment> elements = emptyList();
    private List<ExportSegment> exportSegments = emptyList();
    private List<Integer> functionTypes = emptyList();
    private List<FunctionSegment> functionSegments = emptyList();

    @Override
    public void onVersion(byte[] version) {
        if (!Arrays.equals(version, SUPPORTED_VERSION)) {
            throw new UnsupportedVersionException(SUPPORTED_VERSION, version);
        }
    }

    @Override
    public void onCustomSection(BufferReader buffer) {

    }

    @Override
    public void onStart(int i) {

    }

    @Override
    public void onTypeSection(TypeSectionReader reader) {
        types = reader.toList();
    }

    @Override
    public void onDataSection(DataSectionReader reader) {
        datas = reader.toList();
    }

    @Override
    public void onExportSection(ExportSectionReader reader) {
        exportSegments = reader.toList();
    }

    @Override
    public void onFunctionSection(FunctionSectionReader reader) {
        functionTypes = reader.toList();
    }

    @Override
    public void onGlobalSection(GlobalSectionReader reader) {
        globals = reader.toList();
    }

    @Override
    public void onImportSection(ImportSectionReader reader) {

    }

    @Override
    public void onMemorySection(MemorySectionReader reader) {
        memories = reader.toList();
    }

    @Override
    public void onTableSection(TableSectionReader reader) {

    }

    @Override
    public void onFunction(CodeSectionReader reader) {
        functionSegments = reader.toList();
    }

    @Override
    public void onElementSection(ElementSectionReader reader) {
        elements = reader.toList();
    }

    public Module build() {
        return new Module(memories, globals, datas, elements, types, exportSegments, functionTypes, functionSegments);
    }
}
