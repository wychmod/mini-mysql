package com.wychmod.mysql.dict;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 字典表列信息类
 * 该类用于表示字典表中的列信息，包括列所属的表ID、列名、列类型以及列的编号和长度
 *
 * @author wychmod
 * @date 2024-12-31
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictColumn implements Serializable {

    /**
     * 列所属的表ID
     */
    private int tableId;

    /**
     * 列名
     */
    private String name;

    /**
     * 列类型
     */
    private String type;

    /**
     * 列的编号
     */
    private int colNo;

    /**
     * 列的长度
     */
    private int len;
}
