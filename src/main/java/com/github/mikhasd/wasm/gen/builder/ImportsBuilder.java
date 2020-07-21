package com.github.mikhasd.wasm.gen.builder;

import com.github.mikhasd.wasm.model.FunctionType;
import com.github.mikhasd.wasm.model.ImportSection;
import com.github.mikhasd.wasm.model.IndexVector;
import com.github.mikhasd.wasm.model.Vector;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ImportsBuilder {

    private final List<Import> imports = new LinkedList<>();
    private ModuleBuilder.TypeRegistry typeRegistry;
    private IndexSpace funcidx;

    ImportsBuilder(ModuleBuilder.TypeRegistry typeRegistry, IndexSpace funcidx) {
        this.typeRegistry = typeRegistry;
        this.funcidx = funcidx;
    }

    public FunctionReference function(String module, String name, IndexVector parameters, IndexVector result) {
        int typeIndex = this.typeRegistry.getTypeIndex(FunctionType.of(parameters, result));
        Import impt = new Import(module, name, com.github.mikhasd.wasm.model.Import.TYPE_FUNCTION, typeIndex);
        this.imports.add(impt);
        int index = funcidx.getNext();
        return new FunctionReference(index);
    }

    public FunctionReference function(String module, String name, IndexVector parameters) {
        return this.function(module, name, parameters, IndexVector.empty());
    }

    public FunctionReference function(String module, String name) {
        return this.function(module, name, IndexVector.empty(), IndexVector.empty());
    }

    public ImportSection buildSection() {
        List<com.github.mikhasd.wasm.model.Import> imports = this.imports.stream().map(Import::getModel).collect(toList());
        return new ImportSection(Vector.of(imports));
    }

    private static class Import {
        private final String module;
        private final String name;
        private final byte importType;
        private final int typeIndex;

        protected Import(String module, String name, byte importType, int typeIndex) {
            this.module = module;
            this.name = name;
            this.importType = importType;
            this.typeIndex = typeIndex;
        }

        public com.github.mikhasd.wasm.model.Import getModel() {
            return new com.github.mikhasd.wasm.model.Import(module, name, importType, typeIndex);
        }
    }

}
