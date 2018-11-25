package com.yzxie.easy.log.rpc.service;

import com.yzxie.easy.log.common.api.LogConfServiceApi;
import com.yzxie.easy.log.common.conf.KafkaConfig;
import com.yzxie.easy.log.common.kafka.KafkaTopic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xieyizun
 * @date 18/11/2018 22:35
 * @description:
 */
public class LogConfService implements LogConfServiceApi {
    private List<KafkaTopic> kafkaTopics = KafkaConfig.listKafkaTopics();

    @Override
    public List<String> listAppId() {
//        List<String> appIds = new ArrayList<>(kafkaGroups.size());
//        for (KafkaGroup kafkaGroup : kafkaGroups) {
//            appIds.add(kafkaGroup.getGroupId());
//        }
//        return appIds;
        return  null;
    }
}
