package com.yzxie.easy.log.storage;

import com.yzxie.easy.log.common.data.log.ILogMessage;
import com.yzxie.easy.log.common.service.AbstractService;
import com.yzxie.easy.log.storage.processor.impl.HBaseProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xieyizun
 * @date 26/10/2018 15:37
 * @description:
 */
@Slf4j
public class LogStorageService extends AbstractService {

    @Override
    public void start() {
        log.info("LogStorageService started.");

        startNext();
    }

    @Override
    public void stop() {
        log.info("LogStorageService stopped.");

        stopNext();
    }

    public static void dispatch(ILogMessage logMessage) {
        HBaseProcessor.getInstance().save(logMessage);
        log.info("store log message: {} {}", logMessage.getLogType(), logMessage.getContent());
    }
}
