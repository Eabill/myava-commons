package com.myava.redis;

/**
 * Redis过期时间常量类
 *
 * @author biao
 */
public class Expires {

    public static final int PERMANENT = -1;

    public static final int IN_30SECOND = 1 * 30;

    public static final int IN_1MINUTE = 1 * 60;

    public static final int IN_5MINUTE = 5 * 60;

    public static final int IN_30MINUTE = 30 * 60;

    public static final int IN_1HOUR =  60 * 60;

    public static final int IN_2HOUR = 2 * 60 * 60;

    public static final int IN_6HOUR = 6 * 60 * 60;

    public static final int IN_12HOUR = 12 * 60 * 60;

    public static final int IN_1DAY = 1 * 24 * 60 * 60;

    public static final int IN_1WEEK = 7 * 24 * 60 * 60;

    public static final int IN_1MONTH = 30 * 24 * 60 * 60;

}
