package com.yzxie.easy.log.common.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author xieyizun
 * @date 18/11/2018 16:17
 * @description:
 */
public class TimeUtils {
    public static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public static DateTime parseDateTime(String dateTimeStr) {
        return fmt.parseDateTime(dateTimeStr);
    }

    public static long parseTimeStamp(String dateTimeStr) {
        DateTime dateTime = parseDateTime(dateTimeStr);
        return dateTime.getMillis();
    }
}
