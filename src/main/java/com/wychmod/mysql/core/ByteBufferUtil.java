package com.wychmod.mysql.core;

import cn.hutool.core.util.ByteUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @description:
 * @author: wychmod
 * @date: 2025-01-04
 */
public class ByteBufferUtil {

    /**
     * 写1个字节到ByteBuffer的指定位置
     * @param byteBuffer 内存ByteBuffer
     * @param p position下标
     * @param n 1个字节范围内容的数字，比如1对应0000 0001，比如12对应0000 1100
     */
    public static void mach_write_to_1(ByteBuffer byteBuffer, int p, int n) {
        byte[] b = new byte[1];
        b[0] = (byte) (n);
        byteBuffer.position(p);
        byteBuffer.put(b);
        byteBuffer.clear();  // 只是重置了一些ByteBuffer中的属性，并不会情况具体内容，方便下一次对同一个ByteBuffer读取或写入内容
    }

    /**
     * 从ByteBuffer的指定位置读1个字节并返回
     * @param byteBuffer 内存ByteBuffer
     * @param p position下标
     * @return 1个字节对应的十进制数
     */
    public static int mach_read_from_1(ByteBuffer byteBuffer, int p) {
        byte[] b = new byte[1];
        byteBuffer.position(p);
        byteBuffer.get(b);
        byteBuffer.clear();
        return b[0];
    }

    /**
     * 写2个字节到ByteBuffer的指定位置
     * @param byteBuffer 内存ByteBuffer
     * @param p position下标
     * @param n 2个字节范围内容的数字
     */
    public static void mach_write_to_2(ByteBuffer byteBuffer, int p, int n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n >> 8);
        b[1] = (byte) (n);
        byteBuffer.position(p);
        byteBuffer.put(b);
        byteBuffer.clear();
    }

    /**
     * 从ByteBuffer的指定位置读2个字节并返回
     * @param byteBuffer 内存ByteBuffer
     * @param p position下标
     * @return 2个字节对应的十进制数
     */
    public static int mach_read_from_2(ByteBuffer byteBuffer, int p) {
        byte[] b = new byte[2];
        byteBuffer.position(p);
        byteBuffer.get(b);
        byteBuffer.clear();
        return (((int) (b[0]) << 8) | (int) (ByteUtil.byteToUnsignedInt(b[1])));
    }

    /**
     * 写4个字节到ByteBuffer的指定位置
     * @param byteBuffer 内存ByteBuffer
     * @param p position下标
     * @param n 4个字节范围内容的数字
     */
    public static void mach_write_to_4(ByteBuffer byteBuffer, int p, int n) {
        byte[] bytes = ByteUtil.intToBytes(n, ByteOrder.BIG_ENDIAN);
        byteBuffer.position(p);
        byteBuffer.put(bytes);
        byteBuffer.clear();
    }

    /**
     * 从ByteBuffer的指定位置读4个字节并返回
     * @param byteBuffer 内存ByteBuffer
     * @param p position下标
     * @return 4个字节对应的十进制数
     */
    public static int mach_read_from_4(ByteBuffer byteBuffer, int p) {
        byte[] b = new byte[4];
        byteBuffer.position(p);
        byteBuffer.get(b);
        byteBuffer.clear();
        return ByteUtil.bytesToInt(b, ByteOrder.BIG_ENDIAN);
    }

    /**
     * 从ByteBuffer的指定位置读指定个数的字节作为String返回
     * @param byteBuffer 内存ByteBuffer
     * @param p position下标
     * @param size 指定个数字节
     * @return 对应字符串
     */
    public static String mach_read_from_size_str(ByteBuffer byteBuffer, int p, int size) {
        byte[] b = new byte[size];

        byteBuffer.position(p);
        byteBuffer.get(b);
        byteBuffer.clear();

        return new String(b);
    }


    /**
     * 将字节数组b的内容复制到ByteBuffer中的指定位置
     * @param byteBuffer 内存ByteBuffer
     * @param p position下标
     * @param b 字节数组
     */
    public static void memcpy(ByteBuffer byteBuffer, int p, byte[] b) {
        byteBuffer.position(p);
        byteBuffer.put(b);
        byteBuffer.clear();
    }
}
