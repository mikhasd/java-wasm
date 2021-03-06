package com.github.mikhasd.wasm;

import com.github.mikhasd.wasm.model.*;
import com.github.mikhasd.wasm.parse.BufferReader;
import com.github.mikhasd.wasm.parse.Parser;
import com.github.mikhasd.wasm.parse.section.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Objects;

public class ModuleParseTest {

    public static void main(String... args) throws IOException, URISyntaxException {
        Path wasmPath = Paths.get("/home/mikhas/projects/java-wasm/src/test/resources/main_simply_add.wasm");

        try (var channel = FileChannel.open(wasmPath, StandardOpenOption.READ)) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            new Parser().parse(buffer, new Handler());
        }

    }

    private static final class Handler implements com.github.mikhasd.wasm.parse.Handler {
        @Override
        public void onVersion(byte[] version) {
            System.out.printf("Got version %s\n", Arrays.toString(version));
        }

        @Override
        public void onCustomSection(BufferReader buffer) {
            int nameLength = buffer.readUnsignedLeb128();
            byte[] bytes = buffer.readBytes(nameLength);
            String customSectionName = new String(bytes, StandardCharsets.UTF_8);
            System.out.printf("Got Custom section %s\n", customSectionName);
        }

        @Override
        public void onStart(int i) {
            System.out.printf("Got start 0x%X\n", i);
        }

        @Override
        public void onTypeSection(TypeSectionReader reader) {
            reader.forEach(type -> System.out.printf("Got type %s\n", type));
        }

        @Override
        public void onDataSection(DataSectionReader reader) {
            reader.forEach(item -> System.out.printf("Got data %s\n", item));
        }

        @Override
        public void onExportSection(ExportSectionReader reader) {
            reader.forEach(item -> System.out.printf("Got export %s\n", item));
        }

        @Override
        public void onFunctionSection(FunctionSectionReader reader) {
            reader.forEach(item -> System.out.printf("Got function with type %s\n", item));
        }

        @Override
        public void onGlobalSection(GlobalSectionReader reader) {
            reader.forEach(item -> System.out.printf("Got global %s\n", item));
        }

        @Override
        public void onImportSection(ImportSectionReader reader) {
            reader.forEach(item -> System.out.printf("Got global %s\n", item));
        }

        @Override
        public void onMemorySection(MemorySectionReader reader) {
            reader.forEach(item -> System.out.printf("Got memory %s\n", item));
        }

        @Override
        public void onTableSection(TableSectionReader reader) {
            reader.forEach(item -> System.out.printf("Got table %s\n", item));
        }

        @Override
        public void onFunction(CodeSectionReader reader) {
            reader.forEach(item -> System.out.printf("Got function %s\n", item));
        }

        @Override
        public void onElementSection(ElementSectionReader reader) {
            reader.forEach(item -> System.out.printf("Got element %s\n", item));
        }
    }
}
