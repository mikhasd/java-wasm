package com.github.mikhasd.wasm.gen;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class OutputStreamCodeWriter implements CodeWriter {

    private final static Charset UTF8 = Charset.forName("UTF-8");

    private final OutputStream output;

    public OutputStreamCodeWriter(OutputStream output) {
        this.output = output;
    }

    @Override
    public void writeByte(byte value) {
        this.writeByteInternal(value);
    }

    @Override
    public void writeBytes(byte[] value) {
        this.writeBytesInternal(value);
    }

    @Override
    public void writeInteger(int value) {
        this.writeLeb128(value);
    }

    @Override
    public void writeI32(int value) {
        this.writeLeb128(value);
    }

    @Override
    public void writeI64(long value) {
        this.writeLeb128(value);
    }

    private final byte[] intBuffer = new byte[4];
    @Override
    public synchronized void writeF32(float value) {
        int v = Float.floatToRawIntBits(value);
        intBuffer[0] = (byte)((v >>> 24) & 0xFF);
        intBuffer[1] = (byte)((v >>> 16) & 0xFF);
        intBuffer[2] = (byte)((v >>>  8) & 0xFF);
        intBuffer[3] = (byte)((v >>>  0) & 0xFF);
        this.writeBytesInternal(intBuffer);
    }

    private final byte[] longBuffer = new byte[8];

    @Override
    public synchronized void writeF64(double value) {
        long v = Double.doubleToRawLongBits(value);
        longBuffer[0] = (byte)(v >>> 56);
        longBuffer[1] = (byte)(v >>> 48);
        longBuffer[2] = (byte)(v >>> 40);
        longBuffer[3] = (byte)(v >>> 32);
        longBuffer[4] = (byte)(v >>> 24);
        longBuffer[5] = (byte)(v >>> 16);
        longBuffer[6] = (byte)(v >>>  8);
        longBuffer[7] = (byte)(v >>>  0);
        this.writeBytesInternal(longBuffer);
    }

    @Override
    public void writeString(String value) {
        byte[] bytes = value.getBytes(UTF8);
        this.writeLeb128(bytes.length);
        this.writeBytesInternal(bytes);
    }

    private void writeByteInternal(byte value) {
        try {
            this.output.write(value);
        } catch (IOException e) {
            throw new CodeGenerationException(e);
        }
    }

    private void writeBytesInternal(byte[] value) {
        try {
            this.output.write(value);
        } catch (IOException e) {
            throw new CodeGenerationException(e);
        }
    }

    private void writeLeb128(long value) {
        if (value == 0) {
            this.writeByteInternal((byte)0);
        } else if (value > 0) {
            this.writeUnsignedLeb128(value);
        } else {
            this.writeSignedLeb128(value);
        }
    }

    private void writeLeb128(int value) {
        if (value == 0) {
            this.writeByteInternal((byte)0);
        } else if (value > 0) {
            this.writeUnsignedLeb128(value);
        } else {
            this.writeSignedLeb128(value);
        }
    }

    private void writeUnsignedLeb128(int value) {
        int remaining = value >>> 7;
        while (remaining != 0) {
            this.writeByteInternal((byte) ((value & 0x7f) | 0x80));
            value = remaining;
            remaining >>>= 7;
        }
        this.writeByteInternal((byte) (value & 0x7f));
    }

    private void writeUnsignedLeb128(long value) {
        long remaining = value >>> 7;
        while (remaining != 0) {
            this.writeByteInternal((byte) ((value & 0x7f) | 0x80));
            value = remaining;
            remaining >>>= 7;
        }
        this.writeByteInternal((byte) (value & 0x7f));
    }


    private void writeSignedLeb128(int value) {
        int remaining = value >> 7;
        boolean hasMore = true;
        int end = ((value & Integer.MIN_VALUE) == 0) ? 0 : -1;

        while (hasMore) {
            hasMore = (remaining != end)
                    || ((remaining & 1) != ((value >> 6) & 1));
            this.writeByteInternal((byte) ((value & 0x7f) | (hasMore ? 0x80 : 0)));
            value = remaining;
            remaining >>= 7;
        }
    }

    private void writeSignedLeb128(long value) {
        long remaining = value >> 7;
        boolean hasMore = true;
        long end = ((value & Integer.MIN_VALUE) == 0) ? 0 : -1;

        while (hasMore) {
            hasMore = (remaining != end)
                    || ((remaining & 1) != ((value >> 6) & 1));
            this.writeByteInternal((byte) ((value & 0x7f) | (hasMore ? 0x80 : 0)));
            value = remaining;
            remaining >>= 7;
        }
    }
}
