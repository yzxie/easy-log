package com.yzxie.easy.log.common.conf;

import com.yzxie.easy.log.common.data.log.LogType;
import com.yzxie.easy.log.common.kafka.KafkaTopic;
import com.yzxie.easy.log.common.kafka.KafkaTopicPartition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.HierarchicalConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xieyizun
 * @date 29/10/2018 15:16
 * @description:
 */
@Slf4j
public class KafkaConfig {
    public static final String TOPIC_LIST = "kafka.topic";
    public static final String TOPIC_NAME = "name";
    public static final String TOPIC_PARTITIONS = "partition";
    public static final String TOPIC_PARTITION_APP = "app";
    public static final String TOPIC_PARTITION_INDEX = "index";

    private static List<KafkaTopic> kafkaTopics = new ArrayList<>();

    static {
        XmlConfig topicConfig = new XmlConfig("kafkaConfig.xml");
        List<HierarchicalConfiguration> topicList = topicConfig.getList(TOPIC_LIST);
        for (HierarchicalConfiguration topic : topicList) {
            String topicName = topic.getString(TOPIC_NAME);
            List<HierarchicalConfiguration> partitions = topic.configurationsAt(TOPIC_PARTITIONS);

            KafkaTopic kafkaTopic = new KafkaTopic();
            kafkaTopic.setName(topicName);
            List<KafkaTopicPartition> kafkaTopicPartitions = new ArrayList<>();
            kafkaTopic.setPartitions(kafkaTopicPartitions);
            for (HierarchicalConfiguration partition : partitions) {
                String appId = partition.getString(TOPIC_PARTITION_APP);
                int partitionIndex = Integer.valueOf(partition.getInt(TOPIC_PARTITION_INDEX));
                kafkaTopicPartitions.add(new KafkaTopicPartition(appId, partitionIndex));
            }
            kafkaTopics.add(kafkaTopic);
        }
    }

    public static List<KafkaTopic> listKafkaTopics() {
        return kafkaTopics;
    }

    public static List<String> listKafkaTopicNames() {
        return kafkaTopics.stream().map(topic -> topic.getName()).collect(Collectors.toList());
    }


    public static Optional<KafkaTopic> getKafkaTopic(LogType logType) {
        if (listKafkaTopicNames().contains(logType.getName())) {
            return kafkaTopics.stream().filter(p -> p.getName().equalsIgnoreCase(logType.getName())).findFirst();
        }
        return Optional.empty();
    }
}
