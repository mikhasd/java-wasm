package com.github.mikhasd.wasm.exec;

import com.github.mikhasd.wasm.gen.model.*;
import com.github.mikhasd.wasm.model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class ModuleFactory {

    private final static byte[] MAGIC = {0x00, 0x61, 0x73, 0x6D};
    private final static byte[] VERSION = {0x01, 0x00, 0x00, 0x00};

    public static void main(String... args) throws IOException, URISyntaxException {


        URL resource = Thread.currentThread().getContextClassLoader().getResource("main.wasm");
        Path wasmPath = Paths.get(resource.toURI());

        try (var channel = FileChannel.open(wasmPath, StandardOpenOption.READ)) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            new ModuleFactory().createModule(buffer);
        }

    }

    static int readUnsignedLeb128(ByteBuffer bb) {

        int result = 0;
        int cur;
        int count = 0;
        do {
            cur = bb.get() & 0xff;
            result |= (cur & 0x7f) << (count * 7);
            count++;
        } while (((cur & 0x80) == 0x80) && count < 5);
        if ((cur & 0x80) == 0x80) {
            throw new IllegalArgumentException("invalid LEB128 sequence");
        }
        return result;
    }

    public void createModule(ByteBuffer bb) {
        if (!checkMagic(bb) || !checkVersion(bb)) {
            throw new IllegalArgumentException("Invalid WASM file");
        }
        try {
            while (true) {
                byte section = bb.get();
                int length = readUnsignedLeb128(bb);
                ByteBuffer sectionBuffer = bb.slice().limit(length);
                Sections sections = Sections.valueOf(section);
                System.out.println("Parsing " + sections);
                switch (section) {
                    case Sections.TYPE_IDX:
                        consumeTypeSection(sectionBuffer);
                        break;
                    case Sections.IMPORT_IDX:
                        consumeImportSection(sectionBuffer);
                        break;
                    case Sections.FUNCTION_IDX:
                        consumeFunctionSection(sectionBuffer);
                        break;
                    case Sections.TABLE_IDX:
                        consumeTableSection(sectionBuffer);
                        break;
                    case Sections.MEMORY_IDX:
                        consumeMemorySection(sectionBuffer);
                        break;
                    case Sections.GLOBAL_IDX:
                        consumeGlobalSection(sectionBuffer);
                        break;
                    case Sections.EXPORT_IDX:
                        consumeExportSection(sectionBuffer);
                        break;
                    case Sections.CODE_IDX:
                        consumeCodeSection(sectionBuffer);
                        break;
                    case Sections.DATA_IDX:
                        consumeDataSection(sectionBuffer);
                        break;
                    case Sections.CUSTOM_IDX:
                        consumeCustomSection(sectionBuffer);
                        break;
                    default:
                        throw new UnsupportedOperationException("I don't know how to parse " + sections);
                }

                bb.position(bb.position() + length);
            }
        } catch (BufferUnderflowException buex) {
            if (bb.position() != bb.limit()) {
                throw new IllegalArgumentException("Error while parsing WASM file");
            }
        }

    }

    private TypesSection consumeTypeSection(ByteBuffer bb) {
        int typeCount = readUnsignedLeb128(bb);
        var functionTypes = new ArrayList<FunctionType>(typeCount);
        for (int i = 0; i < typeCount; i++) {
            byte fnType = bb.get();
            if (fnType != Types.TYPE_FUNCTION)
                throw new IllegalArgumentException(String.format("Expected Function Type %02X", fnType));
            int parameterCount = readUnsignedLeb128(bb);
            int[] parameterTypes = new int[parameterCount];
            for (int j = 0; j < parameterCount; j++) {
                int type = readUnsignedLeb128(bb);
                parameterTypes[j] = type;
            }
            int resultCount = readUnsignedLeb128(bb);
            int[] resultTypes = new int[resultCount];
            for (int j = 0; j < resultCount; j++) {
                int type = readUnsignedLeb128(bb);
                resultTypes[j] = type;
            }
            var functionType = FunctionType.of(IndexVector.of(parameterTypes), IndexVector.of(resultTypes));
            functionTypes.add(functionType);

        }

        return new TypesSection(Vector.of(functionTypes));
    }

    private CodeSection consumeCodeSection(ByteBuffer bb) {
        var count = readUnsignedLeb128(bb);
        var functionsCode = new ArrayList<Code>(count);
        for (var i = 0; i < count; i++) {
            final var functionLength = readUnsignedLeb128(bb);
            final var localsStart = bb.position();

            final var localsCount = readUnsignedLeb128(bb);
            final var locals = new int[localsCount];
            for (var l = 0; l < localsCount; l++) {
                locals[l] = readUnsignedLeb128(bb);
            }

            final var localsEnd = bb.position();
            final var localsLength = localsEnd - localsStart;

            final var bytecodeLength = functionLength - localsLength;
            final var bytecodeBuffer = new byte[bytecodeLength];
            bb.get(bytecodeBuffer);
            var code = new Code(new Function(IndexVector.of(locals), bytecodeBuffer));
            functionsCode.add(code);
        }
        return new CodeSection(Vector.of(functionsCode));
    }

    private void consumeCustomSection(ByteBuffer bb) {
        String moduleName = readString(bb.slice());
        System.err.println("Custom section parsing is not yet implemented: " + moduleName);
    }

    private DataSection consumeDataSection(ByteBuffer bb) {
        var count = readUnsignedLeb128(bb);
        var datas = new ArrayList<Data>(count);
        for (var i = 0; i < count; i++) {
            var index = readUnsignedLeb128(bb);
            var expression = new ArrayList<Byte>();
            byte instruction;
            do {
                instruction = bb.get();
                expression.add(instruction);
            } while (instruction != 0x0B);
            var expressionBuffer = new byte[expression.size()];
            for (int j = 0; j < expression.size(); j++) {
                expressionBuffer[j] = expression.get(j);
            }
            var dataLength = readUnsignedLeb128(bb);
            var dataBuffer = new byte[dataLength];
            bb.get(dataBuffer);

            var data = new Data(index, expressionBuffer, dataBuffer);
            datas.add(data);
        }

        return new DataSection(Vector.of(datas));
    }

    private GlobalSections consumeGlobalSection(ByteBuffer bb) {
        var count = readUnsignedLeb128(bb);
        var globals = new ArrayList<Global>(count);
        for (var i = 0; i < count; i++) {
            var type = bb.get();
            var mutability = bb.get();
            byte instruction;
            var instructionsList = new ArrayList<Byte>();
            do {
                instruction = bb.get();
                instructionsList.add(instruction);
            } while (instruction != 0x0B);
            var expressionBuffer = new byte[instructionsList.size()];
            for (int j = 0; j < expressionBuffer.length; j++) {
                expressionBuffer[j] = instructionsList.get(j);
            }

            var global = new Global(type, mutability, expressionBuffer);
            globals.add(global);
        }

        return new GlobalSections(Vector.of(globals));
    }

    private ExportSection consumeExportSection(ByteBuffer bb) {
        var count = readUnsignedLeb128(bb);
        var exports = new ArrayList<Export>(count);
        for (var e = 0; e < count; e++) {
            String name = readString(bb);
            var type = bb.get();
            var index = readUnsignedLeb128(bb);
            var export = new Export(name, type, index);
            exports.add(export);
        }

        return new ExportSection(Vector.of(exports));
    }

    private ImportSection consumeImportSection(ByteBuffer bb) {
        var importCount = readUnsignedLeb128(bb);
        var imports = new ArrayList<Import>(importCount);
        for (int i = 0; i < importCount; i++) {
            var module = readString(bb);
            var name = readString(bb);
            var importType = bb.get();
            var index = readUnsignedLeb128(bb);
            var imp = new Import(module, name, importType, index);
            imports.add(imp);
        }
        return new ImportSection(Vector.of(imports));
    }

    private String readString(ByteBuffer bb) {
        int length = readUnsignedLeb128(bb);
        byte[] buffer = new byte[length];

        bb.get(buffer);

        return new String(buffer, StandardCharsets.UTF_8);

    }

    private FunctionSection consumeFunctionSection(ByteBuffer bb) {
        var functions = readUnsignedLeb128(bb);
        var functionIndexes = new int[functions];
        for (var f = 0; f < functions; f++) {
            var index = readUnsignedLeb128(bb);
            functionIndexes[f] = index;
        }
        return new FunctionSection(IndexVector.of(functionIndexes));
    }

    private TableSection consumeTableSection(ByteBuffer bb) {
        var count = readUnsignedLeb128(bb);
        var tables = new ArrayList<Table>(count);
        for (var i = 0; i < count; i++) {
            if (bb.get() != 0x70)
                throw new IllegalStateException("Expected Table Type");
            var limitType = bb.get();
            var bounded = limitType == 0x01;
            var min = readUnsignedLeb128(bb);
            final Table table;
            if (bounded) {
                var max = readUnsignedLeb128(bb);
                table = new Table(min, max);
            } else {
                table = new Table(min);
            }
            tables.add(table);
        }

        return new TableSection(Vector.of(tables));
    }

    private MemorySection consumeMemorySection(ByteBuffer bb) {
        var count = readUnsignedLeb128(bb);
        var memories = new ArrayList<Memory>(count);
        for (var i = 0; i < count; i++) {
            var limitType = bb.get();
            var bounded = limitType == 0x01;
            final Memory memory;
            var min = readUnsignedLeb128(bb);
            if (bounded) {
                var max = readUnsignedLeb128(bb);
                memory = new Memory(min, max);

            } else {
                memory = new Memory(min);
            }
            memories.add(memory);
        }

        return new MemorySection(Vector.of(memories));
    }

    private boolean checkVersion(ByteBuffer bb) {

        return bb.get() == VERSION[0] &&
                bb.get() == VERSION[1] &&
                bb.get() == VERSION[2] &&
                bb.get() == VERSION[3];
    }

    private boolean checkMagic(ByteBuffer bb) {
        return bb.get() == MAGIC[0] &&
                bb.get() == MAGIC[1] &&
                bb.get() == MAGIC[2] &&
                bb.get() == MAGIC[3];
    }
}
