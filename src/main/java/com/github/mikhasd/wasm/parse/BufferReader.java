package com.github.mikhasd.wasm.parse;

import java.nio.ByteBuffer;

public class BufferReader {

    private final ByteBuffer buffer;

    BufferReader(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    ByteBuffer getBuffer() {
        return this.buffer;
    }

    public int readI32() {
        byte[] buffer = new byte[4];
        this.buffer.get(buffer, 0, 4);
        int ch1 = buffer[0];
        int ch2 = buffer[1];
        int ch3 = buffer[2];
        int ch4 = buffer[3];
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    byte sectionIdx() {
        return readU7();
    }

    byte readU7() {
        byte b = buffer.get();
        if ((b & 0x80) != 0)
            throw new ParseException(String.format("Invalid U7: 0x%X", b));
        return b;
    }

    public final int readUnsignedLeb128() {
        int result = 0;
        int cur;
        int count = 0;
        do {
            cur = buffer.get() & 0xff;
            result |= (cur & 0x7f) << (count * 7);
            count++;
        } while (((cur & 0x80) == 0x80) && count < 5);
        if ((cur & 0x80) == 0x80) {
            throw new ParseException("invalid LEB128 sequence");
        }
        return result;
    }

    public BufferReader slice(int length){
        var buf = buffer.slice().limit(length).asReadOnlyBuffer();
        return new BufferReader(buf);
    }

    public byte readU8() {
        return buffer.get();
    }

    public byte[] readBytes(int length) {
        byte[] buffer = new byte[length];
        this.buffer.get(buffer, 0, length);
        return buffer;
    }


    public void skip(int shift) {
        var current = buffer.position();
        var nu = current + shift;
        buffer.position(nu);
    }

    public int position() {
        return buffer.position();
    }

    public byte[] toBytes() {
        return this.buffer.array();
    }
}
