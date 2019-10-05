package com.myava.base.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * 日期实用类
 *
 * @author biao
 */
public class DateUtil extends DateUtils {

    public static final String DATE_TIME_SIMPLE_PATTERN = "yyyyMMddHHmmss";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_MONTH_DAY_PATTERN = "MM/dd";
    public static final String DATE_HOUR_MINUTE_PATTERN = "HH:mm";

    /**
     * 获取日期字符串，模式：yyyy-MM-dd
     * @param date
     * @return
     */
    public static String getDateStr(Date date) {
        return DateFormatUtils.format(date, DATE_PATTERN);
    }

    /**
     * 获取月份日期字符串，模式：MM/dd
     * @param date
     * @return
     */
    public static String getMonthDayStr(Date date) {
        return DateFormatUtils.format(date, DATE_MONTH_DAY_PATTERN);
    }

    /**
     * 获取小时分钟字符串，模式：HH:mm
     * @param date
     * @return
     */
    public static String getHourMinuteStr(Date date) {
        return DateFormatUtils.format(date, DATE_HOUR_MINUTE_PATTERN);
    }

    /**
     * 获取两个时间相差分钟数
     * @param d1
     * @param d2
     * @return
     */
    public static long getDiffMinutes(Date d1, Date d2) {
        return getDiffSeconds(d1, d2) / 60;
    }

    /**
     * 获取两个时间相差秒数
     * @param d1
     * @param d2
     * @return
     */
    public static long getDiffSeconds(Date d1, Date d2) {
        return (d2.getTime() - d1.getTime()) / 1000;
    }

}