package com.yzxie.easy.log.collector.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

/**
 * @author xieyizun
 * @date 26/10/2018 17:35
 * @description:
 */
public class MessageConsumerFactory {

    public static MessageConsumer createMessageConsumer() {
        return null;
    }

    private ConsumerConfig intConsumerConfig() {
        Properties properties = new Properties();
        return new ConsumerConfig(properties);
    }
}
