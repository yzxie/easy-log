package com.yzxie.easy.log.common.data.log;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xieyizun
 * @date 17/11/2018 20:19
 * @description:
 */
public enum LogType {
    STDOUT("StdOut"), STDERROR("StdError"), GC("Gc"), REDIS_SLOW_LOG("RedisSlowILogMessage"),
    MYSQL_SLOW_LOG("MySQLSlowILogMessage");

    private final String name;

    LogType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static int size() {
        return LogType.values().length;
    }

    public static List<String> getNames() {
        return Arrays.stream(LogType.values()).map(value -> value.getName()).collect(Collectors.toList());
    }
}
