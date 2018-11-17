package com.yzxie.easy.log.common.conf;

import com.yzxie.easy.log.common.kafka.KafkaGroup;
import com.yzxie.easy.log.common.kafka.KafkaTopic;
import org.apache.commons.configuration.HierarchicalConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xieyizun
 * @date 29/10/2018 15:16
 * @description:
 */
public class KafkaConfig {
    public static final String GROUP_LIST = "kafka.consumerGroup";
    public static final String GROUP_Id = "id";

    public static final String TOPIC = "topic";
    public static final String TOPIC_NAME = "name";
    public static final String TOPIC_PARTITIONS = "partitions";

    private static List<KafkaGroup> kafkaGroups = new ArrayList<>();

    static {
        XmlConfig topicConfig = new XmlConfig("kafkaConfig.xml");
        List<HierarchicalConfiguration> groupList = topicConfig.getList(GROUP_LIST);
        for (HierarchicalConfiguration group : groupList) {
            String groupId = group.getString(GROUP_Id);
            List<HierarchicalConfiguration> topicList = group.configurationsAt(TOPIC);

            KafkaGroup kafkaGroup = new KafkaGroup();
            kafkaGroup.setGroupId(groupId);
            List<KafkaTopic> kafkaTopics = new ArrayList<>();
            kafkaGroup.setKafkaTopicList(kafkaTopics);
            for (HierarchicalConfiguration topic : topicList) {
                String topicName = topic.getString(TOPIC_NAME);
                int partitions = Integer.valueOf(topic.getInt(TOPIC_PARTITIONS));
                kafkaTopics.add(new KafkaTopic(topicName, partitions));
            }
            kafkaGroups.add(kafkaGroup);
        }
    }

    public static List<KafkaGroup> listKafkaGroup() {
        return kafkaGroups;
    }

}
