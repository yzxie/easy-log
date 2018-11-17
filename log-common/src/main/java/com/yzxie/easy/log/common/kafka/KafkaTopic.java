package com.yzxie.easy.log.common.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xieyizun
 * @date 17/11/2018 10:48
 * @description:
 */
@Data
@AllArgsConstructor
public class KafkaTopic {
    private String name;
    private Integer partitions;
}
