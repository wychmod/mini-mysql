package com.wychmod.mysql.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * @description:
 * @author: wychmod
 * @date: 2025-01-04
 */
public class PageUtil {

    public static final int PAGE_SIZE = 16 * 1024;  // 16KB

    public static int createPage(int spaceId) {
        return 0;
    }

    /**
     * 此方法负责将一个页面对象中的数据写入到对应的空间文件中
     * 它首先确定页面所属的空间ID和页面号，然后获取对应的文件路径和页面数据的ByteBuffer
     * 通过FileChannel将数据写入到文件中相应的位置
     *
     * @param page 要刷新到磁盘的页面对象
     * @return 写入到磁盘的数据大小
     * @throws RuntimeException 如果在写入过程中发生IO异常，将其包装成RuntimeException抛出
     */
    public static int flushPage(Page page) {
        // 获取页面所属的空间ID
        int spaceId = page.fil_page_get_space_id();
        // 获取页面在空间中的页号
        int pageNo = page.fil_page_get_page_offset();
        // 根据空间ID获取空间文件的路径
        Path path = SpaceUtil.getPathBySpaceId(spaceId);
        // 获取页面数据的ByteBuffer
        ByteBuffer byteBuffer = page.getPageByteBuffer();

        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.WRITE)) {
            // 确保position在ByteBuffer的起始位
            page.pageByteBuffer.clear();
            // 定位到文件中页面数据应该写入的位置
            fileChannel.position((long) pageNo * PAGE_SIZE);
            // 将ByteBuffer中的数据写入到文件中
            return fileChannel.write(byteBuffer);
        } catch (IOException e) {
            // 如果发生IO异常，将其包装成RuntimeException抛出
            throw new RuntimeException(e);
        }
    }
}
