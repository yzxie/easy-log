package com.yzxie.easy.log.rpc.service;

import com.yzxie.easy.log.common.api.LogConfServiceApi;
import com.yzxie.easy.log.common.conf.KafkaConfig;
import com.yzxie.easy.log.common.data.rpc.LogTypeWithApps;
import com.yzxie.easy.log.common.kafka.KafkaTopic;
import com.yzxie.easy.log.common.kafka.KafkaTopicPartition;

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
    public List<LogTypeWithApps> listLogTypeAndApps() {
        List<LogTypeWithApps> logTypeWithAppsList = new ArrayList<>();
        for (KafkaTopic kafkaTopic : kafkaTopics) {
            LogTypeWithApps logTypeWithApps = new LogTypeWithApps();
            logTypeWithApps.setLogType(kafkaTopic.getName());
            List<String> apps = new ArrayList<>(kafkaTopic.getPartitions().size());
            for (KafkaTopicPartition partition : kafkaTopic.getPartitions()) {
                apps.add(partition.getAppId());
            }
            logTypeWithApps.setApps(apps);
            logTypeWithAppsList.add(logTypeWithApps);
        }
        return logTypeWithAppsList;
    }
}
