package com.yzxie.easy.log.collector.handler;

import com.yzxie.easy.log.collector.kafka.Message;
import com.yzxie.easy.log.collector.kafka.MessageConsumer;
import com.yzxie.easy.log.collector.kafka.MessageConsumerFactory;
import com.yzxie.easy.log.common.data.log.LogMessageBuilder;
import com.yzxie.easy.log.common.kafka.KafkaTopic;
import com.yzxie.easy.log.common.kafka.KafkaTopicPartition;
import com.yzxie.easy.log.engine.LogEngineService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xieyizun
 * @date 25/11/2018 10:43
 * @description: 每个主题对应一个消息处理器，主题的每个分区对应一个应用
 */
@Slf4j
public class LogMessageHandler {
    public static final String KAFKA_CONSUMER_GROUP = "LOG_COLLECTOR";
    private KafkaTopic kafkaTopic;
    private MessageConsumer messageConsumer;
    private ExecutorService appLogProcessPool;

    public LogMessageHandler(KafkaTopic kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
        appLogProcessPool = Executors.newFixedThreadPool(this.kafkaTopic.getPartitions().size());
        messageConsumer = MessageConsumerFactory.createMessageConsumer(kafkaTopic, KAFKA_CONSUMER_GROUP);

        for (KafkaTopicPartition partition : kafkaTopic.getPartitions()) {
            appLogProcessPool.execute(new PartitionMessageProcessor(partition.getAppId(), partition.getIndex()));
        }
    }

    /**
     * 主题的每个分区对应一个线程处理器
     */
    private class PartitionMessageProcessor implements Runnable {
        private String appId;
        private int partitionIndex;

        public PartitionMessageProcessor(String appId, int partitionIndex) {
            this.appId = appId;
            this.partitionIndex = partitionIndex;
        }

        @Override
        public void run() {
            while(true) {
                if (messageConsumer.hasNext(partitionIndex)) {
                    Message kafkaMessage = messageConsumer.next(partitionIndex);
                    if (kafkaMessage != null) {
                        LogEngineService.dispatch(LogMessageBuilder.createLogMessage(
                                appId,
                                kafkaMessage.getTopic(),
                                kafkaMessage.getContent()));
                    }
                }
            }
        }
    }
}
