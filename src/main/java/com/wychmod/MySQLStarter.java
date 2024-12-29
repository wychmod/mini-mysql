package com.wychmod;

import com.wychmod.mysql.MysqlServer;

/**
 * @description:
 * @author: wychmod
 * @date: 2024-12-29
 */
public class MySQLStarter {

    public static void main(String[] args) throws Exception {
        MysqlServer mysqlServer = new MysqlServer(8888);
        mysqlServer.run();
    }
}
