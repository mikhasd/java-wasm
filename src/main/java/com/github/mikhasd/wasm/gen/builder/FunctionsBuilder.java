package com.github.mikhasd.wasm.gen.builder;

import com.github.mikhasd.wasm.model.*;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class FunctionsBuilder {

    private ModuleBuilder.TypeRegistry typeRegistry;
    private IndexSpace funcidx;

    private List<Function> functions = new LinkedList<>();

    FunctionsBuilder(ModuleBuilder.TypeRegistry typeRegistry, IndexSpace funcidx) {
        this.typeRegistry = typeRegistry;
        this.funcidx = funcidx;
    }

    public FunctionReference define(IndexVector parameters, IndexVector result, Consumer<FunctionBodyBuilder> functionBodyBuilder) {
        int typeIndex = typeRegistry.getTypeIndex(FunctionType.of(parameters, result));
        FunctionBodyBuilder bodyBuilder = new FunctionBodyBuilder();
        functionBodyBuilder.accept(bodyBuilder);
        Function function = new Function(typeIndex, bodyBuilder);
        this.functions.add(function);
        int index = this.funcidx.getNext();
        return new FunctionReference(index);
    }

    public FunctionReference define(Consumer<FunctionBodyBuilder> functionBodyBuilder) {
        return this.define(IndexVector.empty(), IndexVector.empty(), functionBodyBuilder);
    }

    public FunctionSection buildFunctionSection() {
        int[] types = this.functions.stream().mapToInt(Function::getTypeIndex).toArray();
        return new FunctionSection(IndexVector.of(types));
    }

    public CodeSection buildCodeSection() {
        List<Code> code = this.functions.stream().map(Function::toModel).map(Code::new).collect(toList());
        return new CodeSection(Vector.of(code));
    }

    private static class Function {
        private final int typeIndex;
        private final FunctionBodyBuilder body;

        private Function(int typeIndex, FunctionBodyBuilder body) {
            this.typeIndex = typeIndex;
            this.body = body;
        }

        int getTypeIndex() {
            return this.typeIndex;
        }

        public com.github.mikhasd.wasm.model.Function toModel() {
            byte[] opcodes = body.getOpCodes();
            return com.github.mikhasd.wasm.model.Function.create(IndexVector.empty(), opcodes);
        }
    }
}
