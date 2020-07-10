package com.github.mikhasd.wasm.gen.builder;

import com.github.mikhasd.wasm.gen.model.ExportSection;
import com.github.mikhasd.wasm.gen.model.Vector;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ExportsBuilder {
    private final List<Export> exports = new LinkedList<>();

    public void export(String name, FunctionReference.Holder functionReference) {
        exports.add(new Export(name, ExportType.Function, functionReference.getIndex()));
    }

    ExportSection buildSection() {
        List<com.github.mikhasd.wasm.gen.model.Export> exports = this.exports.stream().map(Export::getModel).collect(toList());
        ExportSection section = new ExportSection(Vector.of(exports));
        return section;
    }

    private enum ExportType {
        Function {
            @Override
            com.github.mikhasd.wasm.gen.model.Export getModel(ExportsBuilder.Export export) {
                return com.github.mikhasd.wasm.gen.model.Export.function(export.name, export.index);
            }
        },
        Table {
            @Override
            com.github.mikhasd.wasm.gen.model.Export getModel(ExportsBuilder.Export export) {
                return com.github.mikhasd.wasm.gen.model.Export.table(export.name, export.index);
            }
        },
        Memory {
            @Override
            com.github.mikhasd.wasm.gen.model.Export getModel(ExportsBuilder.Export export) {
                return com.github.mikhasd.wasm.gen.model.Export.memory(export.name, export.index);
            }
        },
        Global {
            @Override
            com.github.mikhasd.wasm.gen.model.Export getModel(ExportsBuilder.Export export) {
                return com.github.mikhasd.wasm.gen.model.Export.global(export.name, export.index);
            }
        };

        abstract com.github.mikhasd.wasm.gen.model.Export getModel(ExportsBuilder.Export export);
    }

    private static class Export {
        private final String name;
        private final ExportType exportType;
        private final int index;

        protected Export(String name, ExportType exportType, int index) {
            this.name = name;
            this.exportType = exportType;
            this.index = index;
        }

        com.github.mikhasd.wasm.gen.model.Export getModel() {
            return this.exportType.getModel(this);
        }
    }
}
