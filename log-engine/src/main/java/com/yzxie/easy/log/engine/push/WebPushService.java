package com.yzxie.easy.log.engine.push;

import com.alibaba.fastjson.JSONObject;
import com.yzxie.easy.log.common.conf.KafkaConfig;
import com.yzxie.easy.log.common.data.bussine.ApiAccessStat;
import com.yzxie.easy.log.common.data.bussine.SecondRequestStat;
import com.yzxie.easy.log.common.kafka.KafkaGroup;
import com.yzxie.easy.log.engine.bussine.SecondLevelFlow;
import com.yzxie.easy.log.engine.bussine.TopTenApi;
import com.yzxie.easy.log.engine.netty.NettyClient;
import com.yzxie.easy.log.engine.netty.NettyConstants;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xieyizun
 * @date 17/11/2018 23:26
 * @description: 定期推送各种日志分析结果给web展示
 */
public class WebPushService {
    // 每个处理器使用一个nettyClient发送消息给easy web
    private NettyClient nettyClient = new NettyClient(NettyConstants.SERVER_HOST, NettyConstants.SERVER_PORT);
    private List<KafkaGroup>  kafkaGroups;

    public WebPushService() {
        this.kafkaGroups = KafkaConfig.listKafkaGroup();

        // 异步启动netty与服务端的连接，避免服务器还没启动
        ScheduledExecutorService asyncStartUpService = Executors.newSingleThreadScheduledExecutor();
        asyncStartUpService.schedule(new NettyClient.AsyncStartUpTask(nettyClient), 20000, TimeUnit.MILLISECONDS);

        // 每30秒推送一次访问量top10的api
        ScheduledExecutorService topTenApiPush = Executors.newSingleThreadScheduledExecutor();
        topTenApiPush.scheduleAtFixedRate(new PushTopTenApiTask(), 30, 30, TimeUnit.SECONDS);

        // 每30秒推送一次最近300秒的每秒的访问统计
        ScheduledExecutorService secondLevelFlowPush = Executors.newSingleThreadScheduledExecutor();
        secondLevelFlowPush.scheduleAtFixedRate(new PushSecondLevelFlowTask(), 5, 5, TimeUnit.SECONDS);
    }

    private class PushTopTenApiTask implements Runnable {
        @Override
        public void run() {
            JSONObject pushData = new JSONObject();

            for (KafkaGroup kafkaGroup : kafkaGroups) {
                String appId = kafkaGroup.getGroupId();
                List<ApiAccessStat> topTenApis = TopTenApi.getTopTenAPis(appId);
                pushData.put(appId, topTenApis);
            }

            nettyClient.sendMessage(pushData);
        }
    }

    private class PushSecondLevelFlowTask implements Runnable {
        @Override
        public void run() {
            JSONObject pushData = new JSONObject();

            for (KafkaGroup kafkaGroup : kafkaGroups) {
                String appId = kafkaGroup.getGroupId();
                List<SecondRequestStat> secondRequestStats = SecondLevelFlow.getSecondRequestStat(appId);
                pushData.put(appId, secondRequestStats);
            }

            nettyClient.sendMessage(pushData);
        }
    }
}
