package com.yzxie.easy.log.engine.bussine;

import com.yzxie.easy.log.common.data.bussine.SecondRequestStat;
import com.yzxie.easy.log.common.data.log.impl.StdOutILogMessage;
import com.yzxie.easy.log.common.utils.TimeUtils;
import com.yzxie.easy.log.storage.handler.RedisHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author xieyizun
 * @date 17/11/2018 21:29
 * @description: 精确到秒级别，每秒请求数量统计
 */
@Slf4j
public class SecondLevelFlow implements Runnable {
    private static final String SECOND_REQUEST_PREFIX = "second_request:"; // 根据访问量作为score排序
    private static final String SECOND_TIMESTAMP_PREFIX = "second_timestamp:"; // 根据时间戳作为score排序
    private static final RedisTemplate redisTemplate = RedisHandler.getRedisTemplate();
    private static final int STAT_SECONDS = 300;

    private StdOutILogMessage stdOutILogMessage;

    public SecondLevelFlow(StdOutILogMessage stdOutILogMessage) {
        this.stdOutILogMessage = stdOutILogMessage;
    }

    @Override
    public void run() {
        String appId = stdOutILogMessage.getAppId();
        String requestTime = stdOutILogMessage.getRequestTime();
        double requestMills = TimeUtils.parseTimeStamp(requestTime);

        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                BoundZSetOperations zSetOp1 = redisTemplate.boundZSetOps(SECOND_REQUEST_PREFIX+appId);
                zSetOp1.incrementScore(requestTime, 1);
                BoundZSetOperations zSetOp2 = redisTemplate.boundZSetOps(SECOND_TIMESTAMP_PREFIX+appId);
                zSetOp2.add(requestTime, requestMills);
                operations.exec();
                return null;
            }
        });
    }

    /**
     * 获取Redis中最新的前300条，即五分钟
     * @return
     */
    public static List<SecondRequestStat> getSecondRequestStat(String appId) {
        List<SecondRequestStat> secondRequestStats = new ArrayList<>();

        Set<String> timeStampValues = redisTemplate.opsForZSet()
                .reverseRange(SECOND_TIMESTAMP_PREFIX+appId, 0, STAT_SECONDS);

         redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (String secondTimestamp : timeStampValues) {
                    Double count = redisTemplate.opsForZSet().score(SECOND_REQUEST_PREFIX+appId, secondTimestamp);
                    secondRequestStats.add(new SecondRequestStat(secondTimestamp, count));
                }
                return null;
            }
        });
        return secondRequestStats;
    }

    public static void main(String[] args) {
        SecondLevelFlow.getSecondRequestStat("app1");
    }
}
