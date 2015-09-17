package org.cnodejs.android.md.util;

import org.joda.time.DateTime;

import java.util.Date;

public final class FormatUtils {

    private FormatUtils() {}

    private static final long minute = 60 * 1000; //分钟
    private static final long hour = 60 * minute; //小时
    private static final long day = 24 * hour;    //天
    private static final long week = 7 * day;     //周
    private static final long month = 31 * day;   //月
    private static final long year = 12 * month;  //年

    public static String getRecentlyTimeFormatText(DateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        long diff = new Date().getTime() - dateTime.getMillis();
        long r;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > week) {
            r = (diff / week);
            return r + "周前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

}
