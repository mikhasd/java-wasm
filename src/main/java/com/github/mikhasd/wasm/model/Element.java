package com.github.mikhasd.wasm.model;

public class Element {

    public static final class Kind {
        private static Kind passive = new Kind(0, null);
        private static Kind declared = new Kind(0, null);
        private final int tableIndex;
        private final byte[] expression;

        Kind(int tableIndex, byte[] expression) {
            this.tableIndex = tableIndex;
            this.expression = expression;
        }

        public boolean isPassive(){
            return this == passive;
        }

        public boolean isDeclared(){
            return this == declared;
        }

        public static Kind active(int tableIndex, byte[] expression){
            return new Kind(tableIndex, expression);
        }

        public static Kind declared(){
            return declared;
        }

        public static Kind passive(){
            return passive;
        }
    }

    final Kind kind;
    final Type type;

    public Element(Kind kind, Type type) {
        this.kind = kind;
        this.type = type;
    }
}
