package com.yzxie.easy.log.collector.handler;

import com.yzxie.easy.log.collector.kafka.KafkaMessage;
import com.yzxie.easy.log.common.data.log.LogMessageBuilder;
import com.yzxie.easy.log.common.kafka.KafkaTopic;
import com.yzxie.easy.log.collector.kafka.MessageConsumer;
import com.yzxie.easy.log.collector.kafka.MessageConsumerFactory;
import com.yzxie.easy.log.engine.LogEngineService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xieyizun
 * @date 28/10/2018 17:53
 * @description: 每个topic对应一个KafkaMessageHandler
 */
@Slf4j
public class KafkaMessageHandler {
    private ExecutorService executorService;
    private final MessageConsumer messageConsumer;

    public KafkaMessageHandler(KafkaTopic topic, String groupId) {
        this.messageConsumer = MessageConsumerFactory.createMessageConsumer(topic,
                groupId);
        this.executorService = Executors.newFixedThreadPool(topic.getPartitions());
        for (int partitionIndex = 0; partitionIndex < topic.getPartitions(); partitionIndex++) {
            executorService.execute(new PartitionMessageProcessor(partitionIndex));
        }
    }

    /**
     * 主题的每个分区对应一个线程处理器
     */
    private class PartitionMessageProcessor implements Runnable {
        private int partitionIndex;

        public PartitionMessageProcessor(int partitionIndex) {
            this.partitionIndex = partitionIndex;
        }

        @Override
        public void run() {
            while(true) {
                if (messageConsumer.hasNext(partitionIndex)) {
                    KafkaMessage kafkaMessage = messageConsumer.next(partitionIndex);
                    if (kafkaMessage != null) {
                        LogEngineService.dispatch(LogMessageBuilder.createLogMessage(
                                messageConsumer.getGroupId(),
                                kafkaMessage.getTopic(),
                                kafkaMessage.getContent()));
                    }
                }
            }
        }
    }
}
