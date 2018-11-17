package com.yzxie.easy.log.engine.handler;

import com.yzxie.easy.log.common.data.log.ILogMessage;
import com.yzxie.easy.log.engine.netty.NettyClient;
import com.yzxie.easy.log.engine.netty.NettyConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;

/**
 * @author xieyizun
 * @date 29/10/2018 00:34
 * @description:
 */
@Slf4j
public abstract class AbstractEngineHandler<T extends ILogMessage> implements IEngineHandler<T> {
    // 日志量太大，有撑爆内存的风险
    private BlockingQueue<T> messagesList = new LinkedBlockingQueue<>();
    private MessageProcessor messageProcessor = new MessageProcessor();

    public AbstractEngineHandler() {
        messageProcessor.start();
    }

    public void handle(T logMessage) {
        messagesList.add(logMessage);
    }

    protected abstract void process(T logMessage);

    /**
     * 引擎日志分析处理器
     */
    private class MessageProcessor extends Thread {
        @Override
        public void run() {
            T logMessage;
            while (!isInterrupted()) {
                try {
                    if ((logMessage = messagesList.take()) != null) {
                        process(logMessage);
                    }
                } catch (Exception e) {
                    log.error("MessageProcessor exception {}", e);
                }
            }
        }
    }
}
