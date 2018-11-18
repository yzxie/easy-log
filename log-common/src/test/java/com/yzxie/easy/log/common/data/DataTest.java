package com.yzxie.easy.log.common.data;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

/**
 * @author xieyizun
 * @date 18/11/2018 15:13
 * @description:
 */
public class DataTest {
    @Test
    public void testStringSplit() {
        String url = "app1:/api/test";
        String[] tokens = url.split(":");
        for (int i=0; i<tokens.length; i++) {
            System.out.println(tokens[i]);
        }
    }

    @Test
    public void testDateTimeStrToTimestamp() {
        String strInputDateTime = "2018-11-12 11:11:12";
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = fmt.parseDateTime(strInputDateTime);
        System.out.println(dt.toString("yyyy/MM/dd HH:mm:ss"));
        System.out.println(dt.getMillis());
    }
}
