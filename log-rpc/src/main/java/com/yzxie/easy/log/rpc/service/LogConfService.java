package com.yzxie.easy.log.rpc.service;

import com.yzxie.easy.log.common.api.LogConfServiceApi;
import com.yzxie.easy.log.common.conf.KafkaConfig;
import com.yzxie.easy.log.common.kafka.KafkaGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xieyizun
 * @date 18/11/2018 22:35
 * @description:
 */
public class LogConfService implements LogConfServiceApi {
    private List<KafkaGroup> kafkaGroups = KafkaConfig.listKafkaGroup();

    @Override
    public List<String> listAppId() {
        List<String> appIds = new ArrayList<>(kafkaGroups.size());
        for (KafkaGroup kafkaGroup : kafkaGroups) {
            appIds.add(kafkaGroup.getGroupId());
        }
        return appIds;
    }
}
