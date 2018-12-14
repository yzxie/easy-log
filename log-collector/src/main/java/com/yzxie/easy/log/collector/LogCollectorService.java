package com.yzxie.easy.log.collector;

import com.yzxie.easy.log.collector.handler.LogMessageHandler;
import com.yzxie.easy.log.common.conf.KafkaConfig;
import com.yzxie.easy.log.common.kafka.KafkaTopic;
import com.yzxie.easy.log.common.service.AbstractService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xieyizun
 * @date 26/10/2018 15:35
 * @description:
 */
@Slf4j
public final class LogCollectorService extends AbstractService {
    private List<LogMessageHandler> logMessageHandlers;

    @Override
    public void start() {
        List<KafkaTopic> kafkaTopics = KafkaConfig.listKafkaTopics();
        logMessageHandlers = new ArrayList<>(kafkaTopics.size());
        for (KafkaTopic kafkaTopic : kafkaTopics) {
            LogMessageHandler logMessageHandler = new LogMessageHandler(kafkaTopic);
            logMessageHandlers.add(logMessageHandler);
        }
        startNext();
    }

    @Override
    public void stop() {
        stopNext();
    }

    /**
     * 单例实现
     */
    private LogCollectorService() {}

    private static class InstanceHolder {
        public static LogCollectorService instance = new LogCollectorService();
    }

    public static LogCollectorService getInstance() {
        return InstanceHolder.instance;
    }
}
