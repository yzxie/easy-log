package com.yzxie.easy.log.engine.netty;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xieyizun
 * @date 11/11/2018 22:08
 * @description:
 */
@Slf4j
public class NettyClient {
    private String serverHost;
    private int serverPort;

    private Bootstrap bootstrap;
    /**
     * 客户端工作线程池
     * 客户端不需要accept连接，所以只需要一个group完成连接和发送请求和接收响应
     */
    private EventLoopGroup workerGroup;
    private ChannelFuture future;
    private Channel clientChannel;
    private int globalRetryCount = NettyConstants.GLOBAL_RECONNECT_TIMES;

    public NettyClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.bootstrap = new Bootstrap();
        this.workerGroup = new NioEventLoopGroup();
    }

    /**
     * 建立与netty服务端的连接
     */
    public void connect() {
        // 初始化bootstrap
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        // TCP相关参数
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        // 与客户端的channel初始化器
        bootstrap.handler(new NettyClientInitializer());
        doConnect();
    }

    /**
     * 实际发起与服务端的连接
     */
    public void doConnect() {
        try {
            future = bootstrap.connect(serverHost, serverPort);
            // 添加监听器，等future.sync()完成，获取结果
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        // 重置重试次数，以便运行过程中服务端宕机或重启在重连
                        globalRetryCount = NettyConstants.GLOBAL_RECONNECT_TIMES;
                        log.info("netty client connect {}:{} successfully.", serverHost, serverPort);
                    } else {
                        log.error("netty client connect {}:{} failure. go to finally to retry.",
                                serverHost, serverPort);
                        // 此处只能做启动时，服务端还没启动或者连不上的重连。
                        // 运行过程中，服务端宕机或重启，则无法重连，故统一放在finally进行重连。
                        /*
                        doConnect();
                        //休息5秒
                        try {
                            Thread.sleep(5000);
                        } catch (Exception ignore) {}
                        */
                    }
                }
            });
            // 成功建立连接，获取到clientChannel
            clientChannel = future.sync().channel();
            log.debug("clientChannel established.");

            // 阻塞等待关闭
            clientChannel.closeFuture().sync();
        } catch (Exception e) {
            log.error("NettyClient connect {}:{} failed. {}", serverHost, serverPort, e, e.getMessage());
        } finally {
            // 如果服务端宕机或重启导致断线，此处重连globalReTryCount次，此处为递归
            if (globalRetryCount-- > 0) {
                // 休息5秒
                try {
                    Thread.sleep(5000);
                } catch (Exception ignore) {}

                log.info("NettyClient connection lost. retry {}", globalRetryCount);
                // 重连
                doConnect();
            } else {
                // 重试失败后，关闭连接
                workerGroup.shutdownGracefully();
                clientChannel = null;
                log.error("Netty Client retry failure.");
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
            log.info("NettyClient sendMessage: {}", request);
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
