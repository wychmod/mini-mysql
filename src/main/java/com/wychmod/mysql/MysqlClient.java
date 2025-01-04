package com.wychmod.mysql;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.Setter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MySQL客户端类，用于连接MySQL服务器并进行通信
 */
public class MysqlClient {

    // Netty客户端处理器，用于处理与MySQL服务器的交互
    public NettyClientHandler client = null;

    // 线程池，用于执行异步任务
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    // 服务器主机名
    private String host;

    // 服务器端口号
    private Integer port;

    /**
     * 构造函数，初始化MySQL客户端并连接到指定的主机和端口
     *
     * @param host 服务器主机名
     * @param port 服务器端口号
     */
    public MysqlClient(String host, Integer port) {
        this.host = host;
        this.port = port;

        // 启动客户端并连接到服务器
        start();
    }

    /**
     * 启动客户端，创建并配置Netty的Bootstrap以连接到MySQL服务器
     */
    public void start() {
        client = new NettyClientHandler();

        // 配置Netty的Bootstrap
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 添加字符串解码器和编码器，以及客户端处理器到管道
                        socketChannel.pipeline().addLast(new StringDecoder());
                        socketChannel.pipeline().addLast(new StringEncoder());
                        socketChannel.pipeline().addLast(client);
                    }
                });

        // 连接到指定的主机和端口
        try {
            b.connect(host, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息到MySQL服务器并等待结果
     *
     * @param message 要发送的消息
     * @return 从服务器接收到的结果
     * @throws InterruptedException 如果线程被中断
     * @throws ExecutionException   如果任务执行出错
     */
    public String sendMessage(String message) throws InterruptedException, ExecutionException {
        client.setMessage(message);
        return (String) executorService.submit(client).get();
    }

    /**
     * Netty客户端处理器类，处理与MySQL服务器的交互
     */
    static public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

        // 通道上下文，用于与服务器通信
        private ChannelHandlerContext context;

        // 要发送的消息
        @Setter
        private String message;

        // 从服务器接收到的结果
        private String result;

        /**
         * 当通道激活时调用，保存通道上下文
         *
         * @param ctx 通道上下文
         * @throws Exception 如果处理过程中发生异常
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            context = ctx;
        }

        /**
         * 当从服务器接收到消息时调用，保存结果并通知等待的线程
         *
         * @param ctx 通道上下文
         * @param msg 接收到的消息
         * @throws Exception 如果处理过程中发生异常
         */
        @Override
        public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            result = msg.toString();
            notify();
        }

        /**
         * 发送消息到服务器并等待结果
         *
         * @return 从服务器接收到的结果
         * @throws Exception 如果处理过程中发生异常
         */
        public synchronized Object call() throws Exception {
            context.writeAndFlush(this.message);
            wait();
            return result;
        }

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        MysqlClient client = new MysqlClient("localhost", 8888);

        String createSql = """
                CREATE TABLE t1 (
                  id int,
                  a varchar(10),
                  b varchar(10),
                  c varchar(10),
                  d int,
                  PRIMARY KEY (id)
                );
                """;
        client.sendMessage(createSql);
    }
}
