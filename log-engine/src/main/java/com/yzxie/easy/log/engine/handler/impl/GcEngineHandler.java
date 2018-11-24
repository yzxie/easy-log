package com.yzxie.easy.log.engine.handler.impl;

import com.yzxie.easy.log.common.data.log.impl.GcLogMessage;
import com.yzxie.easy.log.engine.handler.AbstractEngineHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xieyizun
 * @date 17/11/2018 16:10
 * @description:
 */
@Slf4j
public class GcEngineHandler extends AbstractEngineHandler<GcLogMessage> {
    @Override
    protected void process(GcLogMessage logMessage) {
        log.info("process log {}", logMessage);
    }
}
