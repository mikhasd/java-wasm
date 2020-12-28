package com.github.mikhasd.wasm.model;

public class Import {
    final String module;
    final String field;
    final ImportSectionEntryType importSectionEntry;

    public Import(String module, String field, ImportSectionEntryType importSectionEntry) {
        this.module = module;
        this.field = field;
        this.importSectionEntry = importSectionEntry;
    }

    @Override
    public String toString() {
        return
                module + '.' + field + ' ' + importSectionEntry;
    }
}
