package com.wychmod.mysql;

import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * @description: 该类用于执行SQL语句，包括创建表、插入数据、查询数据和修改表结构的操作。
 * @author: wychmod
 * @date: 2024-12-29
 */
public class SqlExecutor {
    private final HaInnodb haInnodb = new HaInnodb();

    /**
     * 执行创建表的操作。
     * @param createTableStatement 包含创建表所需信息的语句。
     */
    public void createTable(CreateTable createTableStatement) {
        haInnodb.createTable(createTableStatement);
    }

    /**
     * 执行数据插入操作。
     * @param insertStatement 包含数据插入所需信息的语句。
     */
    public void insert(Insert insertStatement) {
        haInnodb.insert(insertStatement);
    }

    /**
     * 执行查询操作。
     * @param plainSelectStatement 包含查询所需信息的语句。
     */
    public void select(PlainSelect plainSelectStatement) {
        haInnodb.select_one(plainSelectStatement);
    }

    /**
     * 执行表结构修改操作。
     * @param alterStatement 包含修改表结构所需信息的语句。
     */
    public void alter(Alter alterStatement) {
        haInnodb.alter(alterStatement);
    }
}
