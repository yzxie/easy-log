package com.yzxie.easy.log.storage.util;

import org.junit.Test;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.CountDownLatch;

/**
 * @author xieyizun
 * @date 18/11/2018 14:28
 * @description:
 */
public class RedisHandlerTest {
    private RedisTemplate redisTemplate = RedisUtils.getRedisTemplate();

    @Test
    public void testRedisTemplateThreadSafe() {
        try {
            CountDownLatch begin = new CountDownLatch(1); // 同时启动开关
            CountDownLatch end = new CountDownLatch(40); // 40个并发线程
            for (int i = 0; i < 40; i++) {
                Thread thread = new Thread(new RedisOpTask(begin, end));
                thread.start();
            }
            begin.countDown();
            end.await();

            BoundZSetOperations operations = redisTemplate.boundZSetOps("test_thread_safe");
            System.out.println(operations.score("sum"));
        } catch (Exception e) {

        }
    }

    private class RedisOpTask implements Runnable {
        private CountDownLatch begin;
        private CountDownLatch end;

        public RedisOpTask(CountDownLatch begin, CountDownLatch end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        public void run() {
            try {
                begin.await();
                System.out.println(System.currentTimeMillis());
                //RedisUtils.increaseScore("test_thread_safe", "sum", 1);
                redisTemplate.opsForZSet().incrementScore("test_thread_safe", "sum", 1);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                end.countDown();
            }
        }
    }
}
