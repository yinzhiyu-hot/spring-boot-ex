package com.example.common.utils;

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

        return localToUTC(cal.getTime(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
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
        return localToUTC(usCal.getTime(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
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

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date utcDate = format.parse(DateUtils.getUTCTimeStrByLocale(locale));
            Date offsetDate = DateUtil.offsetDay(utcDate, offset);
            utcOffsetDate = format.format(offsetDate);
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
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        StringBuffer utcDateBuffer = new StringBuffer();
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
            format.parse(utcDateBuffer.toString());
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
        /*创建格式化时间对象simpleDateFormat，并初始化格式yyyy-MM-dd HH:mm:ss*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*创建时区对象utcZone，获取utc所在的时区*/
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = null;
        try {
            utcDate = sdf.parse(sdf.format(utc));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.setTimeZone(TimeZone.getDefault());
        Date locatlDate = null;
        String localTime = sdf.format(utcDate.getTime());
        try {
            locatlDate = sdf.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(locatlDate);
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
        /*创建格式化时间对象simpleDateFormat，并初始化格式yyyy-MM-dd HH:mm:ss*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date localDate = null;
        try {
            localDate = sdf.parse(localTime);
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
     * @Param utcTime
     * @Return java.lang.String
     * @Date 2020/4/21 10:02
     * @Auther YINZHIYU
     */
    public static String utcToLocal(String utcTime) {
        /*创建格式化时间对象simpleDateFormat，并初始化格式yyyy-MM-dd HH:mm:ss*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*创建时区对象utcZone，获取utc所在的时区*/
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = null;
        try {
            utcDate = sdf.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.setTimeZone(TimeZone.getDefault());
        Date locatlDate = null;
        String localTime = sdf.format(utcDate.getTime());
        try {
            locatlDate = sdf.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(locatlDate);
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date());
    }
}
