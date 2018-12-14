package com.yzxie.easy.log.engine.processor.impl;

import com.yzxie.easy.log.common.data.log.impl.MySQLSlowLogMessage;
import com.yzxie.easy.log.engine.processor.AbstractEngineProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xieyizun
 * @date 17/11/2018 16:11
 * @description:
 */
@Slf4j
public class SlowMySQLEngineProcessor extends AbstractEngineProcessor<MySQLSlowLogMessage> {
    @Override
    protected void process(MySQLSlowLogMessage content) {
        log.info("process log {}", content);
    }
}
