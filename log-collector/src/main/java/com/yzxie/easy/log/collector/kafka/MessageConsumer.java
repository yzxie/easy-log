package com.yzxie.easy.log.collector.kafka;

import com.google.common.collect.ImmutableMap;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.ConsumerTimeoutException;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xieyizun
 * @date 26/10/2018 17:35
 * @description:
 */
public class MessageConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(MessageConsumer.class);

    private String topicName;
    private int partitions = 1;
    /**
     * each partition of this topic with one iterator to get message in it.
     * the size is equal to partitions of this topic.
     */
    private List<ConsumerIterator<byte[], byte[]>> partitionsIterators;
    private ConsumerConnector consumerConnector;

    public MessageConsumer(String topicName, ConsumerConnector consumerConnector) {
        this(topicName, consumerConnector, 1);
    }

    public MessageConsumer(String topicName, ConsumerConnector consumerConnector, int partitions) {
        if (partitions < 0) {
            throw new RuntimeException("construct message consumer failure");
        }
        this.topicName = topicName;
        this.partitions = partitions;
        this.partitionsIterators = new ArrayList<>(partitions);
        this.consumerConnector = consumerConnector;

        init();
    }

    /**
     * init consumer and partitions iterator.
     */
    public void init() {
        LOG.info("MessageConsumer for {} init starting.", topicName);

        // each partition attach to a kafka stream
        Map<String, Integer> topicStreamCountMap = ImmutableMap.of(topicName, partitions);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreamMap =
                consumerConnector.createMessageStreams(topicStreamCountMap);
        for (int partitionIndex = 0; partitionIndex < partitions; partitionIndex++) {
            KafkaStream<byte[], byte[]> partitionStream =
                    consumerStreamMap.get(topicName).get(partitionIndex);
            partitionsIterators.add(partitionStream.iterator());
        }

        LOG.info("MessageConsumer for {} init successfully", topicName);
    }

    /**
     * stop consumer and commit offsets of all partitions
     */
    public void shutdown() {
        LOG.info("MessageConsumer for {} shutdown staring.", topicName);

        if (consumerConnector != null) {
            this.commitOffset();

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                LOG.error("Error while waiting consumer for {} to shutdown, {}", topicName, e);
            }

            consumerConnector.shutdown();
        }

        LOG.info("MessageConsumer for {} shutdown successfully.", topicName);
    }

    /**
     * get next message from specific partition, 0 as default
     */
    public boolean hasNext() {
        return this.hasNext(0);
    }

    public boolean hasNext(int partitionIndex) {
        if (partitionIndex > partitionsIterators.size()) {
            LOG.error("hasNext invalid param: {}", partitionIndex);
            return false;
        }
        try {
            return partitionsIterators.get(partitionIndex).hasNext();
        } catch (Exception e) {
            LOG.error("hasNext exception {}", e);
        }

        return false;
    }

    public KafkaMessage next() {
        return this.next(0);
    }

    public KafkaMessage next(int partitionIndex) {
        if (partitionIndex > partitionsIterators.size()) {
            return null;
        }

        try {
            MessageAndMetadata<byte[], byte[]> messageAndMetadata =
                    partitionsIterators.get(partitionIndex).next();
            // extract topicName, key, content
            String topicName = messageAndMetadata.topic();
            byte[] message = messageAndMetadata.message();
            String key = null;
            if (messageAndMetadata.key() != null) {
                key = new String(messageAndMetadata.key(), Charset.forName("UTF-8"));
            }
            String messageContent = new String(message, Charset.forName("UTF-8"));

            return new KafkaMessage(topicName, key, messageContent);
        } catch (ConsumerTimeoutException e) {
            LOG.error("next timeout {}", e);
        } catch (Exception e) {
            LOG.error("next receive data failed {}", e);
        }

        return null;
    }

    /**
     * sync commit offset
     */
    public void commitOffset() {
        this.consumerConnector.commitOffsets();
    }

    public String getTopicName() {
        return topicName;
    }
}
