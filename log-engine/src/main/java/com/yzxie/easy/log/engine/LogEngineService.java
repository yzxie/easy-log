package com.yzxie.easy.log.engine;

import com.yzxie.easy.log.common.TopicContainer;
import com.yzxie.easy.log.common.service.AbstractService;
import com.yzxie.easy.log.engine.handler.IEngineHandler;
import com.yzxie.easy.log.engine.handler.SimpleEngineHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xieyizun
 * @date 26/10/2018 15:34
 * @description:
 */
public class LogEngineService extends AbstractService {
    private static final Logger LOG = LoggerFactory.getLogger(LogEngineService.class);

    /**
     * key is kafka topic, which is a service ip or host
     * value is the engine handler
     */
    private static Map<String, IEngineHandler> logEngineHandlerMap;

    @Override
    public void start() {
        LOG.info("LogEngineService starting.");
        List<String> topicLists = TopicContainer.listTopics();
        logEngineHandlerMap = new ConcurrentHashMap<>(topicLists.size());
        for (int i = 0; i < TopicContainer.listTopics().size(); i++) {
            String topicName = topicLists.get(i);
            logEngineHandlerMap.put(topicName, new SimpleEngineHandler(topicName));
        }
        LOG.info("LogEngineService started successfully");

        startNext();
    }

    @Override
    public void stop() {
        stopNext();
    }

    public static void dispatch(String topicName, String content) {
        if (logEngineHandlerMap.get(topicName) != null) {
            logEngineHandlerMap.get(topicName).handle(content);
        }
    }
}
