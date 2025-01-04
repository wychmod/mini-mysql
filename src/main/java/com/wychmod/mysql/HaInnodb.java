package com.wychmod.mysql;

import com.wychmod.mysql.core.FspHdrPage;
import com.wychmod.mysql.core.PageUtil;
import com.wychmod.mysql.core.SpaceUtil;
import com.wychmod.mysql.dict.DictColumn;
import com.wychmod.mysql.dict.DictTable;
import com.wychmod.mysql.dict.SystemDict;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 该类用于模拟和处理HaInnodb数据库中的基本数据库操作，包括创建表、插入数据、查询数据和修改表结构。
 * @author: wychmod
 * @date: 2024-12-29
 */
public class HaInnodb {


    /**
     * 根据CreateTable对象创建表
     * 此方法负责处理表的创建逻辑，包括检查表是否已存在、生成表和空间ID、
     * 创建表空间文件、构建表的元数据并持久化到系统字典中
     * @param createTableStatement 包含创建表信息的对象
     */
    public void createTable(CreateTable createTableStatement) {
        // 获取表名
        String tableName = createTableStatement.getTable().getName();

        // 检查表是否已存在
        if (SystemDict.getInstance().getNameTables().containsKey(tableName)) {
            throw new RuntimeException("Table already exists.");
        }

        // 生成新的表ID和空间ID
        int tableId = SystemDict.getInstance().addMaxTableId();
        int spaceId = SystemDict.getInstance().addMaxSpaceId();

        // 创建t1.ibd文件，返回文件路径
        Path tableSpacePath = SpaceUtil.createUserTableSpace(tableName);

        // 构建表的元数据对象
        DictTable dictTable = new DictTable();
        dictTable.setTableId(tableId);
        dictTable.setSpaceId(spaceId);
        dictTable.setTableName(tableName);
        dictTable.setPath(tableSpacePath.toString());

        // 获取并设置表的列信息
        List<DictColumn> dictColumnList = getDictColumns(createTableStatement, tableId);
        dictTable.setDictColumnList(dictColumnList);

        // 将当前创建的表元数据添加到数据字典
        SystemDict.getInstance().getNameTables().put(tableName, dictTable);
        SystemDict.getInstance().getIdTables().put(tableId, dictTable);
        SystemDict.getInstance().getSpaceIdTables().put(spaceId, dictTable);

        FspHdrPage fspHdrPage = SpaceUtil.getFspHdrPage(spaceId);
        fspHdrPage.init_file_header(spaceId, 0);
        fspHdrPage.fil_page_set_type(8);  // 源码中8表示FSP_HDR
        fspHdrPage.set_fsp_size(1);       // 当前表空间中只有1页
        PageUtil.flushPage(fspHdrPage);   // 记得把页的修改持久化到ibd文件中去

        // 把SystemDict对象持久化
        SystemDict.getInstance().serialize();
    }

    /**
     * 根据CreateTable对象提取并构建列的元数据列表
     * 此方法遍历创建表语句中的列定义，为每个列生成一个DictColumn对象，
     * 设置其属性，如表ID、列名、数据类型和长度，并返回列的元数据列表
     *
     * @param createTableStatement 包含创建表信息的对象
     * @param tableId 表的唯一标识符
     * @return 列的元数据列表
     */
    private static List<DictColumn> getDictColumns(CreateTable createTableStatement, int tableId) {
        List<DictColumn> dictColumnList = new ArrayList<>();
        List<ColumnDefinition> columnDefinitions = createTableStatement.getColumnDefinitions();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            DictColumn dictColumn = new DictColumn();
            dictColumn.setTableId(tableId);
            dictColumn.setName(columnDefinition.getColumnName());
            String dataType = columnDefinition.getColDataType().getDataType();
            dictColumn.setType(dataType);
            dictColumn.setColNo(dictColumnList.size());
            if ("int".equals(dataType)) {
                dictColumn.setLen(4);
            } else {
                // 实际应该解析出varchar(11)中的11，知道编码集，才能算出字段长度
                // 写死了，方便模拟
                dictColumn.setLen(2);
            }
            dictColumnList.add(dictColumn);
        }
        return dictColumnList;
    }
    /**
     * 处理数据插入操作。
     * @param insertStatement 包含数据插入所需信息的语句。
     */
    public void insert(Insert insertStatement) {
        // 待实现：插入数据到数据库的逻辑。
    }

    /**
     * 处理单条查询操作。
     * @param plainSelectStatement 包含查询所需信息的语句。
     */
    public void select_one(PlainSelect plainSelectStatement) {
        // 待实现：从数据库查询单条数据的逻辑。
    }

    /**
     * 处理表结构修改操作。
     * @param alterStatement 包含修改表结构所需信息的语句。
     */
    public void alter(Alter alterStatement) {
        // 待实现：修改表结构的逻辑。
    }
}
