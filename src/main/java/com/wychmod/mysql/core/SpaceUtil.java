package com.wychmod.mysql.core;

import com.wychmod.mysql.dict.DictTable;
import com.wychmod.mysql.dict.SystemDict;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @description: SpaceUtil 类用于处理与用户表空间相关的操作
 * @author: wychmod
 * @date: 2024-01-03
 */
public class SpaceUtil {

    private static final int PAGE_SIZE = 16384;


    /**
     * 创建用户表空间
     * 表空间文件的扩展名约定为.ibd，文件内容初始化为空
     *
     * @param tableName 表名，用于生成表空间文件的名称
     * @return 创建的表空间文件的路径
     */
    public static Path createUserTableSpace(String tableName) {
        // 生成表空间文件的路径，约定文件扩展名为.ibd
        Path path = Paths.get(tableName + ".ibd");

        if (!Files.exists(path)) {
            byte[] bytes = new byte[1 * PAGE_SIZE];

            try {
                Files.write(path, bytes, StandardOpenOption.CREATE_NEW);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return path;
    }

    /**
     * 根据表空间ID获取FspHdrPage对象
     * FspHdrPage是表空间的第0页，包含表空间的元数据信息
     *
     * @param spaceId 表空间ID，用于定位表空间文件
     * @return 返回FspHdrPage对象，包含表空间的元数据
     */
    public static FspHdrPage getFspHdrPage(int spaceId) {
        Path path = getPathBySpaceId(spaceId);
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(PAGE_SIZE);
            fileChannel.position(0); // FspHdrPage是表空间的第0页
            fileChannel.read(byteBuffer);

            FspHdrPage page = new FspHdrPage();
            page.setPageByteBuffer(byteBuffer);
            return page;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据空间ID获取对应的路径
     *
     * @param spaceId 空间ID，用于识别特定的空间
     * @return 返回对应空间的路径
     */
    public static Path getPathBySpaceId(int spaceId) {
        DictTable dictTable = SystemDict.getInstance().getSpaceIdTables().get(spaceId);
        return Paths.get(dictTable.getPath());
    }

    /**
     * 获取指定表空间的下一个空闲页号
     *
     * @param spaceId 表空间id
     * @return 页号
     */
    public static int getNextPageNo(int spaceId) {
        FspHdrPage hdrPage = getFspHdrPage(spaceId);
        int pageNo = hdrPage.get_fsp_size();
        hdrPage.set_fsp_size(pageNo + 1);
        PageUtil.flushPage(hdrPage);  // 修改了FspHdrPage，直接持久化
        return pageNo;
    }
}
