package com.github.mikhasd.wasm.model;

import com.github.mikhasd.wasm.gen.CodeEmitter;
import com.github.mikhasd.wasm.gen.CodeWriter;

import java.util.Objects;

public class FunctionType implements CodeEmitter {

    private IndexVector parameters;
    private IndexVector result;

    private FunctionType(IndexVector parameters, IndexVector result) {
        this.parameters = parameters;
        this.result = result;
    }

    @Override
    public void emitCode(CodeWriter output) {
        output.writeInteger(Types.TYPE_FUNCTION);
        parameters.emitCode(output);
        result.emitCode(output);
    }

    public static FunctionType of(IndexVector parameters, IndexVector result){
        return new FunctionType(parameters,result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionType that = (FunctionType) o;
        return Objects.equals(parameters, that.parameters) &&
                Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameters, result);
    }
}
