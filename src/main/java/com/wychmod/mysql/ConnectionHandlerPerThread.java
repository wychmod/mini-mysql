package com.wychmod.mysql;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: wychmod
 * @date: 2024-12-29
 */
public class ConnectionHandlerPerThread extends ChannelInboundHandlerAdapter {

    private final SqlParse sqlParse = new SqlParse();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        String sql = in.toString(CharsetUtil.UTF_8);
        System.out.println("接收到sql: "+ sql);

        // 执行
        String result = sqlParse.mysqlParse(sql);

        // 发送响应
        ByteBuf responseBuffer = Unpooled.copiedBuffer(result, CharsetUtil.UTF_8);
        ctx.write(responseBuffer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("处理请求时发生异常"+cause);
        ctx.close();
    }
}
