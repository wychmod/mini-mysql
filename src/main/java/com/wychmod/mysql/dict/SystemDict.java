package com.wychmod.mysql.dict;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileMode;
import lombok.Data;
import lombok.Getter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * @description: 系统字典类，用于存储和管理字典表信息
 * @author: wychmod
 * @date: 2024-12-31
 */
@Data
public class SystemDict implements Serializable {

    private final static String SYSTEM_DICT_FILE = "ibdata";

    private SystemDict() {
    }

    @Getter
    private static SystemDict instance = new SystemDict();

    public HashMap<String, DictTable> nameTables = new HashMap<>();
    public HashMap<Integer, DictTable> idTables = new HashMap<>();
    public HashMap<Integer, DictTable> spaceIdTables = new HashMap<>();
    public HashMap<Integer, List<DictIndex>> tableIdIndexes = new HashMap<>();

    public int maxTableId;
    public int maxIndexId;
    public int maxSpaceId;

    public int addMaxTableId() {
        return ++maxTableId;
    }

    public int addMaxIndexId() {
        return ++maxIndexId;
    }

    public int addMaxSpaceId() {
        return ++maxSpaceId;
    }

    /**
     * 序列化系统字典对象
     * 如果系统字典文件不存在，则创建该文件
     * 然后将当前系统字典对象序列化并写入文件中
     */
    public void serialize() {

        Path path = Paths.get(SYSTEM_DICT_FILE);
        if (!FileUtil.exist(path.toFile())) {
            FileUtil.createRandomAccessFile(path, FileMode.rw);
        }

        try (ObjectOutputStream out =
                     new ObjectOutputStream(FileUtil.getOutputStream(path))) {
            out.writeObject(this);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化系统字典对象
     * 如果系统字典文件不存在，则创建该文件并返回
     * 如果文件存在，则从文件中读取系统字典对象，并更新当前实例
     */
    public void deserialize() {

        Path path = Paths.get(SYSTEM_DICT_FILE);
        if (!FileUtil.exist(path.toFile())) {
            FileUtil.createRandomAccessFile(Paths.get(SYSTEM_DICT_FILE), FileMode.rw);
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(FileUtil.getInputStream(Paths.get(SYSTEM_DICT_FILE)))) {
            instance = (SystemDict) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
