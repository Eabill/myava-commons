package com.myava.base.util;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * 日期实用类
 *
 * @author biao
 */
public class DateUtil extends DateUtils {

    /**
     * 获取两个时间相差分钟数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long getDiffMinutes(Date d1, Date d2) {
        return getDiffSeconds(d1, d2) / 60;
    }

    /**
     * 获取两个时间相差秒数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long getDiffSeconds(Date d1, Date d2) {
        return Math.abs((d2.getTime() - d1.getTime()) / 1000);
    }

}