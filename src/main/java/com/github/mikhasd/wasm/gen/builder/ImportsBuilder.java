package com.github.mikhasd.wasm.gen.builder;

import com.github.mikhasd.wasm.gen.model.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class ImportsBuilder {

    private ModuleBuilder.TypeRegistry typeRegistry;
    private IndexSpace funcidx;

    private final List<Import> imports = new LinkedList<>();

    ImportsBuilder(ModuleBuilder.TypeRegistry typeRegistry, IndexSpace funcidx) {
        this.typeRegistry = typeRegistry;
        this.funcidx = funcidx;
    }

    public FunctionReference function(String module, String name, IndexVector parameters, IndexVector result) {
        int typeIndex = this.typeRegistry.getTypeIndex(FunctionType.of(parameters, result));
        Import impt = new Import(module, name, ImportType.Function, typeIndex);
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
        List<com.github.mikhasd.wasm.gen.model.Import> imports = this.imports.stream().map(Import::getModel).collect(toList());
        return new ImportSection(Vector.of(imports));
    }

    private enum ImportType {
        Function{
            @Override
            com.github.mikhasd.wasm.gen.model.Import getMode(ImportsBuilder.Import anImport) {
                return com.github.mikhasd.wasm.gen.model.Import.function(anImport.module, anImport.name, anImport.typeIndex);
            }
        },
        Table{
            @Override
            com.github.mikhasd.wasm.gen.model.Import getMode(ImportsBuilder.Import anImport) {
                return com.github.mikhasd.wasm.gen.model.Import.table(anImport.module, anImport.name, anImport.typeIndex);
            }
        },
        Memory{
            @Override
            com.github.mikhasd.wasm.gen.model.Import getMode(ImportsBuilder.Import anImport) {
                return com.github.mikhasd.wasm.gen.model.Import.memory(anImport.module, anImport.name, anImport.typeIndex);
            }
        },
        Global{
            @Override
            com.github.mikhasd.wasm.gen.model.Import getMode(ImportsBuilder.Import anImport) {
                return com.github.mikhasd.wasm.gen.model.Import.global(anImport.module, anImport.name, anImport.typeIndex);
            }
        };


        abstract com.github.mikhasd.wasm.gen.model.Import getMode(ImportsBuilder.Import anImport);
    }

    private static class Import {
        private final String module;
        private final String name;
        private final ImportType importType;
        private final int typeIndex;

        protected Import(String module, String name, ImportType importType, int typeIndex) {
            this.module = module;
            this.name = name;
            this.importType = importType;
            this.typeIndex = typeIndex;
        }

        public com.github.mikhasd.wasm.gen.model.Import getModel() {
            return this.importType.getMode(this);
        }
    }

}
