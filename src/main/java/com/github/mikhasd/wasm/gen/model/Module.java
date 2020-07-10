package com.github.mikhasd.wasm.gen.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Module implements CodeEmitter {

    private final static byte[] MAGIC = {0x00, 0x61, 0x73, 0x6D};
    private final static byte[] VERSION = {0x01, 0x00, 0x00, 0x00};
    public final List<Section> sections = new ArrayList<>();

    public Module(Section ... sections){
        for(Section section : sections){
            this.sections.add(section);
        }
    }

    @Override
    public void emitCode(CodeWriter output) {
        output.writeBytes(MAGIC);
        output.writeBytes(VERSION);
        this.sections.forEach(section -> {
            section.emitCode(output);
        });
    }
}
