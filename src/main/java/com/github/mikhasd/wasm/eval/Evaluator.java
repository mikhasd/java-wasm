package com.github.mikhasd.wasm.eval;

import com.github.mikhasd.wasm.WasmRuntimeException;
import com.github.mikhasd.wasm.model.Type;
import com.github.mikhasd.wasm.parse.ParseException;

import java.util.Stack;

public class Evaluator {

    private int instructionPointer;

    public WasmValue evaluate(byte[] opcodes, Type[] parameterTypes, Object[] args, Type returnType) {
        System.out.println("New invocation");
        instructionPointer = 0;
        var depth = 0;
        WasmValue[] locals = toLocals(parameterTypes, args);
        Stack<WasmValue> stack = new Stack<>();
        execution:
        while (instructionPointer < opcodes.length) {
            var opcode = opcodes[instructionPointer++];
            System.out.printf("Opcode 0x%02X ", opcode);


            switch (opcode) {
                // block
                case 0x02: {
                    System.out.println("block");
                    depth++;
                    var blockType = opcodes[instructionPointer++];
                }
                break;

                // end
                case 0x0B:
                    System.out.println("end");
                    break;
                // br_if
                case 0x0D: {
                    System.out.println("br_if");
                    var val = stack.pop();
                    if (!val.type().equals(Type.I32))
                        throw new WasmRuntimeException("0x0D:br_if expects I32 in the top of the stack but got " + val.type());
                    var label = readUnsignedLeb128(opcodes);
                    if (val.value().equals(1)) {
                        while(depth != label){
                            while(0x0B != opcodes[instructionPointer++]);
                            depth--;
                        }
                    }
                    break;
                }
                // return
                case 0x0F: {
                    System.out.println("return");
                    break execution;
                }

                // local.get
                case 0x20:
                    System.out.println("local.get");
                    var local = readUnsignedLeb128(opcodes);
                    stack.push(locals[local]);
                    break;
                // i32.const
                case 0x41: {
                    System.out.println("i32.const");
                    var val = readUnsignedLeb128(opcodes);
                    var currentValue = new WasmValue<>(Type.I32, val);
                    stack.push(currentValue);
                }
                break;
                // i32.lt_s
                case 0x48: {
                    System.out.println("i32.lt_s");
                    WasmValue<Integer> b = stack.pop();
                    WasmValue<Integer> a = stack.pop();
                    Integer d = a.value() < b.value() ? 1 : 0;
                    stack.push(new WasmValue(Type.I32, d));
                }
                break;
                // i32.add
                case 0x6A: {
                    System.out.println("i32.add");
                    WasmValue<Integer> b = stack.pop();
                    WasmValue<Integer> a = stack.pop();
                    Integer d = a.value() + b.value();
                    stack.push(new WasmValue(Type.I32, d));
                    break;
                }
                // i32.mul
                case 0x6C: {
                    System.out.println("i32.mul");
                    WasmValue<Integer> b = stack.pop();
                    WasmValue<Integer> a = stack.pop();
                    Integer d = a.value() * b.value();
                    stack.push(new WasmValue(Type.I32, d));
                    break;
                }
                // i32.div_u
                case 0x6E: {
                    System.out.println("i32.div_u");
                    WasmValue<Integer> b = stack.pop();
                    WasmValue<Integer> a = stack.pop();
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
