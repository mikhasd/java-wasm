package com.github.mikhasd.wasm.eval;

import com.github.mikhasd.wasm.WasmException;

public class UnsupportedVersionException extends WasmException {
    public UnsupportedVersionException(byte[] supportedVersion, byte[] version) {
        super(String.format("supported: 0x%02X 0x%02X 0x%02X 0x%02X; provided: 0x%02X 0x%02X 0x%02X 0x%02X",
                supportedVersion[0], supportedVersion[1], supportedVersion[2], supportedVersion[3],
                version[0], version[1], version[2], version[3]));
    }
}
