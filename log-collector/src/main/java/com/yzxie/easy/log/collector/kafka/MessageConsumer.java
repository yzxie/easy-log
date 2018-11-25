package com.yzxie.easy.log.collector.kafka;

import com.google.common.collect.ImmutableMap;
import com.yzxie.easy.log.common.kafka.KafkaTopic;
import com.yzxie.easy.log.common.kafka.KafkaTopicPartition;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.ConsumerTimeoutException;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xieyizun
 * @date 26/10/2018 17:35
 * @description: 每个主题对应一个MessageConsumer
 */
@Slf4j
public class MessageConsumer {
    private String groupId;
    private KafkaTopic kafkaTopic;
    /**
     * 主题的多个分区列表，每个分区对应一个迭代器
     */
    private List<ConsumerIterator<byte[], byte[]>> partitionsIterators;
    private ConsumerConnector consumerConnector;

    public MessageConsumer(String groupId, KafkaTopic kafkaTopic, ConsumerConnector consumerConnector) {
        if (kafkaTopic.getPartitions().size() < 0) {
            throw new RuntimeException("construct message consumer failure");
        }
        this.groupId = groupId;
        this.kafkaTopic = kafkaTopic;
        this.partitionsIterators = new ArrayList<>(kafkaTopic.getPartitions().size());
        this.consumerConnector = consumerConnector;
        init();
    }

    public void init() {
        log.info("MessageConsumer for {} init starting.", kafkaTopic.getName());

        // each partition attach to a kafka stream
        Map<String, Integer> topicStreamCountMap = ImmutableMap.of(kafkaTopic.getName(), kafkaTopic.getPartitions().size());
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreamMap =
                consumerConnector.createMessageStreams(topicStreamCountMap);
        for (KafkaTopicPartition partition : kafkaTopic.getPartitions()) {
            KafkaStream<byte[], byte[]> partitionStream =
                    consumerStreamMap.get(kafkaTopic.getName()).get(partition.getIndex());
            partitionsIterators.add(partitionStream.iterator());
        }

        log.info("MessageConsumer for {} init successfully", kafkaTopic.getName());
    }


    public void shutdown() {
        log.info("MessageConsumer for {} shutdown staring.", kafkaTopic.getName());

        if (consumerConnector != null) {
            this.commitOffset();

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                log.error("Error while waiting consumer for {} to shutdown, {}", kafkaTopic.getName(), e);
            }

            consumerConnector.shutdown();
        }

        log.info("MessageConsumer for {} shutdown successfully.", kafkaTopic.getName());
    }

    public boolean hasNext(int partitionIndex) {
        if (partitionIndex > partitionsIterators.size()) {
            log.error("hasNext invalid param: {}", partitionIndex);
            return false;
        }
        try {
            return partitionsIterators.get(partitionIndex).hasNext();
        } catch (Exception e) {
            log.error("hasNext exception {}", e);
        }

        return false;
    }

    public Message next(int partitionIndex) {
        if (partitionIndex > partitionsIterators.size() || partitionIndex < 0) {
            return null;
        }

        try {
            MessageAndMetadata<byte[], byte[]> messageAndMetadata =
                    partitionsIterators.get(partitionIndex).next();
            if (messageAndMetadata != null) {
                // extract topicName, key, content
                String topicName = messageAndMetadata.topic();
                byte[] message = messageAndMetadata.message();
                String key = null;
                if (messageAndMetadata.key() != null) {
                    key = new String(messageAndMetadata.key(), Charset.forName("UTF-8"));
                }
                String messageContent = new String(message, Charset.forName("UTF-8"));
                return new Message(topicName, key, messageContent);
            }

        } catch (ConsumerTimeoutException e) {
            log.error("next timeout {}", e);
        } catch (Exception e) {
            log.error("next receive data failed {}, {}, {}", e, kafkaTopic.getName());
        }

        return null;
    }

    public void commitOffset() {
        this.consumerConnector.commitOffsets();
    }

    // todo 启动时清空kafka数据
    public void consumeHistory() {}

    public final String getGroupId() {
        return groupId;
    }
}
