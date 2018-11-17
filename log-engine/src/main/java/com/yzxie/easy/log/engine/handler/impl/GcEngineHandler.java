package com.yzxie.easy.log.engine.handler.impl;

import com.yzxie.easy.log.common.data.log.impl.GcILogMessage;
import com.yzxie.easy.log.engine.handler.AbstractEngineHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xieyizun
 * @date 17/11/2018 16:10
 * @description:
 */
@Slf4j
public class GcEngineHandler extends AbstractEngineHandler<GcILogMessage> {
    @Override
    protected void process(GcILogMessage logMessage) {
        log.info("process log {}", logMessage);
    }
}
