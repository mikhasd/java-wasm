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
import java.util.Objects;

public class ModuleParseTest {

    public static void main(String... args) throws IOException, URISyntaxException {
        Path wasmPath = Paths.get("/home/mikhas/projects/java-wasm/src/test/resources/main.wasm");

        try (var channel = FileChannel.open(wasmPath, StandardOpenOption.READ)) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            new Parser().parse(buffer, new Handler());
        }

    }

    private static final class Handler implements com.github.mikhasd.wasm.parse.Handler {
        @Override
        public void onVersion(int version) {
            System.out.printf("Got version %d\n", version);
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
            while (reader.hasNext()){
                TypeDefinition type = reader.read();
                System.out.printf("Got type %s\n", type);
            }
        }

        @Override
        public void onDataSection(DataSectionReader reader) {
            while(reader.hasNext()){
                var data = reader.read();
                System.out.printf("Got data %s\n", data);
            }
        }

        @Override
        public void onExportSection(ExportSectionReader reader) {
            while(reader.hasNext()){
                Export export = reader.read();
                System.out.printf("Got export %s\n",export);
            }

        }

        @Override
        public void onFunctionSection(FunctionSectionReader reader) {
            while(reader.hasNext()){
                Integer fnIdx = reader.read();
                System.out.printf("Got function with type %d\n", fnIdx);
            }
        }

        @Override
        public void onGlobalSection(GlobalSectionReader reader) {
            while (reader.hasNext()){
                Global global = reader.read();
                System.out.printf("Got global %s\n", global);
            }
        }

        @Override
        public void onImportSection(ImportSectionReader reader) {
            while (reader.hasNext()){
                Import anImport = reader.read();
                System.out.printf("Got import %s\n", anImport);
            }
        }

        @Override
        public void onMemorySection(MemorySectionReader reader) {
            while (reader.hasNext()){
                Memory memory = reader.read();
                System.out.printf("Got memory %s\n", memory);
            }
        }

        @Override
        public void onTableSection(TableSectionReader reader) {

            while(reader.hasNext()){
                Table table = reader.read();
                System.out.printf("Got table %s\n", table);
            }
        }

        @Override
        public void onFunction(CodeSectionReader reader) {
            while(reader.hasNext()){
                Function code = reader.read();
                System.out.printf("Got code %s\n", code);
            }
        }
    }
}
