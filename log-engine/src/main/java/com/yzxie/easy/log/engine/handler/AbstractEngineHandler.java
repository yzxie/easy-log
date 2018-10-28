package com.yzxie.easy.log.engine.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private ExecutorService singleThread = Executors.newSingleThreadExecutor();
    private MessageProcessor messageProcessor = new MessageProcessor();

    public AbstractEngineHandler(String topicName) {
        this.topicName = topicName;
        singleThread.execute(messageProcessor);
    }

    public void handle(String content) {
        messagesList.add(content);
    }


    public String getTopicName() {
        return topicName;
    }

    /**
     * implement by child class to specific analyze
     * @param content
     */
    protected abstract void process(String content);

    private class MessageProcessor implements Runnable {
        @Override
        public void run() {
            // todo isInterrupt
            while (true) {
                try {
                    String content = messagesList.take();
                    LOG.info("engine handler analyze {} : {}", topicName, content);
                    process(content);
                } catch (Exception e) {

                }
            }
        }
    }
}
