package com.bestzpr.tally.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @className DateUtil
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/21 23:00
 * @Version 1.0
 */
public class DateUtil {

    // SimpleDateFormat不是线程安全的，所以不应该定义为静态变量
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String formatDate(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat(DATE_FORMAT).parse(date);
    }

    public static String formatDateTime(Date dateTime) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(dateTime);
    }

    public static Date parseDateTime(String dateTime) throws ParseException {
        return new SimpleDateFormat(DATE_TIME_FORMAT).parse(dateTime);
    }
}