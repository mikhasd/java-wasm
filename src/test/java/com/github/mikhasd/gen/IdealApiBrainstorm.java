package com.github.mikhasd.gen;

import com.github.mikhasd.wasm.gen.CodeWriter;
import com.github.mikhasd.wasm.gen.OutputStreamCodeWriter;
import com.github.mikhasd.wasm.gen.builder.FunctionReference;
import com.github.mikhasd.wasm.gen.builder.ModuleBuilder;
import com.github.mikhasd.wasm.model.Module;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.github.mikhasd.wasm.model.IndexVector.index;
import static com.github.mikhasd.wasm.model.Types.TYPE_INDEX_I32;

public class IdealApiBrainstorm {
    public static void main(String... args) throws IOException {

        FunctionReference.Holder fnEnvAdd = new FunctionReference.Holder();
        FunctionReference.Holder fnAddOne = new FunctionReference.Holder();

        Module module = new ModuleBuilder().
                imports(imports -> {
                    fnEnvAdd.set(imports.function("env", "add", index(TYPE_INDEX_I32), index(TYPE_INDEX_I32)));
                }).
                functions(functionProducer -> {
                    FunctionReference subFn = functionProducer.define(index(TYPE_INDEX_I32), index(TYPE_INDEX_I32),
                            bodyProducer -> {
                                bodyProducer
                                        .getLocal(0)
                                        .i32Const(-1)
                                        .i32Add();
                            });
                    fnAddOne.set(functionProducer.define(index(TYPE_INDEX_I32), index(TYPE_INDEX_I32),
                            bodyProducer -> {
                                bodyProducer
                                        .getLocal(0)
                                        .call(subFn)
                                        .getLocal(0)
                                        .i32Add()
                                        .getLocal(0)
                                        .call(fnEnvAdd)
                                        .i32Add();
                            }));
                })
                .exports(exportsBuilder -> {
                    exportsBuilder.export("addOne", fnAddOne);
                })
                .build();
        FileOutputStream output = new FileOutputStream("C:\\dev\\test.wasm");
        CodeWriter writer = new OutputStreamCodeWriter(output);
        module.emitCode(writer);
        output.close();
    }

    public static class PrintingBytesOutput extends OutputStream {

        private long byteCount = 0;


        public void write(byte b) {
            String str = Integer.toHexString(b);
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
