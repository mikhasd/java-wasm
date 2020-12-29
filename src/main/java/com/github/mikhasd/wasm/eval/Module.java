package com.github.mikhasd.wasm.eval;

import com.github.mikhasd.wasm.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Module {
    private final List<MemorySegment> memories;
    private final List<Global> globals;
    private final List<DataSegment> datas;
    private final List<ElementSegment> elements;
    private final List<TypeDefinition> types;
    private final List<ExportSegment> exportSegments;
    private final List<Integer> functionTypes;
    private final List<FunctionSegment> functionSegments;

    public Module(List<MemorySegment> memories, List<Global> globals, List<DataSegment> datas, List<ElementSegment> elements, List<TypeDefinition> types, List<ExportSegment> exportSegments, List<Integer> functionTypes, List<FunctionSegment> functionSegments) {
        this.memories = memories;
        this.globals = globals;
        this.datas = datas;
        this.elements = elements;
        this.types = types;
        this.exportSegments = exportSegments;
        this.functionTypes = functionTypes;
        this.functionSegments = functionSegments;
    }

    public Instance instantiate() {
        Evaluator evaluator = new Evaluator();

        var memories = new ArrayList<Memory>(this.memories.size());
        for(var memory : this.memories) {
            var mem = new Memory(memory.limits());
            memories.add(mem);
        }

        var globals = new ArrayList<WasmValue>(this.globals.size());
        for (var global : this.globals) {
            WasmValue result = evaluator.evaluate(global.expression(), new Type[0], null, global.type());
            globals.add(result);
        }

        for(var data : this.datas){
            byte[] bytes = data.getData();
            byte[] expression = data.getExpression();
            int memoryIdx = data.getMemoryIdx();
            var index = evaluator.evaluate(expression, new Type[0], null,Type.I32);
            var memory = memories.get(memoryIdx);
            memory.copy(bytes, (Integer)index.value());
        }

        var functions = new ArrayList<Function>(functionTypes.size());
        for(var fnIdx = 0; fnIdx < functionTypes.size(); fnIdx++){
            var typeidx = functionTypes.get(fnIdx);
            var type = types.get(typeidx);
            var code = functionSegments.get(fnIdx);
            var function = new Function(type.asFunction(), code.getLocals(), code.opcodes());
            functions.add(function);
        }

        var exports  = new HashMap<String, ExportSegment>(this.exportSegments.size());
        for (var export : this.exportSegments) {
            var name = export.name();
            exports.put(name, export);
        }


        return new Instance(evaluator, memories, globals, functions, exports);
    }
}
