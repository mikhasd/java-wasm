package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vector<T extends CodeEmitter> implements CodeEmitter {

    private final List<T> items;

    private Vector(List<T> items) {
        this.items = items;
    }

    @Override
    public void emitCode(CodeWriter output) {
        output.writeInteger(this.items.size());
        for(CodeEmitter item : items){
            item.emitCode(output);
        }
    }

    public static <T extends CodeEmitter> Vector<T> of(T ... items){
        return new Vector<>(Arrays.asList(items));
    }

    public static <T extends CodeEmitter> Vector<T> of(List<T> items){
        return new Vector<>(new ArrayList<>(items));
    }
}
