package com.yzxie.easy.log.engine.handler;

import com.yzxie.easy.log.common.data.log.ILogMessage;
import com.yzxie.easy.log.engine.push.netty.NettyClient;
import com.yzxie.easy.log.engine.push.netty.NettyConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author xieyizun
 * @date 29/10/2018 00:34
 * @description:
 */
@Slf4j
public abstract class AbstractEngineHandler<T extends ILogMessage> implements IEngineHandler<T> {
    private BlockingQueue<T> messageList;
    private MessageProcessor messageProcessor;

    /**
     * 初始化
     * @return
     */
    public IEngineHandler startUp() {
        messageList = new LinkedBlockingQueue<>();
        messageProcessor = new MessageProcessor(messageList);
        ExecutorService messageProcessPool = Executors.newSingleThreadExecutor();
        messageProcessPool.execute(messageProcessor);
        return this;
    }

    /**
     * 暂存消息到消息队列，保证每种类型消息的顺序性
     * 默认实现为单个消息队列，单个生产者
     * @param logMessage
     */
    public void handle(T logMessage) {
        messageList.add(logMessage);
    }

    protected abstract void process(T logMessage);

    /**
     * 引擎日志分析处理器
     */
    protected class MessageProcessor implements Runnable {
        private BlockingQueue<T> messageList;

        public MessageProcessor(BlockingQueue<T> messageList) {
            this.messageList = messageList;
        }

        @Override
        public void run() {
            T logMessage;
            while (true) {
                try {
                    if ((logMessage = messageList.take()) != null) {
                        process(logMessage);
                    }
                } catch (Exception e) {
                    log.error("MessageProcessor exception {}", e);
                }
            }
        }
    }
}
