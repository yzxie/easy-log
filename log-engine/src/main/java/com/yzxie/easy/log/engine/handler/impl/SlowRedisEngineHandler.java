package com.yzxie.easy.log.engine.handler.impl;

import com.yzxie.easy.log.common.data.log.impl.RedisSlowILogMessage;
import com.yzxie.easy.log.engine.handler.AbstractEngineHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xieyizun
 * @date 17/11/2018 16:11
 * @description:
 */
@Slf4j
public class SlowRedisEngineHandler extends AbstractEngineHandler<RedisSlowILogMessage> {
    @Override
    protected void process(RedisSlowILogMessage logMessage) {
        log.info("process log {}", logMessage);
    }
}
