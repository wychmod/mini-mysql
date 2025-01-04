package com.wychmod.mysql.core;

import lombok.Data;

import java.nio.ByteBuffer;

import static com.wychmod.mysql.core.ByteBufferUtil.*;

/**
 * @description: 本类用于表示和操作数据库中的一个页面，提供了设置和获取页面头部信息的方法
 * @author: wychmod
 * @date: 2025-01-04
 */
@Data
public class Page {
    /* 文件头相关，共38个字节*/
    public final static int FIL_PAGE_SPACE_OR_CHKSUM = 0;
    public final static int FIL_PAGE_OFFSET = 4;        /*当前页的页号，从第4个字节开始，占4个字节*/
    public final static int FIL_PAGE_PREV = 8;         /* 上一页的页号，占4个字节*/
    public final static int FIL_PAGE_NEXT = 12;         /* 下一页的页号，占4个字节*/
    public final static int FIL_PAGE_LSN = 16;
    public final static int FIL_PAGE_TYPE = 24;         /* 页的类型，从第24个字节开始，占2个字节*/
    public final static int FIL_PAGE_FILE_FLUSH_LSN = 26;
    public final static int FIL_PAGE_ARCH_LOG_NO_OR_SPACE_ID = 34; /*属于哪个表空间，spaceId，占4个字节*/

    public ByteBuffer pageByteBuffer;

    /**
     * 初始化文件头信息，包括设置spaceId和pageNo
     * @param spaceId 表空间ID
     * @param pageNo 页面编号
     */
    public void init_file_header(int spaceId, int pageNo) {
        fil_page_set_space_id(spaceId);
        fil_page_set_page_offset(pageNo);
    }

    /**
     * 设置页面编号
     * @param pageNo 页面编号
     */
    public void fil_page_set_page_offset(int pageNo) {
        mach_write_to_4(pageByteBuffer, FIL_PAGE_OFFSET, pageNo);
    }

    /**
     * 获取页面编号
     * @return 页面编号
     */
    public int fil_page_get_page_offset() {
        return mach_read_from_4(pageByteBuffer, FIL_PAGE_OFFSET);
    }

    /**
     * 设置表空间ID
     * @param spaceId 表空间ID
     */
    public void fil_page_set_space_id(int spaceId) {
        mach_write_to_4(pageByteBuffer, FIL_PAGE_ARCH_LOG_NO_OR_SPACE_ID, spaceId);
    }

    /**
     * 获取表空间ID
     * @return 表空间ID
     */
    public int fil_page_get_space_id() {
        return mach_read_from_4(pageByteBuffer, FIL_PAGE_ARCH_LOG_NO_OR_SPACE_ID);
    }

    /**
     * 设置页面类型
     * @param type 页面类型
     */
    public void fil_page_set_type(int type) {
        mach_write_to_2(pageByteBuffer, FIL_PAGE_TYPE, type);
    }

    /**
     * 获取页面类型
     * @return 页面类型
     */
    public int fil_page_get_type() {
        return mach_read_from_2(pageByteBuffer, FIL_PAGE_TYPE);
    }

}
