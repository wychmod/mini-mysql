package com.wychmod.mysql;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * SqlParse类用于解析和执行MySQL命令。
 * 它使用了JSQLParser库来解析SQL语句，并根据SQL语句的类型执行相应的操作。
 * @author wychmod
 * @date 2024-12-29
 */
public class SqlParse {

    // SqlOptimizer实例用于优化SQL查询语句
    private final SqlOptimizer sqlOptimizer = new SqlOptimizer();
    // SqlExecutor实例用于执行SQL命令
    private final SqlExecutor sqlExecutor = new SqlExecutor();

    /**
     * 解析MySQL命令。
     * 该方法接收一个SQL字符串，将其解析成一个Statement对象，并调用mysqlExecuteCommand方法执行相应的操作。
     * @param sql SQL命令字符串
     * @return 执行结果字符串
     */
    public String mysqlParse(String sql) {
        try {
            // 使用JSQLParser库解析SQL字符串
            Statement statement = CCJSqlParserUtil.parse(sql);
            // 执行解析后的SQL命令
            return mysqlExecuteCommand(statement);
        } catch (JSQLParserException e) {
            // 如果解析出错，抛出运行时异常
            System.out.println("sql解析错误:"+e);
            throw new RuntimeException("sql解析错误"+e);
        }
    }

    /**
     * 执行MySQL命令。
     * 该方法根据Statement对象的类型执行相应的操作，包括创建表、插入数据、执行查询和修改表结构。
     * @param statement 解析后的SQL命令对象
     * @return 执行结果字符串
     */
    public String mysqlExecuteCommand(Statement statement) {
        // 根据Statement对象的类型执行相应的操作
        if (statement instanceof CreateTable createTableStatement) {
            sqlExecutor.createTable(createTableStatement);
        } else if (statement instanceof Insert insertStatement) {
            sqlExecutor.insert(insertStatement);
        } else if (statement instanceof PlainSelect plainSelect) {
            SqlOptimizer.QEP_TAB qepTab = sqlOptimizer.optimize(plainSelect);
            qepTab.exec();
        } else if (statement instanceof Alter alterStatement) {
            sqlExecutor.alter(alterStatement);
        }

        return "success";
    }
}
