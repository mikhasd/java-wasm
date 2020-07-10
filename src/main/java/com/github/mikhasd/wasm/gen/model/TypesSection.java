package com.github.mikhasd.wasm.gen.model;

public class TypesSection extends VectorSection<FunctionType> {

    public TypesSection(Vector<FunctionType> items) {
        super(Sections.Type, items);
    }
}
