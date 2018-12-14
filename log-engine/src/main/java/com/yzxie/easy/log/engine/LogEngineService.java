package com.yzxie.easy.log.engine;

import com.yzxie.easy.log.common.data.log.ILogMessage;
import com.yzxie.easy.log.common.data.log.LogType;
import com.yzxie.easy.log.common.service.AbstractService;
import com.yzxie.easy.log.engine.processor.EngineProcessorFactory;
import com.yzxie.easy.log.engine.processor.IEngineProcessor;
import com.yzxie.easy.log.engine.push.WebPushService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xieyizun
 * @date 26/10/2018 15:34
 * @description:
 */
@Slf4j
public class LogEngineService extends AbstractService {
    private static Map<LogType, IEngineProcessor> logEngineProcessors = new HashMap<>(LogType.size());

    @Override
    public void start() {
        for (LogType logTypeSupport : LogType.values()) {
            logEngineProcessors.put(logTypeSupport, EngineProcessorFactory.getEngineHandler(logTypeSupport).startUp());
        }
        log.info("LogEngineService started successfully");

        startNext();
    }

    @Override
    public void stop() {
        stopNext();
    }

    public static void dispatch(ILogMessage logMessage) {
        if (logEngineProcessors.get(logMessage.getLogType()) != null) {
            logEngineProcessors.get(logMessage.getLogType()).handle(logMessage);
        } else {
            log.error("logType: {} is not supported.", logMessage.getLogType());
        }
    }

    /**
     * 单例实现
     */
    private LogEngineService() {}

    private static class InstanceHolder {
        public static LogEngineService instance = new LogEngineService();
    }

    public static LogEngineService getInstance() {
        return InstanceHolder.instance;
    }
}
