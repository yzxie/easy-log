package com.yzxie.easy.log.engine.handler.impl;

import com.yzxie.easy.log.common.data.log.impl.MySQLSlowILogMessage;
import com.yzxie.easy.log.engine.handler.AbstractEngineHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xieyizun
 * @date 17/11/2018 16:11
 * @description:
 */
@Slf4j
public class SlowMySQLEngineHandler extends AbstractEngineHandler<MySQLSlowILogMessage> {
    @Override
    protected void process(MySQLSlowILogMessage content) {
        log.info("process log {}", content);
    }
}
