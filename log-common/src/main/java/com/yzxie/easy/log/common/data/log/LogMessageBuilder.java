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

    public static ILogMessage createLogMessage(String logType, String content) {
        if (supportLogTypes.contains(logType)) {
//            switch (LogType.valueOf(logType)) {
            switch (logType) {
                case "StdOut":
                    return stdOutILogMessage(content);
//                case STDERROR:
//                    return stdErrorILogMessage(content);
//                case GC:
//                    return gcILogMessage(content);
//                case MYSQL_SLOW_LOG:
//                    return mySQLSlowILogMessage(content);
//                case REDIS_SLOW_LOG:
//                    return redisSlowILogMessage(content);
            }
        }
        return null;
    }

    public static StdOutILogMessage stdOutILogMessage(String content) {
        return new StdOutILogMessage(content);
    }

    public static StdErrorILogMessage stdErrorILogMessage(String content) {
        return new StdErrorILogMessage(content);
    }

    public static GcILogMessage gcILogMessage(String content) {
        return new GcILogMessage(content);
    }

    public static MySQLSlowILogMessage mySQLSlowILogMessage(String content) {
        return new MySQLSlowILogMessage(content);
    }

    public static RedisSlowILogMessage redisSlowILogMessage(String content) {
        return new RedisSlowILogMessage(content);
    }
}
