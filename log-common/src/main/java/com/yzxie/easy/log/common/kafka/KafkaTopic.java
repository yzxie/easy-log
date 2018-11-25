package com.yzxie.easy.log.common.kafka;

import lombok.Data;

import java.util.List;

/**
 * @author xieyizun
 * @date 17/11/2018 10:48
 * @description:
 */
@Data
public class KafkaTopic {
    private String name;
    private List<KafkaTopicPartition> partitions;
}
