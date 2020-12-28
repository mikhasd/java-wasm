package com.github.mikhasd.wasm.model;

public abstract class ImportSectionEntryType {

    public boolean isA(Class<? extends ImportSectionEntryType> type){
        return type.isInstance(this);
    }

    public static class Function extends ImportSectionEntryType {
        final int index;

        public Function(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "Function{" +
                    "index=" + index +
                    '}';
        }
    }

    public static class Table extends ImportSectionEntryType {
        final Type type;
        final Limits limits;

        public Table(Type type, Limits limits) {
            this.type = type;
            this.limits = limits;
        }

        @Override
        public String toString() {
            return "Table{" +
                    "type=" + type +
                    ", limits=" + limits +
                    '}';
        }
    }

    public static class Memory extends ImportSectionEntryType {
        final Limits limits;

        public Memory(Limits limits) {
            this.limits = limits;
        }

        @Override
        public String toString() {
            return "Memory{" +
                    "limits=" + limits +
                    '}';
        }
    }

    public static class Global extends ImportSectionEntryType {
        final Type type;
        final boolean mutable;

        public Global(Type type, boolean mutable) {
            this.type = type;
            this.mutable = mutable;
        }

        @Override
        public String toString() {
            return "Global{" +
                    "type=" + type +
                    ", mutable=" + mutable +
                    '}';
        }
    }

    public static class Module extends ImportSectionEntryType {
        final int index;

        public Module(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "Module{" +
                    "index=" + index +
                    '}';
        }
    }

    public  static class Instance extends ImportSectionEntryType {
        final int index;

        public Instance(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "Instance{" +
                    "index=" + index +
                    '}';
        }
    }
}
