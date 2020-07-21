package com.github.mikhasd.wasm;

import com.github.mikhasd.wasm.exec.ModuleFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ModuleParseTest {

    public static void main(String... args) throws IOException, URISyntaxException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("main.wasm");
        Path wasmPath = Paths.get(resource.toURI());

        try (var channel = FileChannel.open(wasmPath, StandardOpenOption.READ)) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            new ModuleFactory().createModule(buffer);
        }

    }
}
