package com.yzxie.easy.log.engine.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xieyizun
 * @date 11/11/2018 22:32
 * @description: 客户端处理器
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger LOG = LoggerFactory.getLogger(NettyClientHandler.class);

    /**
     * 接收服务端的响应
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        LOG.info("NettyClientHandler received response: {}", msg);
    }
}
