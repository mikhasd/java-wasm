package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.WasmReader;

public abstract class BaseSectionReader<T> {

    private final int count;
    private final int length;
    protected final WasmReader file;
    private final int start;
    private int current = 0;


    protected BaseSectionReader(WasmReader file, int length) {
        this.file = file;
        this.start = file.position();
        this.count = file.readUnsignedLeb128();
        this.length = length;
    }

    public final boolean hasNext() {
        return current < count;
    }

    public final T read() {
        T one = this.readOne();
        current++;
        return one;
    }

    public void drain(){
        var end = this.start + this.length;
        var diff = end - file.position();
        if(diff > 0)
            file.skip(diff);
    }

    protected abstract T readOne();

    public abstract void handle(Handler handler);

}
