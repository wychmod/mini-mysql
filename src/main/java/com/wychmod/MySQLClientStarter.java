package com.wychmod;

import com.wychmod.mysql.MysqlClient;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @description: MySQL客户端启动器
 * @author: wychmod
 * @date: 2024-12-29
 */
public class MySQLClientStarter {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        // 创建终端
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        // 读取终端输入
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();

        MysqlClient client = new MysqlClient("localhost", 8888);

        // 输出欢迎语
        terminal.writer().append("连接成功！");

        // 提示符
        String prompt = "mini_mysql> ";
        while (true) {
            terminal.writer().append("\n");
            terminal.flush();

            final String line = lineReader.readLine(prompt);;
            System.out.println(client.sendMessage(line));
        }
    }
}
