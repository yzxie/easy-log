package com.yzxie.easy.log.collector.handler;

import com.yzxie.easy.log.collector.kafka.KafkaMessage;
import com.yzxie.easy.log.collector.kafka.MessageConsumer;
import com.yzxie.easy.log.collector.kafka.MessageConsumerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xieyizun
 * @date 28/10/2018 17:53
 * @description:
 */
public class SimpleMessageHandler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleMessageHandler.class);

    private final MessageConsumer messageConsumer;

    public SimpleMessageHandler(String topic) {
        this.messageConsumer = MessageConsumerFactory.createMessageConsumer(topic, "logCollector");
    }

    @Override
    public void run() {
        while(true) {
            if (messageConsumer.hasNext()) {
                KafkaMessage kafkaMessage = messageConsumer.next();
                //todo send to storm
                LOG.info("SimpleMessageHandler receive from {} : {}", messageConsumer.getTopicName(),
                        kafkaMessage.getContent());
            }
        }
    }
}