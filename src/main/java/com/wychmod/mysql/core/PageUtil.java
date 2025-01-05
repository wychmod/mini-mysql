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

    /**
     * 创建一个新的页面
     *
     * 此方法负责在指定的表空间内分配并初始化一个新的页面它首先获取下一个可用的页面编号，
     * 然后准备一个ByteBuffer用于页面数据的存储，接着设置页面的元数据，包括表空间ID、页面编号、页面类型，
     * 并初始化文件头和页面头最后，将页面写入到磁盘并返回新的页面编号
     *
     * @param spaceId 表空间的唯一标识符，用于确定页面属于哪个表空间
     * @return 返回新创建页面的编号
     */
    public static int createPage(int spaceId) {
        // 获取下一个可用的页面编号
        int nextPageNo = SpaceUtil.getNextPageNo(spaceId);

        // 16KB内存
        ByteBuffer byteBuffer = ByteBuffer.allocate(PAGE_SIZE);
        IndexPage page = new IndexPage();
        page.setPageByteBuffer(byteBuffer);

        // 设置页面的表空间ID
        page.fil_page_set_space_id(spaceId);
        // 设置页面的偏移量（即页面编号）
        page.fil_page_set_page_offset(nextPageNo);
        // 设置页面类型为INDEX页，源码中17855表示INDEX页，暂时还不知道怎么要用这个数字
        page.fil_page_set_type(17855);
        // 初始化文件头 每个页都有的文件头
        page.init_file_header(spaceId, nextPageNo);

        // 初始化页面头 index页自己的文件头
        page.init_page_header();

        // 将页面写入到磁盘并返回新的页面编号
        return flushPage(page);
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
