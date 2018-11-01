package com.yzxie.easy.log.storage.handler;

import com.yzxie.easy.log.conf.xml.StorageConfig;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;

/**
 * @author xieyizun
 * @date 31/10/2018 23:13
 * @description:
 */
public class RedisHandler {
    private static RedisTemplate<String, Object> redisTemplate;
    private static JedisConnectionFactory jedisConnectionFactory;

    static {
        /**
         * 加载配置文件
         */
        StorageConfig storageConfig = new StorageConfig();

        /**
         * jedisConnectionFactory初始化
         */
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(storageConfig.getInt(StorageConfig.REDIS_MAX_IDLE));
        jedisPoolConfig.setMaxWaitMillis(storageConfig.getInt(StorageConfig.REDIS_MAX_WAIT));
        jedisPoolConfig.setMaxTotal(storageConfig.getInt(StorageConfig.REDIS_MAX_TOTAL));
        jedisPoolConfig.setTestOnBorrow(storageConfig.getBoolean(StorageConfig.REDIS_TEST_ON_BORROW));

        jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(storageConfig.getString(StorageConfig.REDIS_HOST));
        jedisConnectionFactory.setPort(storageConfig.getInt(StorageConfig.REDIS_PORT));
        if (storageConfig.getString(StorageConfig.REDIS_PASSWORD) != null) {
            jedisConnectionFactory.setPassword(storageConfig.getString(StorageConfig.REDIS_PASSWORD));
        }
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.afterPropertiesSet();

        /**
         * redisTemplate初始化
         */
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        //处理异常
        //java.lang.IllegalArgumentException: template not initialized; call afterPropertiesSet() before using it
        redisTemplate.afterPropertiesSet();
    }

    /**
     * string相关操作
     * @param key
     * @return
     */
    public static Object getValue(String key) {
        Object value = redisTemplate.boundValueOps(key).get();
        return value;
    }

    public static void setValue(String key, Object value) {
        redisTemplate.boundValueOps(key).set(value);
    }

    /**
     * hash相关操作
     * @param key
     * @return
     */
    public static Map<String, Object> getHashEntries(String key) {
        BoundHashOperations operations = redisTemplate.boundHashOps(key);
        return operations.entries();
    }

    public static void setHashEntry(String key, String field, String value) {
        redisTemplate.boundHashOps(key).put(field, value);
    }

    /**
     * list相关操作
     * @param key
     * @return
     */
    public static List<Object> getListValues(String key) {
        BoundListOperations operations = redisTemplate.boundListOps(key);
        return operations.range(0,-1);
    }

    public static Object getLeftValueOfList(String key) {
        return redisTemplate.boundListOps(key).leftPop();
    }

    public static void rightPushList(String key, Object value) {
        redisTemplate.boundListOps(key).rightPush(value);
    }

    /**
     * zset相关操作
     */
    public static void addZSet(String key, String value, double score) {
        BoundZSetOperations operations = redisTemplate.boundZSetOps(key);
        operations.add(value, score);
    }

    public static List<Map<String, Object>> getTop10WithScore(String key) {
        List<Map<String, Object>> zSetValues = new ArrayList<>();
        BoundZSetOperations operations = redisTemplate.boundZSetOps(key);
        Set<ZSetOperations.TypedTuple<String>> valuesWithScore = operations.rangeWithScores(0, 10);

        for (ZSetOperations.TypedTuple<String> value : valuesWithScore) {
            Map<String, Object> valueScoreMap = new HashMap<>();
            valueScoreMap.put("value", value.getValue());
            valueScoreMap.put("score", value.getScore());
            zSetValues.add(valueScoreMap);
        }
        return zSetValues;
    }

    public static double getScore(String key, String value) {
        return redisTemplate.boundZSetOps(key).score(value);
    }

    public static void increaseScore(String key, String value, double score) {
        redisTemplate.boundZSetOps(key).incrementScore(value, score);
    }

    public static void increaseScore(String key, String value) {
        increaseScore(key, value, 1.0);
    }

}
