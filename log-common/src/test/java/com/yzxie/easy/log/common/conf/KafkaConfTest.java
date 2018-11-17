package com.yzxie.easy.log.common.conf;

import com.yzxie.easy.log.common.kafka.KafkaGroup;
import com.yzxie.easy.log.common.kafka.KafkaTopic;
import org.junit.Test;

/**
 * @author xieyizun
 * @date 17/11/2018 15:38
 * @description:
 */
public class KafkaConfTest {

    @Test
    public void testKafkaTopicsParse() {
        for (KafkaGroup kafkaGroup : KafkaConfig.listKafkaGroup()) {
            System.out.println(kafkaGroup.getGroupId());
            for (KafkaTopic kafkaTopic : kafkaGroup.getKafkaTopicList()) {
                System.out.print(kafkaTopic.getName() + " " + kafkaTopic.getPartitions() + ", ");
            }
            System.out.println();
        }
    }
}
