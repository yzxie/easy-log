package com.yzxie.easy.log.engine.push;

import com.alibaba.fastjson.JSONObject;
import com.yzxie.easy.log.engine.bussine.TopTenApi;
import com.yzxie.easy.log.engine.netty.NettyClient;
import com.yzxie.easy.log.engine.netty.NettyConstants;
import com.yzxie.easy.log.storage.handler.RedisHandler;

import java.util.List;
import java.util.Map;
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

    public WebPushService() {
        // 异步启动netty与服务端的连接，避免服务器还没启动
        ScheduledExecutorService asyncStartUpService = Executors.newSingleThreadScheduledExecutor();
        asyncStartUpService.schedule(new NettyClient.AsyncStartUpTask(nettyClient), 20000, TimeUnit.MILLISECONDS);

        // 每30秒推送一次访问量top10的api
        ScheduledExecutorService topTenApiPush = Executors.newSingleThreadScheduledExecutor();
        topTenApiPush.scheduleWithFixedDelay(new PushTopTenApiTask(), 30, 30, TimeUnit.SECONDS);
    }

    private class PushTopTenApiTask implements Runnable {
        @Override
        public void run() {
            List<Map<String, Object>> topTenApis = RedisHandler.getTop10WithScore(TopTenApi.REDIS_KEY);
            JSONObject pushData = new JSONObject();
            pushData.put("topTenApis", topTenApis);
            nettyClient.sendMessage(pushData);
        }
    }

}
