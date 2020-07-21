package com.github.mikhasd.wasm;

import com.github.mikhasd.wasm.gen.CodeWriter;
import com.github.mikhasd.wasm.gen.OutputStreamCodeWriter;
import com.github.mikhasd.wasm.model.Module;
import com.github.mikhasd.wasm.model.*;

import java.io.IOException;
import java.io.OutputStream;

public class TestGen {

    public static void main(String... args) {
        CodeWriter writer = new OutputStreamCodeWriter(new PrintingBytesOutput());

        TypesSection typesSection = new TypesSection(Vector.of(FunctionType.of(IndexVector.of(Types.TYPE_INDEX_I32), IndexVector.of(Types.TYPE_INDEX_I32))));
        FunctionSection functionSection = new FunctionSection(IndexVector.of(0));
        ExportSection exportSection = new ExportSection(Vector.of(Export.function("addOne", 0)));
        CodeSection codeSection = new CodeSection(Vector.of(new Code(Function.create(IndexVector.empty(), new byte[]{
                0x20,
                0x00,
                0x41,
                0x01,
                0x6A,
                0x0B
        }))));

        Module module = new Module(typesSection, functionSection, exportSection, codeSection);
        module.emitCode(writer);
    }

    public static class PrintingBytesOutput extends OutputStream {

        private long byteCount = 0;


        public void write(byte b) {
            String str = Integer.toString(b, 16);
            if (str.length() == 1) {
                System.out.print(0);
            }
            System.out.print(str);
            if ((++byteCount) % 16 == 0) {
                System.out.println();
            } else {
                System.out.print(' ');
            }
        }

        @Override
        public void write(int b) throws IOException {
            this.write((byte) b);
        }

        @Override
        public void write(byte[] bytes) {
            for (byte b : bytes) {
                this.write(b);
            }
        }
    }
}
