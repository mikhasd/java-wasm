package com.github.mikhasd.wasm.eval;

import com.github.mikhasd.wasm.WasmException;
import com.github.mikhasd.wasm.model.ExportSegment;
import com.github.mikhasd.wasm.model.ExternalKind;
import com.github.mikhasd.wasm.model.Type;

import java.util.List;
import java.util.Map;

public class Instance {
    private final Evaluator evaluator;
    private final List<Memory> memories;
    private final List<WasmValue> globals;
    private final List<Function> functions;
    private final Map<String, ExportSegment> exports;
    private WasmValue[] stack;




    public Instance(Evaluator evaluator, List<Memory> memories, List<WasmValue> globals, List<Function> functions, Map<String, ExportSegment> exports) {
        this.evaluator = evaluator;
        this.memories = memories;
        this.globals = globals;
        this.functions = functions;
        this.exports = exports;
    }

    public WasmValue call(String functionName, Object... args) {
        ExportSegment export = this.exports.get(functionName);
        if (export == null)
            throw new NotExportedException(functionName);

        if (!ExternalKind.Function.equals(export.type())) {
            throw new WasmException(functionName + " is not an exported function");
        }

        Function function = this.functions.get(export.index());

        Type[] parameters = function.type().parameters();
        var returnTypes = function.type().returns();
        final Type returnType;
        if (returnTypes.length == 0) {
            returnType = Type.Void;
        } else {
            returnType = returnTypes[0];
        }
        return evaluator.evaluate(function.opcodes(), parameters, args, returnType);
    }


}
