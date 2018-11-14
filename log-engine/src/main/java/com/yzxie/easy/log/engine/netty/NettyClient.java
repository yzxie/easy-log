package com.yzxie.easy.log.engine.netty;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
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
     * netty客户端启动器
     */
    private Bootstrap bootstrap = new Bootstrap();

    /**
     * 客户端工作线程池
     * 客户端不需要accept连接，所以只需要一个group完成连接和发送请求和接收响应
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 与服务端的连接channel
     */
    private Channel clientChannel;

    private ChannelFuture future;

    private int globalRetryCount = NettyConstants.GLOBAL_RECONNECT_TIMES;

    public NettyClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
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

    public void doConnect() {
        try {
            // 建立与服务端连接
            future = bootstrap.connect(serverHost, serverPort);
            // 添加监听器，等future.sync()完成，获取结果
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        // 重置重试次数，以便运行过程中服务端宕机或重启在重连
                        globalRetryCount = NettyConstants.GLOBAL_RECONNECT_TIMES;
                        LOG.info("netty client connect {}:{} successfully.", serverHost, serverPort);
                    } else {
                        LOG.error("netty client connect {}:{} failure. go to finally to retry.",
                                serverHost, serverPort);
                    }
                }
            });
            clientChannel = future.sync().channel();
            // 成功建立连接，获取到clientChannel，阻塞等待关闭
            LOG.info("clientChannel established.");
            clientChannel.closeFuture().sync();
        } catch (Exception e) {
            LOG.error("NettyClient connect {}:{} failed. {}", serverHost, serverPort, e, e.getMessage());
        } finally {
            // 如果服务端宕机或重启导致断线，此处重连globalReTryCount次，此处为递归
            if (globalRetryCount-- > 0) {
                // 休息5秒
                try {
                    Thread.sleep(5000);
                } catch (Exception ignore) {}
                // 重连
                LOG.info("NettyClient connection lost. retry {}", globalRetryCount);
                doConnect();
            } else {
                // 重试失败后，关闭连接
                workerGroup.shutdownGracefully();
                clientChannel = null;
                LOG.error("Netty Client retry failure.");
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
