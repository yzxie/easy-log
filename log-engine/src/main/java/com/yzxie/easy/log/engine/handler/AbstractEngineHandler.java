package com.yzxie.easy.log.engine.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

    public AbstractEngineHandler(String topicName) {
        this.topicName = topicName;
        messageProcessor.start();
    }

    public void handle(String content) {
        messagesList.add(content);
    }


    public String getTopicName() {
        return topicName;
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
