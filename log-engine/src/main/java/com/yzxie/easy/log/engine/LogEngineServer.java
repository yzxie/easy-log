package com.yzxie.easy.log.engine;

import com.yzxie.easy.log.common.TopicContainer;
import com.yzxie.easy.log.common.server.AbstractServer;
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
public class LogEngineServer extends AbstractServer {
    private static final Logger LOG = LoggerFactory.getLogger(LogEngineServer.class);
    /**
     * key is kafka topic, which is a server ip or host
     * value is the engine handler
     */
    private static Map<String, IEngineHandler> logEngineHandlerMap;
    //test
    private static LogEngineServer logEngineServer = new LogEngineServer();

    @Override
    public void start() {
        List<String> topicLists = TopicContainer.listTopics();
        LOG.info("LogEngineServer start.....");

        logEngineHandlerMap = new ConcurrentHashMap<>(topicLists.size());
        for (int i = 0; i < TopicContainer.listTopics().size(); i++) {
            String topicName = topicLists.get(i);
            logEngineHandlerMap.put(topicName, new SimpleEngineHandler(topicName));
        }

        LOG.info("LogEngineServer start successfully");
    }

    @Override
    public void stop() {

    }

    public static void dispatch(String topicName, String content) {
        // test
        if (logEngineHandlerMap == null) {
            logEngineServer.start();
        }
        if (logEngineHandlerMap.get(topicName) != null) {
            logEngineHandlerMap.get(topicName).handle(content);
        }
    }
}
