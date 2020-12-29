package com.github.mikhasd.wasm.eval;

import com.github.mikhasd.wasm.parse.Parser;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class EvaluationTest {

    public static void main(String[] args) throws IOException {
        Path wasmPath = Paths.get("/home/mikhas/Downloads/main.wasm");

        try (var channel = FileChannel.open(wasmPath, StandardOpenOption.READ)) {
            var buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            var handler = new ModuleBuildingHandler();
            new Parser().parse(buffer, handler);
            var module = handler.build();
            var instance = module.instantiate();
            WasmValue result = instance.call("do_the_magic", 20, 2);
            System.out.println(result.value());
        }
    }
}
