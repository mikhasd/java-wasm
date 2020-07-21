package com.github.mikhasd.wasm.gen.builder;

import com.github.mikhasd.wasm.model.ExportSection;
import com.github.mikhasd.wasm.model.Vector;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ExportsBuilder {
    private final List<Export> exports = new LinkedList<>();

    public void export(String name, FunctionReference.Holder functionReference) {
        exports.add(new Export(name, com.github.mikhasd.wasm.model.Export.TYPE_FUNCTION, functionReference.getIndex()));
    }

    ExportSection buildSection() {
        List<com.github.mikhasd.wasm.model.Export> exports = this.exports.stream().map(Export::getModel).collect(toList());
        ExportSection section = new ExportSection(Vector.of(exports));
        return section;
    }

    private static class Export {
        private final String name;
        private final byte exportType;
        private final int index;

        protected Export(String name, byte exportType, int index) {
            this.name = name;
            this.exportType = exportType;
            this.index = index;
        }

        com.github.mikhasd.wasm.model.Export getModel() {
            return new com.github.mikhasd.wasm.model.Export(name, exportType, index);
        }
    }
}
