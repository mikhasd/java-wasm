package com.github.mikhasd.wasm.parse.section;

import com.github.mikhasd.wasm.parse.Handler;
import com.github.mikhasd.wasm.parse.WasmReader;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class BaseSectionReader<T> implements Iterable<T> {

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

    public void drain(){
        var end = this.start + this.length;
        var diff = end - file.position();
        if(diff > 0)
            file.skip(diff);
    }

    private T readInternal(){
        T one = read();
        current++;
        return one;
    }

    protected abstract T read();

    public abstract void handle(Handler handler);

    @Override
    public Iterator<T> iterator() {
        return new SectionElementIterator();
    }

    public Stream<T> stream(){
        var spliterator = Spliterators.spliterator(this.iterator(), this.count, 0);
        return StreamSupport.stream(spliterator, false);
    }

    public List<T> toList(){
        var list = new ArrayList<T>(this.count);
        for (var i = 0; i < this.count; i++){
            list.add(readInternal());
        }
        return list;
    }

    public class SectionElementIterator implements Iterator<T> {

        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < count;
        }

        @Override
        public T next() {
            return readInternal();
        }
    }
}
