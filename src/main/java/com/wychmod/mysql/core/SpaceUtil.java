package com.wychmod.mysql.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileMode;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @description: SpaceUtil 类用于处理与用户表空间相关的操作
 * @author: wychmod
 * @date: 2024-01-03
 */
public class SpaceUtil {

    /**
     * 创建并返回用户表空间的路径
     * @param tableName 表名，用于生成表空间文件名
     * @return 返回用户表空间的路径
     */
    public static Path createUserTableSpace(String tableName) {
        Path path = Paths.get(tableName + ".ibd");
        FileUtil.createRandomAccessFile(path, FileMode.rw);
        return path;
    }
}
