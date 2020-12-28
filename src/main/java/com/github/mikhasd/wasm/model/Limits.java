package com.github.mikhasd.wasm.model;

public final class Limits {
    private final int initial;
    private final int maximum;

    public Limits(int initial, int maximum) {
        this.initial = initial;
        this.maximum = maximum;
    }

    public Limits(int initial) {
        this.initial = initial;
        this.maximum = -1;
    }


    @Override
    public String toString() {
        if(maximum >= 0){
            return "{" +
                    "initial=" + initial +
                    ", maximum=" + maximum +
                    '}';
        } else {
            return "{" +
                    "initial=" + initial +
                    '}';
        }

    }
}
