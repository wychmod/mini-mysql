package com.wychmod.mysql.dict;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 索引类，用于表示数据在数据库表中的索引信息
 * 主要用途包括：关联数据与数据库表结构，便于管理和查询
 *
 * @author wychmod
 * @date 2024-12-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictIndex implements Serializable {

    /**
     * 数据库表的唯一标识符
     */
    private int tableId;

    /**
     * 索引的唯一标识符
     */
    private int indexId;

    /**
     * 索引的名称
     */
    private String indexName;

    /**
     * 字段列表
     * 包含该索引下所有表字段的信息，用于描述索引的具体结构
     */
    private List<DictField> dictFieldList = new ArrayList<>();
}
