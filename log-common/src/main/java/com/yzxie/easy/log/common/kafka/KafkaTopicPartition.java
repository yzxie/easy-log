package com.yzxie.easy.log.common.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xieyizun
 * @date 25/11/2018 10:21
 * @description:
 */
@Data
@AllArgsConstructor
public class KafkaTopicPartition {
    private String appId;
    private Integer index;
}
