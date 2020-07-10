package com.github.mikhasd.wasm.exec;

import com.github.mikhasd.wasm.gen.model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            while (true) {
                byte section = bb.get();
                int length = readUnsignedLeb128(bb);
                ByteBuffer sectionBuffer = bb.slice();
                Sections sections = Sections.valueOf(section);
                System.out.println("Parsing " + sections);
                switch (section) {
                    case Sections.TYPE_IDX:
                        pool.execute(() -> consumeTypeSection(sectionBuffer));
                        break;
                    case Sections.IMPORT_IDX:
                        pool.execute(() -> consumeImportSection(sectionBuffer));
                        break;
                    case Sections.FUNCTION_IDX:
                        pool.execute(() -> consumeFunctionSection(sectionBuffer));
                        break;
                    case Sections.TABLE_IDX:
                        pool.execute(() -> consumeTableSection(sectionBuffer));
                        break;
                    case Sections.MEMORY_IDX:
                        pool.execute(() -> consumeMemorySection(sectionBuffer));
                        break;
                    case Sections.GLOBAL_IDX:
                        pool.execute(() -> consumeGlobalSection(sectionBuffer));
                        break;
                    case Sections.EXPORT_IDX:
                        pool.execute(() -> consumeExportSection(sectionBuffer));
                        break;
                    case Sections.CODE_IDX:
                        pool.execute(() -> consumeCodeSection(sectionBuffer));
                        break;
                    case Sections.DATA_IDX:
                        pool.execute(() -> consumeDataSection(sectionBuffer));
                        break;
                    case Sections.CUSTOM_IDX:
                        pool.execute(() -> consumeCustomSection(sectionBuffer));
                        break;
                    default:
                        throw new UnsupportedOperationException("I don't know how to parse " + sections);
                }

                bb.position(bb.position() + length);
            }
        } finally {
            pool.shutdown();
        }


    }

    private TypesSection consumeTypeSection(ByteBuffer bb) {
        int typeCount = readUnsignedLeb128(bb);
        var functionTypes = new ArrayList<FunctionType>(typeCount);
        for (int i = 0; i < typeCount; i++) {
            synchronized (this) {
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
        }

        return new TypesSection(Vector.of(functionTypes));
    }

    private void printType(int type) {
        switch (type) {
            case Types.TYPE_INDEX_F32:
                System.out.print("\tTYPE_INDEX_F32");
                break;
            case Types.TYPE_INDEX_F64:
                System.out.print("\tTYPE_INDEX_F64");
                break;
            case Types.TYPE_INDEX_I32:
                System.out.print("\tTYPE_INDEX_I32");
                break;
            case Types.TYPE_INDEX_I64:
                System.out.print("\tTYPE_INDEX_I64");
                break;
            default:
                System.out.print("Unknown Type " + type);
        }
    }

    private void consumeCodeSection(ByteBuffer bb) {

    }

    private void consumeCustomSection(ByteBuffer bb) {
        String moduleName = readString(bb.slice());
        System.out.println("\tname: " + moduleName);
    }

    private void consumeDataSection(ByteBuffer bb) {
        int datas = readUnsignedLeb128(bb);
        for (int d = 0; d < datas; d++) {
            synchronized (this) {
                System.out.println("Data " + d);
                System.out.print("\t");
                int index = readUnsignedLeb128(bb);
                System.out.print(index);
                System.out.println();
                System.out.print('\t');
                byte instruction = bb.get();
                while (instruction != 0x0B) {
                    System.out.print(String.format("%02X ", instruction));
                    instruction = bb.get();
                }
                int dataLength = readUnsignedLeb128(bb);
                System.out.println("\tLength " + dataLength);
                bb.position(bb.position() + dataLength);
            }
        }
    }

    private void consumeGlobalSection(ByteBuffer bb) {
        int globals = readUnsignedLeb128(bb);

        for (int g = 0; g < globals; g++) {
            synchronized (this) {
                System.out.println("Global " + g);
                byte type = bb.get();
                byte mutability = bb.get();
                if (mutability == 0x00) {
                    System.out.print("\tconst\t");
                } else if (mutability == 0x01) {
                    System.out.print("\tvar\t");
                }
                printType(type);
                System.out.print("\n\t");
                byte instruction = bb.get();
                while (instruction != 0x0B) {
                    System.out.print(String.format("%02X ", instruction));
                    instruction = bb.get();
                }

                System.out.println();
            }
        }
    }

    private void consumeExportSection(ByteBuffer bb) {
        int exports = readUnsignedLeb128(bb);
        for (int e = 0; e < exports; e++) {
            synchronized (this) {
                System.out.println("Export " + e);
                String name = readString(bb);
                System.out.print("\t" + name);
                byte type = bb.get();
                switch (type) {
                    case 0x00:
                        System.out.print(" function ");
                        break;
                    case 0x01:
                        System.out.print(" table ");
                        break;
                    case 0x02:
                        System.out.print(" memory ");
                        break;
                    case 0x03:
                        System.out.print(" global ");
                        break;
                }

                int index = readUnsignedLeb128(bb);
                System.out.println(index);
            }
        }

    }

    private void consumeImportSection(ByteBuffer bb) {

        int importCount = readUnsignedLeb128(bb);
        for (int i = 0; i < importCount; i++) {
            synchronized (this) {
                System.out.println("Import " + i);
                String module = readString(bb);
                String name = readString(bb);
                System.out.println(module + "." + name);
                byte importType = bb.get();
                switch (importType) {
                    case 0x00:
                        System.out.println("\tFunction");
                        break;
                    case 0x01:
                        System.out.println("\tTable");
                        break;
                    case 0x02:
                        System.out.println("\tMemory");
                        break;
                    case 0x03:
                        System.out.println("\tGlobal");
                        break;
                }
                int index = readUnsignedLeb128(bb);
                System.out.print('\t');
                System.out.println(index);
            }
        }
    }

    private String readString(ByteBuffer bb) {
        int length = readUnsignedLeb128(bb);
        byte[] buffer = new byte[length];

        bb.get(buffer);

        return new String(buffer, StandardCharsets.UTF_8);

    }

    private void consumeFunctionSection(ByteBuffer bb) {
        int functions = readUnsignedLeb128(bb);
        for (int f = 0; f < functions; f++) {
            synchronized (this) {
                System.out.println("Function " + f);
                int index = readUnsignedLeb128(bb);
                System.out.println("\tIndex " + index);
            }
        }
    }

    private void consumeTableSection(ByteBuffer bb) {
        int tables = readUnsignedLeb128(bb);

        for (int t = 0; t < tables; t++) {
            synchronized (this) {
                System.out.println("Table " + t);
                if (bb.get() != 0x70)
                    throw new IllegalStateException("Expected Table Type");
                byte limitType = bb.get();
                boolean bounded = limitType == 0x01;
                if (bounded) {
                    int min = readUnsignedLeb128(bb);
                    int max = readUnsignedLeb128(bb);
                    System.out.println("\tMin " + min + " Max " + max);
                } else {
                    int min = readUnsignedLeb128(bb);
                    System.out.println("\tMin " + min);
                }
            }
        }
    }

    private void consumeMemorySection(ByteBuffer bb) {
        int memories = readUnsignedLeb128(bb);

        for (int m = 0; m < memories; m++) {
            synchronized (this) {
                System.out.println("Memory " + m);
                byte limitType = bb.get();
                boolean bounded = limitType == 0x01;
                if (bounded) {
                    int min = readUnsignedLeb128(bb);
                    int max = readUnsignedLeb128(bb);
                    System.out.println("\tMin " + min + " Max " + max);
                } else {
                    int min = readUnsignedLeb128(bb);
                    System.out.println("\tMin " + min);
                }
            }
        }
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
