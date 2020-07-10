package com.github.mikhasd.gen;

public class FloatTest {
    public static void main(String ... args){
        System.out.println(Integer.toBinaryString(Float.floatToRawIntBits(1.0f)));
        System.out.println(Integer.toBinaryString(Float.floatToRawIntBits(-1.0f)));
    }
}
