package com.github.mikhasd.wasm.gen.model;

public class ImportSection extends VectorSection<Import> {

    public ImportSection(Vector<Import> items) {
        super(Sections.Import, items);
    }
}
