package com.wychmod.mysql.dict;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 索引字段类，用于表示表中的字段信息
 * 主要包含表列信息和前缀长度两个属性
 *
 * @author wychmod
 * @date 2024-12-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictField implements Serializable {

    /**
     * 列信息，存储字段的列相关数据
     */
    private DictColumn dictColumn;

    /**
     * 前缀长度，表示字段值前缀的长度
     * 用于某些特定的场景下，索引对字段值进行前缀匹配或处理
     */
    private Integer prefixLen;
}
