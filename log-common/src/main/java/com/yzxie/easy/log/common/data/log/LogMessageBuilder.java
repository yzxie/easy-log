package com.yzxie.easy.log.common.data.log;

import com.yzxie.easy.log.common.data.log.impl.*;

import java.util.List;

/**
 * @author xieyizun
 * @date 17/11/2018 20:19
 * @description:
 */
public class LogMessageBuilder {
    private static final List<String> supportLogTypes = LogType.getNames();

    public static ILogMessage createLogMessage(String appId, String logType, String content) {
        if (supportLogTypes.contains(logType)) {
            switch (LogType.valueOf(logType.toUpperCase())) {
                case STDOUT:
                    return stdOutILogMessage(appId, content);
                case STDERROR:
                    return stdErrorILogMessage(appId, content);
                case GC:
                    return gcILogMessage(appId, content);
                case MYSQL_SLOW_LOG:
                    return mySQLSlowILogMessage(appId, content);
                case REDIS_SLOW_LOG:
                    return redisSlowILogMessage(appId, content);
            }
        }
        return null;
    }

    public static StdOutILogMessage stdOutILogMessage(String appId, String content) {

        return new StdOutILogMessage(appId, content);
    }

    public static StdErrorILogMessage stdErrorILogMessage(String appId, String content) {

        return new StdErrorILogMessage(appId, content);
    }

    public static GcILogMessage gcILogMessage(String appId, String content) {

        return new GcILogMessage(appId, content);
    }

    public static MySQLSlowILogMessage mySQLSlowILogMessage(String appId, String content) {

        return new MySQLSlowILogMessage(appId, content);
    }

    public static RedisSlowILogMessage redisSlowILogMessage(String appId, String content) {

        return new RedisSlowILogMessage(appId, content);
    }
}
