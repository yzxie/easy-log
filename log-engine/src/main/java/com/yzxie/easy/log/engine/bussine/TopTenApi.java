package com.yzxie.easy.log.engine.bussine;

import com.yzxie.easy.log.common.data.bussine.ApiAccessStat;
import com.yzxie.easy.log.common.data.log.impl.StdOutLogMessage;
import com.yzxie.easy.log.storage.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;

/**
 * @author xieyizun
 * @date 17/11/2018 21:28
 * @description: 访问量前十的API
 */
@Slf4j
public class TopTenApi implements Runnable {
    public static final String TOP_TEN_API_PREFIX = "top_ten_api:";
    private StdOutLogMessage stdOutLogMessage;

    public TopTenApi(StdOutLogMessage stdOutLogMessage) {
        this.stdOutLogMessage = stdOutLogMessage;
    }

    @Override
    public void run() {
        String appId = stdOutLogMessage.getAppId();
        String apiPath = stdOutLogMessage.getApiPath();
        RedisUtils.increaseScore(TOP_TEN_API_PREFIX+appId, apiPath);
    }

    public static List<ApiAccessStat> getTopTenAPis(String appId) {
        List<ApiAccessStat> apiAccessStats = new ArrayList<>(10);

        RedisTemplate redisTemplate = RedisUtils.getRedisTemplate();
        BoundZSetOperations operations = redisTemplate.boundZSetOps(TOP_TEN_API_PREFIX+appId);
        Set<ZSetOperations.TypedTuple<String>> valuesWithScore = operations.rangeWithScores(0, 10);

        for (ZSetOperations.TypedTuple<String> record : valuesWithScore) {
            ApiAccessStat apiAccessStat = new ApiAccessStat();
            String apiPath = record.getValue();
            double requestCount = record.getScore();

            apiAccessStat.setAppId(appId);
            apiAccessStat.setApiPath(apiPath);
            apiAccessStat.setRequestCount(requestCount);
            apiAccessStats.add(apiAccessStat);
        }

        return apiAccessStats;
    }
}
