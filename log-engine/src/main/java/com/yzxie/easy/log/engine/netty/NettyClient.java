package com.yzxie.easy.log.engine.netty;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xieyizun
 * @date 11/11/2018 22:08
 * @description:
 */
public class NettyClient {
    private static final Logger LOG = LoggerFactory.getLogger(NettyClient.class);

    /**
     * 服务端主机
     */
    private String serverHost;

    /**
     * 服务端端口
     */
    private int serverPort;

    /**
     * 与服务端的连接channel
     */
    private Channel clientChannel;

    public NettyClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    /**
     * 建立与netty服务端的连接
     */
    public void connect() {
        // 客户端不需要accept连接，所以只需要一个group完成连接和发送请求和接收响应
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        int trysCount = 3;
        boolean succeed = false;

        while (!succeed && trysCount > 0) {
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(workerGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.option(ChannelOption.TCP_NODELAY, true);
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                bootstrap.handler(new NettyClientInitializer());
                // 建立与服务端连接
                clientChannel = bootstrap.connect(serverHost, serverPort).sync().channel();
                clientChannel.closeFuture().sync();
                succeed = true;
            } catch (Exception e) {
                LOG.error("NettyClient connect {}:{} failed. {}", serverHost, serverPort, e, e.getMessage());
            } finally {
                workerGroup.shutdownGracefully();
            }

            // 重试
            if (!succeed) {
                try {
                    Thread.sleep(10000);
                    trysCount--;
                } catch (InterruptedException e) {

                }
            }

        }

    }

    /**
     * 发送消息给netty服务端
     * @param data
     */
    public void sendMessage(JSONObject data) {
        if (clientChannel != null) {
            StringBuilder request = new StringBuilder();
            request.append(data);
            request.append("\n");
            clientChannel.writeAndFlush(request);
            LOG.info("NettyClient sendMessage: {}", request);
        }
    }

    /**
     * netty client异步启动任务
     */
    public static class AsyncStartUpTask implements Runnable {
        private NettyClient nettyClient;

        public AsyncStartUpTask(NettyClient nettyClient) {
            this.nettyClient = nettyClient;
        }

        @Override
        public void run() {
            nettyClient.connect();
        }
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8089;
        NettyClient nettyClient = new NettyClient(host, port);
        nettyClient.connect();
    }
}
