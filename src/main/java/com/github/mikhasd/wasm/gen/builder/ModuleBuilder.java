package com.github.mikhasd.wasm.gen.builder;

import com.github.mikhasd.wasm.gen.model.Module;
import com.github.mikhasd.wasm.gen.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModuleBuilder {

    private final TypeRegistry typeRegistry = new TypeRegistry();
    private final IndexSpace funcidx = new IndexSpace();

    private final ImportsBuilder importsBuilder = new ImportsBuilder(typeRegistry, funcidx);
    private final FunctionsBuilder functionsBuilder = new FunctionsBuilder(typeRegistry, funcidx);
    private final ExportsBuilder exportsBuilder = new ExportsBuilder();


    public final ModuleBuilder imports(Consumer<ImportsBuilder> imports) {
        imports.accept(this.importsBuilder);
        return this;
    }

    public final ModuleBuilder functions(Consumer<FunctionsBuilder> functions) {
        functions.accept(functionsBuilder);
        return this;
    }

    public final ModuleBuilder exports(Consumer<ExportsBuilder> exports) {
        exports.accept(this.exportsBuilder);
        return this;
    }

    public Module build() {
        TypesSection typesSection = createTypeSection();
        ExportSection exportSection = exportsBuilder.buildSection();
        ImportSection importSection = importsBuilder.buildSection();
        FunctionSection functionSection = functionsBuilder.buildFunctionSection();
        CodeSection codeSection = functionsBuilder.buildCodeSection();
        return new Module(typesSection, importSection, functionSection, exportSection, codeSection);
    }

    private TypesSection createTypeSection() {
        return new TypesSection(Vector.of(this.typeRegistry.knownFunctionTypes));
    }

    static class TypeRegistry {
        private final List<FunctionType> knownFunctionTypes;

        private TypeRegistry() {
            knownFunctionTypes = new ArrayList<>();
        }

        public int getTypeIndex(FunctionType type) {
            int i = this.knownFunctionTypes.indexOf(type);
            if (i < 0) {
                this.knownFunctionTypes.add(type);
                return this.knownFunctionTypes.size() - 1;
            } else {
                return i;
            }
        }
    }
}
