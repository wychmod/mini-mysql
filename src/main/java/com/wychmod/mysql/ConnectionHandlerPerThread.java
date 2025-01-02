package com.wychmod.mysql;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * @description: 读取客户端发送的SQL语句，解析并返回结果
 * @author: wychmod
 * @date: 2024-12-29
 */
public class ConnectionHandlerPerThread extends ChannelInboundHandlerAdapter {

    // SqlParse实例用于SQL解析
    private final SqlParse sqlParse = new SqlParse();

    /**
     * 处理接收到的消息
     * @param ctx 上下文，用于访问通道、写数据等
     * @param msg 接收到的消息，这里预期是ByteBuf类型的SQL语句
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 将接收到的消息转换为字符串形式的SQL语句
        ByteBuf in = (ByteBuf) msg;
        String sql = in.toString(CharsetUtil.UTF_8);
        System.out.println("接收到sql: "+ sql);

        // 执行SQL解析
        String result = sqlParse.mysqlParse(sql);

        // 准备并发送响应
        ByteBuf responseBuffer = Unpooled.copiedBuffer(result, CharsetUtil.UTF_8);
        ctx.write(responseBuffer);
    }

    /**
     * 处理读取操作完成时的动作
     * @param ctx 上下文，用于刷新通道
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 刷新通道，将缓冲区的数据写入到网络
        ctx.flush();
    }

    /**
     * 处理异常
     * @param ctx 上下文，用于关闭通道
     * @param cause 异常原因
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 打印异常信息并关闭通道
        System.out.println("处理请求时发生异常"+cause);
        ctx.close();
    }
}
