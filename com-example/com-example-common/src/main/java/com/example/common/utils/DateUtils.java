package com.example.common.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description 时间转换类
 * @PackagePath com.example.common.utils.DateUtils
 * @Author YINZHIYU
 * @Date 2020/5/8 13:48
 * @Version 1.0.0.0
 **/
public class DateUtils {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DatePattern.NORM_DATE_PATTERN);
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);

    /*
     * @Description 根据时区对应的本地时间获取UTC时间
     * @Params ==>
     * @Param timeZone
     * @Return java.lang.String
     * @Date 2020/4/21 10:00
     * @Auther YINZHIYU
     */
    public static String getUTCTimeStrByTimeZone(TimeZone timeZone) {
        Calendar cal = Calendar.getInstance(timeZone);

        return localToUTC(cal.getTime(), dateTimeFormat);
    }

    /*
     * @Description 根据本地时间获取UTC时间
     * @Params ==>
     * @Param locale
     * @Return java.lang.String
     * @Date 2020/4/21 10:00
     * @Auther YINZHIYU
     */
    public static String getUTCTimeStrByLocale(Locale locale) {
        Calendar usCal = Calendar.getInstance(locale);
        return localToUTC(usCal.getTime(), dateTimeFormat);
    }

    /*
     * @Description 根据本地时间间隔获取UTC时间
     * @Params ==>
     * @Param locale
     * @Param offset
     * @Return java.lang.String
     * @Date 2020/4/21 10:00
     * @Auther YINZHIYU
     */
    public static String getUTCTimeStrByLocaleOffset(Locale locale, int offset) {
        String utcOffsetDate;

        try {
            Date utcDate = dateFormat.parse(DateUtils.getUTCTimeStrByLocale(locale));
            Date offsetDate = DateUtil.offsetDay(utcDate, offset);
            utcOffsetDate = dateFormat.format(offsetDate);
        } catch (ParseException e) {
            utcOffsetDate = "";
        }
        return utcOffsetDate;
    }

    /*
     * @Description 得到本地时间对应的UTC日期，类型为字符串，格式为"yyyy-MM-dd" 如果获取失败，返回null
     * @Params ==>
     * @Return java.lang.String
     * @Date 2020/4/21 10:01
     * @Auther YINZHIYU
     */
    public static String getUTCDateStr() {
        StringBuilder utcDateBuffer = new StringBuilder();
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        utcDateBuffer.append(year).append("-").append(month).append("-").append(day);
        try {
            dateFormat.parse(utcDateBuffer.toString());
            return utcDateBuffer.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * @Description 本地时间转化为UTC时间
     * @Params ==>
     * @Param local
     * @Return java.lang.String
     * @Date 2020/4/21 10:02
     * @Auther YINZHIYU
     */
    public static String localToUTC(Date local, SimpleDateFormat sdf) {

        Date localDate = null;
        try {
            localDate = sdf.parse(sdf.format(local));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long localTimeInMillis = localDate.getTime();
        /* long时间转换成Calendar */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localTimeInMillis);
        /* 取得时间偏移量 */
        int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
        /* 取得夏令时差 */
        int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
        /* 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        /* 取得的时间就是UTC标准时间 */
        Date utcDate = new Date(calendar.getTimeInMillis());
        return sdf.format(utcDate);
    }

    /*
     * @Description UTC时间转化为本地时间
     * @Params ==>
     * @Param utc
     * @Return java.lang.String
     * @Date 2020/4/21 10:02
     * @Auther YINZHIYU
     */
    public static String utcToLocal(Date utc) {
        /*创建时区对象utcZone，获取utc所在的时区*/
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = null;
        try {
            utcDate = dateTimeFormat.parse(dateTimeFormat.format(utc));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateTimeFormat.setTimeZone(TimeZone.getDefault());
        Date locatlDate = null;
        String localTime = dateTimeFormat.format(utcDate.getTime());
        try {
            locatlDate = dateTimeFormat.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimeFormat.format(locatlDate);
    }

    /*
     * @Description 本地时间转化为UTC时间
     * @Params ==>
     * @Param localTime
     * @Return java.lang.String
     * @Date 2020/4/21 10:02
     * @Auther YINZHIYU
     */
    public static String localToUTC(String localTime) {
        Date localDate = null;
        try {
            localDate = dateTimeFormat.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long localTimeInMillis = localDate.getTime();
        /* long时间转换成Calendar */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localTimeInMillis);
        /* 取得时间偏移量 */
        int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
        /* 取得夏令时差 */
        int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
        /* 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        /* 取得的时间就是UTC标准时间 */
        Date utcDate = new Date(calendar.getTimeInMillis());
        return dateTimeFormat.format(utcDate);
    }

    /*
     * @Description UTC时间转化为本地时间
     * @Params ==>
     * @Param utcTime
     * @Return java.lang.String
     * @Date 2020/4/21 10:02
     * @Auther YINZHIYU
     */
    public static String utcToLocal(String utcTime) {
        /*创建时区对象utcZone，获取utc所在的时区*/
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = null;
        try {
            utcDate = dateTimeFormat.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateTimeFormat.setTimeZone(TimeZone.getDefault());
        Date locatlDate = null;
        String localTime = dateTimeFormat.format(utcDate.getTime());
        try {
            locatlDate = dateTimeFormat.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimeFormat.format(locatlDate);
    }

    /*
     * @Description 获得任意时区的时间
     * @Params ==>
     * @Param timeZoneOffset
     * @Return java.lang.String
     * @Date 2020/4/21 10:02
     * @Auther YINZHIYU
     */
    public static String getFormatedDateString(float timeZoneOffset) {

        if (timeZoneOffset > 13 || timeZoneOffset < -12) {
            timeZoneOffset = 0;
        }
        int newTime = (int) (timeZoneOffset * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }

        dateTimeFormat.setTimeZone(timeZone);
        return dateTimeFormat.format(new Date());
    }
}
