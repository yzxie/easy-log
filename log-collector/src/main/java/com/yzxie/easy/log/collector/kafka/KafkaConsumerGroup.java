package com.yzxie.easy.log.collector.kafka;

import com.yzxie.easy.log.collector.handler.KafkaMessageHandler;
import com.yzxie.easy.log.common.kafka.KafkaGroup;
import com.yzxie.easy.log.common.kafka.KafkaTopic;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xieyizun
 * @date 17/11/2018 13:00
 * @description: 每个组对应一个应用，包括多个topic，每个topic对应一种日志
 */
@Slf4j
public class KafkaConsumerGroup {
    private String groupId;
    private Map<String, KafkaMessageHandler> consumers;

    public KafkaConsumerGroup(KafkaGroup kafkaGroup) {
        this.groupId = kafkaGroup.getGroupId();
        List<KafkaTopic> kafkaTopics = kafkaGroup.getKafkaTopicList();
        this.consumers = new HashMap<>(kafkaTopics.size());

        for (KafkaTopic kafkaTopic : kafkaTopics) {
            this.consumers.put(kafkaTopic.getName(), new KafkaMessageHandler(kafkaTopic, this.groupId));
        }
    }
}
