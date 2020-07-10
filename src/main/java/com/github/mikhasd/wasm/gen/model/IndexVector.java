package com.github.mikhasd.wasm.gen.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

import java.util.Arrays;

public class IndexVector implements CodeEmitter {

    private static final IndexVector EMPTY = new IndexVector(new int[0]){
        @Override
        public boolean equals(Object o) {
            return o == this;
        }

        @Override
        public void emitCode(CodeWriter output) {
            output.writeInteger(0);
        }
    };

    private final int[] indexes;

    private IndexVector(int[] indexes) {
        this.indexes = indexes;
    }

    @Override
    public void emitCode(CodeWriter output) {
        output.writeInteger(indexes.length);
        for (int i : indexes) {
            output.writeInteger(i);
        }
    }

    public static IndexVector of(int... indexes) {
        int copy[] = new int[indexes.length];
        System.arraycopy(indexes, 0, copy, 0, indexes.length);
        return new IndexVector(copy);
    }

    public static IndexVector indexes(int ... indexes){
        return of(indexes);
    }

    public static IndexVector index(int i){
        return of(i);
    }

    public static IndexVector empty() {
        return EMPTY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexVector that = (IndexVector) o;
        return Arrays.equals(indexes, that.indexes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(indexes);
    }
}
