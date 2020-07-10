package com.github.mikhasd.wasm.gen.builder;

public class FunctionReference {
    private int index;

    FunctionReference(int index) {
        this.index = index;
    }

    int getIndex() {
        return index;
    }

    public static class Holder {
        private FunctionReference functionReference;

        public void set(FunctionReference functionReference){
            this.functionReference = functionReference;
        }

        int getIndex() {
            return this.functionReference.index;
        }
    }
}
