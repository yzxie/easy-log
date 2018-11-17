package com.yzxie.easy.log.common.kafka;

import lombok.Data;

import java.util.List;

/**
 * @author xieyizun
 * @date 17/11/2018 14:06
 * @description:
 */
@Data
public class KafkaGroup {
    private String groupId;
    private List<KafkaTopic> kafkaTopicList;
}
