package com.yzxie.easy.log.engine.processor;

import com.yzxie.easy.log.common.data.log.LogType;
import com.yzxie.easy.log.engine.processor.impl.*;

/**
 * @author xieyizun
 * @date 17/11/2018 16:33
 * @description:
 */
public class EngineProcessorFactory {
    public static IEngineProcessor getEngineHandler(LogType logType) {
        switch (logType) {
            case STDOUT:
                return new StdOutEngineProcessor();
            case STDERROR:
                return new StdErrorEngineProcessor();
            case GC:
                return new GcEngineProcessor();
            case MYSQL_SLOW_LOG:
                return new SlowMySQLEngineProcessor();
            case REDIS_SLOW_LOG:
                return new SlowRedisEngineProcessor();
            default:
                return null;
        }
    }
}
