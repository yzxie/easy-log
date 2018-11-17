package com.yzxie.easy.log.collector;

import com.yzxie.easy.log.collector.kafka.KafkaConsumerGroup;
import com.yzxie.easy.log.common.conf.KafkaConfig;
import com.yzxie.easy.log.common.kafka.KafkaGroup;
import com.yzxie.easy.log.common.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author xieyizun
 * @date 26/10/2018 15:35
 * @description:
 */
@Slf4j
public final class LogCollectorService extends AbstractService {
    private List<KafkaConsumerGroup> kafkaConsumerGroups;

    @Override
    public void start() {
        // 组列表，对应应用列表
        List<KafkaGroup> kafkaGroups = KafkaConfig.listKafkaGroup();
        this.kafkaConsumerGroups = new ArrayList<>(kafkaGroups.size());
        for (KafkaGroup kafkaGroup : kafkaGroups) {
            KafkaConsumerGroup kafkaConsumerGroup = new KafkaConsumerGroup(kafkaGroup);
            kafkaConsumerGroups.add(kafkaConsumerGroup);
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
