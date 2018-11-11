package com.yzxie.easy.log.engine.handler;

import com.yzxie.easy.log.engine.netty.NettyClient;
import com.yzxie.easy.log.engine.netty.NettyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;

/**
 * @author xieyizun
 * @date 29/10/2018 00:34
 * @description:
 */
public abstract class AbstractEngineHandler implements IEngineHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractEngineHandler.class);

    private String topicName;
    private BlockingQueue<String> messagesList = new LinkedBlockingQueue<>();
    private MessageProcessor messageProcessor = new MessageProcessor();
    // 每个处理器使用一个nettyClient发送消息给easy web
    private NettyClient nettyClient = new NettyClient(NettyConstants.SERVER_HOST, NettyConstants.SERVER_PORT);

    public AbstractEngineHandler(String topicName) {
        this.topicName = topicName;
        messageProcessor.start();
        // 异步启动netty与服务端的连接，避免服务器还没启动
        ScheduledExecutorService asyncStartUpService = Executors.newSingleThreadScheduledExecutor();
        asyncStartUpService.schedule(new NettyClient.AsyncStartUpTask(nettyClient), 30000, TimeUnit.MILLISECONDS);
    }

    public void handle(String content) {
        messagesList.add(content);
    }


    public String getTopicName() {
        return topicName;
    }

    public NettyClient getNettyClient() {
        return this.nettyClient;
    }

    /**
     * implement by child class to specific analyze process
     * @param content
     */
    protected abstract void process(String content);

    /**
     * each engine handler is bound to a thread.
     */
    private class MessageProcessor extends Thread {
        @Override
        public void run() {
            String content;
            while (!isInterrupted()) {
                try {
                    if ((content = messagesList.take()) != null) {
                        LOG.info("engine handler analyze {} : {}", topicName, content);
                        process(content);
                    }
                } catch (Exception e) {
                    LOG.error("MessageProcessor exception {}", e);
                }
            }
        }
    }
}
