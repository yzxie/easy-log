package com.yzxie.easy.log.collector;

import com.yzxie.easy.log.collector.handler.SimpleMessageHandler;
import com.yzxie.easy.log.common.TopicContainer;
import com.yzxie.easy.log.common.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xieyizun
 * @date 26/10/2018 15:35
 * @description:
 */
public final class LogCollectorService extends AbstractService {
    private static final Logger LOG = LoggerFactory.getLogger(LogCollectorService.class);

    private ExecutorService executorService;

    @Override
    public void start() {
        LOG.info("LogCollectorService starting.");
        List<String> topicsList = TopicContainer.listTopics();
        executorService = Executors.newFixedThreadPool(topicsList.size());
        for (int i = 0; i < topicsList.size(); i++) {
            executorService.execute(new SimpleMessageHandler(topicsList.get(i)));
        }
        executorService.shutdown();
        LOG.info("LogCollectorService started successfully.");

        startNext();
    }

    @Override
    public void stop() {
        //todo
        executorService.shutdownNow();
        stopNext();
    }

}
