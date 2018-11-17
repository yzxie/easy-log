package com.yzxie.easy.log.engine.handler;

import com.yzxie.easy.log.common.data.log.LogType;
import com.yzxie.easy.log.engine.handler.impl.*;

/**
 * @author xieyizun
 * @date 17/11/2018 16:33
 * @description:
 */
public class EngineHandlerFactory {
    public static IEngineHandler getEngineHandler(LogType logType) {
        switch (logType) {
            case STDOUT:
                return new StdOutEngineHandler();
            case STDERROR:
                return new StdErrorEngineHandler();
            case GC:
                return new GcEngineHandler();
            case MYSQL_SLOW_LOG:
                return new SlowMySQLEngineHandler();
            case REDIS_SLOW_LOG:
                return new SlowRedisEngineHandler();
            default:
                return null;
        }
    }
}
