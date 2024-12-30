package com.wychmod.mysql.dict;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 字典表类，用于表示字典数据的表格信息
 *
 * @author wychmod
 * @date 2024-12-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictTable implements Serializable {

    /**
     * 表的唯一标识符
     */
    private int tableId;

    /**
     * 表空间的唯一标识符，表示该表所属的表空间
     */
    private int spaceId;

    /**
     * 表的名称，用于标识和描述表的内容
     */
    private String tableName;

    /**
     * 表的路径，表示表在文件系统中的位置
     */
    private String path;

    /**
     * 字典列列表，包含该表中所有列的定义信息
     */
    private List<DictColumn> dictColumnList;

}
