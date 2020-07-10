package com.github.mikhasd.wasm.gen.builder;

public class IndexSpace {
    private int currentIndex = 0;

    public synchronized int getNext(){
        return currentIndex++;
    }
}
