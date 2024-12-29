package com.wychmod.mysql;

import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * SqlOptimizer类用于优化SQL查询语句
 * 它提供了一个方法来接收一个查询语句，并返回一个经过优化的查询执行计划表（QEP_TAB）
 */
public class SqlOptimizer {

    /**
     * 优化给定的PlainSelect查询语句
     * 此方法接收一个PlainSelect对象作为输入，代表一个基本的SELECT查询语句
     * 它返回一个QEP_TAB对象，该对象封装了优化后的查询执行计划
     *
     * @param plainSelect 代表待优化的SELECT查询语句的PlainSelect对象
     * @return 返回一个QEP_TAB对象，其中包含优化后的查询执行计划
     */
    public QEP_TAB optimize(PlainSelect plainSelect) {
        return new QEP_TAB(plainSelect);
    }

    /**
     * QEP_TAB类代表查询执行计划表
     * 它封装了一个SQL查询语句的执行计划，并提供执行该查询的方法
     */
    static class QEP_TAB {

        // SqlExecutor实例用于执行SQL查询语句
        private final SqlExecutor sqlExecutor = new SqlExecutor();

        // 保存当前QEP_TAB实例关联的PlainSelect查询语句
        private final PlainSelect plainSelectStatement;

        /**
         * 构造方法，初始化QEP_TAB实例
         * 它接收一个PlainSelect查询语句，并将其保存到实例变量中
         *
         * @param plainSelectStatement 代表SELECT查询语句的PlainSelect对象
         */
        public QEP_TAB(PlainSelect plainSelectStatement) {
            this.plainSelectStatement = plainSelectStatement;
        }

        /**
         * 执行当前QEP_TAB实例关联的SELECT查询语句
         * 此方法通过调用SqlExecutor的select方法来执行查询语句
         */
        public void exec() {
            sqlExecutor.select(plainSelectStatement);
        }
    }
}
