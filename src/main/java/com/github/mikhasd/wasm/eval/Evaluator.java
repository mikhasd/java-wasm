package com.github.mikhasd.wasm.eval;

import com.github.mikhasd.wasm.WasmRuntimeException;
import com.github.mikhasd.wasm.model.Type;
import com.github.mikhasd.wasm.parse.ParseException;

import java.util.Stack;

public class Evaluator {

    private int instructionPointer;

    public WasmValue evaluate(byte[] opcodes, Type[] parameterTypes, Object[] args, Type returnType) {
        instructionPointer = 0;
        WasmValue[] locals = toLocals(parameterTypes, args);
        Stack<WasmValue> stack = new Stack<>();
        execution:
        while (instructionPointer < opcodes.length) {
            var opcode = opcodes[instructionPointer++];


            switch (opcode) {
                // block
                case 0x02: {
                    var blockType = opcodes[instructionPointer++];
                }
                break;

                // end
                case 0x0B:
                    break;
                // br_if
                case 0x0D: {
                    var val = stack.pop();
                    if (!val.type().equals(Type.I32))
                        throw new WasmRuntimeException("0x0D:br_if expects I32 in the top of the stack but got " + val.type());
                    var label = readUnsignedLeb128(opcodes);
                    if (val.value().equals(1)) {
                        instructionPointer = label;
                    }
                    break;
                }
                // return
                case 0x0F: {
                    break execution;
                }

                // local.get
                case 0x20:
                    var local = readUnsignedLeb128(opcodes);
                    stack.push(locals[local]);
                    break;
                // i32.const
                case 0x41: {
                    var val = readUnsignedLeb128(opcodes);
                    var currentValue = new WasmValue<>(Type.I32, val);
                    stack.push(currentValue);
                }
                break;
                // i32.lt_s
                case 0x48: {
                    WasmValue<Integer> a = stack.pop();
                    WasmValue<Integer> b = stack.pop();
                    Integer d = a.value() < b.value() ? 1 : 0;
                    stack.push(new WasmValue(Type.I32, d));
                }
                break;
                // i32.add
                case 0x6A: {
                    WasmValue<Integer> a = stack.pop();
                    WasmValue<Integer> b = stack.pop();
                    Integer d = a.value() + b.value();
                    stack.push(new WasmValue(Type.I32, d));
                    break;
                }
                // i32.mul
                case 0x6C: {
                    WasmValue<Integer> a = stack.pop();
                    WasmValue<Integer> b = stack.pop();
                    Integer d = a.value() * b.value();
                    stack.push(new WasmValue(Type.I32, d));
                    break;
                }
                // i32.div_u
                case 0x6E: {
                    WasmValue<Integer> a = stack.pop();
                    WasmValue<Integer> b = stack.pop();
                    Integer d = Integer.divideUnsigned(a.value(), b.value());
                    stack.push(new WasmValue(Type.I32, d));
                    break;
                }
                default:
                    throw new EvaluationException(String.format("unsupported opcode 0x%02X", opcode));
            }
        }

        var result = stack.pop();

        if (!result.type().equals(returnType)) {
            throw new EvaluationException(String.format("incorrect return type. Expected: %s; got: %s", returnType, result.type()));
        }

        return result;
    }

    private WasmValue[] toLocals(Type[] parameterTypes, Object[] args) {
        WasmValue[] locals = new WasmValue[parameterTypes.length];

        for (var i = 0; i < locals.length; i++) {
            locals[i] = new WasmValue(parameterTypes[i], args[i]);
        }
        return locals;
    }

    public final int readUnsignedLeb128(byte[] buffer) {
        int result = 0;
        int cur;
        int count = 0;
        do {
            cur = buffer[instructionPointer] & 0xff;
            result |= (cur & 0x7f) << (count * 7);
            count++;
            instructionPointer++;
        } while (((cur & 0x80) == 0x80) && count < 5);
        if ((cur & 0x80) == 0x80) {
            throw new ParseException("invalid LEB128 sequence");
        }
        return result;
    }
}
