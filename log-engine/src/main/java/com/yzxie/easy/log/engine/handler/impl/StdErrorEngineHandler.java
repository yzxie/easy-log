package com.yzxie.easy.log.engine.handler.impl;

import com.yzxie.easy.log.common.data.log.impl.StdErrorILogMessage;
import com.yzxie.easy.log.engine.handler.AbstractEngineHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xieyizun
 * @date 17/11/2018 16:10
 * @description:
 */
@Slf4j
public class StdErrorEngineHandler extends AbstractEngineHandler<StdErrorILogMessage> {
    @Override
    protected void process(StdErrorILogMessage logMessage) {
        log.info("process log {}", logMessage);
    }
}
