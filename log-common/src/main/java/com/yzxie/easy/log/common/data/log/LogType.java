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
    STDOUT("STDOUT"), STDERROR("STDERROR"), GC("GC"), REDIS_SLOW_LOG("REDIS_SLOW_LOG"),
    MYSQL_SLOW_LOG("MYSQL_SLOW_LOG");

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

    public static boolean contains(String name) {
        return getNames().contains(name);
    }
}
