package com.yzxie.easy.log.engine;

import com.yzxie.easy.log.common.data.log.ILogMessage;
import com.yzxie.easy.log.common.data.log.LogType;
import com.yzxie.easy.log.common.service.AbstractService;
import com.yzxie.easy.log.engine.handler.EngineHandlerFactory;
import com.yzxie.easy.log.engine.handler.IEngineHandler;
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
    private static Map<LogType, IEngineHandler> logEngineHandlers = new HashMap<>(LogType.size());
    private WebPushService webPushService;

    @Override
    public void start() {
        for (LogType logTypeSupport : LogType.values()) {
            logEngineHandlers.put(logTypeSupport, EngineHandlerFactory.getEngineHandler(logTypeSupport));
        }
        this.webPushService = new WebPushService();
        log.info("LogEngineService started successfully");

        startNext();
    }

    @Override
    public void stop() {
        stopNext();
    }

    public static void dispatch(ILogMessage logMessage) {
        if (logEngineHandlers.get(logMessage.getLogType()) != null) {
            logEngineHandlers.get(logMessage.getLogType()).handle(logMessage);
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
