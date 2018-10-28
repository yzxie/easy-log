package com.yzxie.easy.log.collector.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import java.util.Properties;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;

/**
 * @author xieyizun
 * @date 26/10/2018 17:35
 * @description:
 */
public class MessageConsumerFactory {

    public static MessageConsumer creatMessageConsumer(String topicName) {
        return create0(topicName, "default", 1);
    }

    public static MessageConsumer createMessageConsumer(String topicName, String groupId) {
        return create0(topicName, groupId, 1);
    }

    public static MessageConsumer createMessageConsumer(String topicName, String groupId, int partitions) {
        return create0(topicName, groupId, partitions);
    }

    private static MessageConsumer create0(String topicName, String groupId, int partitions) {
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(intConsumerConfig(groupId));
        return new MessageConsumer(topicName, consumerConnector, partitions);
    }

    private static ConsumerConfig intConsumerConfig(String groupId) {
        Properties props = new Properties();
        props.put("group.id", groupId);
        props.put("zookeeper.connect", "127.0.0.1:2181");

        props.put("zookeeper.connection.timeout.ms", "3000");
        props.put("zookeeper.session.timeout.ms", "30000");
        props.put("zookeeper.sync.time.ms", "5000");
        props.put("auto.commit.interval.ms", "5000");
        props.put("auto.offset.reset", "smallest");
        props.put("dual.commit.enabled", "true");
        props.put("serializer.class", "kafka.serializer.StringEncoder");

        return new ConsumerConfig(props);
    }
}
