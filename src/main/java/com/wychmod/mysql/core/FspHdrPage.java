package com.wychmod.mysql.core;

/**
 * @description: FspHdrPage类用于处理表空间的文件头页面
 * 它提供了获取和设置表空间大小的方法
 * @author: wychmod
 * @date: 2025-01-04
 */
public class FspHdrPage extends Page{

    // 定义FSP_HEADER的起始位置和大小
    public final static int FSP_HEADER = 38;   /* FSP_HEADER从Page中的第38个字节开始，表示文件头占38个字节，FSP_HEADER实际占112个字节 */

    // 定义表空间ID的偏移量和大小
    public final static int FSP_SPACE_ID = 0;  /* 表空间id，占4个字节*/
    // 定义未使用字段的偏移量
    public final static int FSP_NOT_USED = 4;  /* 没用 */
    // 定义当前表空间占有页面数的偏移量和大小
    public final static int FSP_SIZE = 8;       /* 当前表空间占有的页面数，占4个字节 */


    /**
     * 获取表空间的页面数
     *
     * @return 表空间大小，以页面数表示
     */
    public int get_fsp_size() {
        return ByteBufferUtil.mach_read_from_4(pageByteBuffer, FSP_HEADER + FSP_SIZE);
    }

    /**
     * 设置表空间页面数
     *
     * @param size 新的表空间大小，以页面数表示
     */
    public void set_fsp_size(int size) {
        ByteBufferUtil.mach_write_to_4(pageByteBuffer, FSP_HEADER + FSP_SIZE, size);
    }
}
