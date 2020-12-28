package com.github.mikhasd.wasm.model;

public enum ExternalKind {
    Function,
    Table,
    Memory,
    Global,
    Type,
    Module,
    Instance;

    public static ExternalKind valueOf(byte code){
        switch(code){
            case 0: return ExternalKind.Function;
            case 1: return ExternalKind.Table;
            case 2: return ExternalKind.Memory;
            case 3: return ExternalKind.Global;
            case 5: return ExternalKind.Type;
            case 6: return ExternalKind.Module;
            case 7: return ExternalKind.Instance;
        }
        throw new IllegalArgumentException(String.format("Invalid external kind: 0x%02X", code));
    }
}
