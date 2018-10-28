package com.yzxie.easy.log.collector;

import com.yzxie.easy.log.collector.handler.SimpleMessageHandler;
import com.yzxie.easy.log.common.server.AbstractServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xieyizun
 * @date 26/10/2018 15:35
 * @description:
 */
public final class LogCollectorServer extends AbstractServer {
    private static final Logger LOG = LoggerFactory.getLogger(LogCollectorServer.class);

    private List<String> topicsList = new ArrayList<>();
    private ExecutorService messageProcessThreadPool;

    @Override
    public void start() {
        init();

        messageProcessThreadPool = Executors.newFixedThreadPool(topicsList.size());
        for (int i = 0; i < topicsList.size(); i++) {
            messageProcessThreadPool.execute(new SimpleMessageHandler(topicsList.get(i)));
        }
        //messageProcessThreadPool.shutdown();
    }

    @Override
    public void stop() {
        //todo
        messageProcessThreadPool.shutdownNow();
    }

    private void init() {
        //todo load all topics from xml setting file
        topicsList.add("testFlume");
    }

}
