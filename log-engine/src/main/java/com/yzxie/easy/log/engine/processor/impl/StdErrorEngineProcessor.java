package com.yzxie.easy.log.engine.processor.impl;

import com.yzxie.easy.log.common.data.log.impl.StdErrorLogMessage;
import com.yzxie.easy.log.engine.processor.AbstractEngineProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xieyizun
 * @date 17/11/2018 16:10
 * @description:
 */
@Slf4j
public class StdErrorEngineProcessor extends AbstractEngineProcessor<StdErrorLogMessage> {
    @Override
    protected void process(StdErrorLogMessage logMessage) {
        log.info("process log {}", logMessage);
    }
}
