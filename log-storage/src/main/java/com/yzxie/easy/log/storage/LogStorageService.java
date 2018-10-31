package com.yzxie.easy.log.storage;

import com.yzxie.easy.log.common.service.AbstractService;
import com.yzxie.easy.log.storage.handler.RedisHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xieyizun
 * @date 26/10/2018 15:37
 * @description:
 */
public class LogStorageService extends AbstractService {
    private static final Logger LOG = LoggerFactory.getLogger(LogStorageService.class);

    @Override
    public void start() {
        LOG.info("LogStorageService started.");

        startNext();
    }

    @Override
    public void stop() {
        LOG.info("LogStorageService stopped.");

        stopNext();
    }

    public static void dispatch(String topicName, String content) {
        RedisHandler.rightPushList(topicName, content);
    }
}
