package com.github.mikhasd.wasm.model;

import java.util.Arrays;

public interface TypeDefinition {

    class FunctionType implements TypeDefinition {
        Type[] parameters;
        Type[] returns;

        public FunctionType(Type[] parameterTypes, Type[] resultTypes) {
            this.parameters = parameterTypes;
            this.returns = resultTypes;
        }

        @Override
        public String toString() {
            return Arrays.toString(parameters) + ',' + Arrays.toString(returns);
        }

        public Type[] parameters() {
            return parameters;
        }

        public Type[] returns() {
            return returns;
        }
    }

    class ModuleType implements TypeDefinition {

    }

    default FunctionType asFunction() {
        return (FunctionType) this;
    }


}