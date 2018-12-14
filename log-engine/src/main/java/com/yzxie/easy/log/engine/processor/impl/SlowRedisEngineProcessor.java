package com.yzxie.easy.log.engine.processor.impl;

import com.yzxie.easy.log.common.data.log.impl.RedisSlowLogMessage;
import com.yzxie.easy.log.engine.processor.AbstractEngineProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xieyizun
 * @date 17/11/2018 16:11
 * @description:
 */
@Slf4j
public class SlowRedisEngineProcessor extends AbstractEngineProcessor<RedisSlowLogMessage> {
    @Override
    protected void process(RedisSlowLogMessage logMessage) {
        log.info("process log {}", logMessage);
    }
}
