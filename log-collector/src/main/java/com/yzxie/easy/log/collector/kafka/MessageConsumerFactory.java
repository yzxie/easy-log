package com.yzxie.easy.log.collector.kafka;

import com.yzxie.easy.log.common.kafka.KafkaTopic;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import java.util.Properties;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

/**
 * @author xieyizun
 * @date 26/10/2018 17:35
 * @description:
 */
public class MessageConsumerFactory {
    public static MessageConsumer createMessageConsumer(KafkaTopic kafkaTopic, String groupId) {
        return create(kafkaTopic, groupId);
    }

    private static MessageConsumer create(KafkaTopic kafkaTopic, String groupId) {
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(intConsumerConfig(groupId));
        return new MessageConsumer(kafkaTopic, consumerConnector);
    }

    private static ConsumerConfig intConsumerConfig(String groupId) {
        Properties props = new Properties();
        props.put(GROUP_ID_CONFIG, groupId);
        props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");
        props.put(AUTO_OFFSET_RESET_CONFIG, "smallest");

        props.put("zookeeper.connect", "127.0.0.1:2181");
        props.put("zookeeper.connection.timeout.ms", "3000");
        props.put("zookeeper.session.timeout.ms", "30000");
        props.put("zookeeper.sync.time.ms", "5000");
        props.put("dual.commit.enabled", "true");
        props.put("serializer.class", "kafka.serializer.StringEncoder");

        return new ConsumerConfig(props);
    }
}
