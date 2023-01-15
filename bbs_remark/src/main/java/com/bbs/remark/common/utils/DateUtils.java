package com.bbs.remark.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Title: DateUtils
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/5/26 9:58
 */
public class DateUtils {

    /**
     *
     * 功能描述:
     *
     * @param: 获取当前系统时间 yyyy-MM-dd HH:mm:ss
     * @return:
     * @auther: youqing
     * @date: 2018/5/26 9:59
     */
    public static String getCurrentDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(System.currentTimeMillis());
        return date;
    }


    /**
     *
     * 功能描述: 
     *
     * @param: date类 获取当前系统时间 yyyy-MM-dd HH:mm:ss
     * @return: 
     * @auther: youqing
     * @date: 2018/5/26 10:39
     */
    public static Date getCurrentDateToDate () {
        DateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String date = df.format(System.currentTimeMillis());
        Date d = null;
        try {
            d = df.parse( date.toString( ) );
        } catch ( ParseException e ) {
            e.printStackTrace( );
        }
        return d;
    }

    /**
     * 增加时间单位：天
     * @param day
     * @return
     */
    public static String getCurrentAddDay(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, day);
        return sdf.format(cal.getTime());
    }

    /**
     * 增加时间单位：分钟
     * @param minute
     * @return
     */
    public static String getCurrentAddMin(int minute) {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, minute);
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getNowDateString (  ) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd");
        return sdf.format( d );
    }

    /**
     * 把Date转为String
     * @param date
     * @param format
     * @return
     */
    public static String getFormatTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 增加时间单位：天
     * @param day
     * @return
     */
    public static Date addDay(int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    /**
     * 增加时间单位：天
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    /**
     * 减去多少天
     * @param date
     * @param day
     * @return
     */
    public static Date minusDay(Date date, int day) {
        return addDay(date, -day);
    }

    /**
     * 两个时间相减得到的差（秒数）
     *
     * @param big   大的时间
     * @param small 小的时间
     * @return
     */
    public static Long DifferTime(Date big, Date small) {
        if (null == big || null == small) return 0L;
        Long diff = big.getTime() - small.getTime();
        return diff / 1000;
    }

    /**
     * 两个时间相减得到的差，相差几天几小时几分几秒
     *
     * @param big   大的时间
     * @param small 小的时间
     * @return 返回空表示参数异常，或者返回对应的时间差
     */
    public static String DifferTimeStr(Date big, Date small) {
        if (null == big || null == small) return null;
        long diff = big.getTime() - small.getTime();
        long diffSeconds = diff / 1000;
        if (diffSeconds >= 60) {
            long diffMins = diffSeconds / 60;
            if (diffMins >= 60) {
                long diffHours = diffMins / 60;
                if (diffHours >= 24) {
                    long diffDays = diffHours / 24;
                    long hours = diffHours % 24;
                    long mins = diffMins % 60;
                    long seconds = diffSeconds % 60;

                    return String.format("%s天%s小时%s分%s秒", diffDays, hours, mins, seconds);
                } else {
                    long mins = diffMins % 60;
                    long seconds = diffSeconds % 60;
                    return String.format("%s小时%s分%s秒", diffHours, mins, seconds);
                }
            } else {
                long seconds = diffSeconds % 60;
                return String.format("%s分%s秒", diffMins, seconds);
            }
        } else {
            return diffSeconds + "秒";
        }
    }

    /**
     * 格式化时间
     *
     * @param timestamp
     * @return
     */
    public static String dateFormat(Date timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timestamp);
    }

    /**
     * 获得当前日期的数字：yyyyMMdd  格式
     *
     * @return 例如: 20180201
     */
    public static int getCurrentDayNumber() {
        Integer day = Integer.parseInt(DateUtils.getDate("yyyyMMdd"));
        return day;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        if (null == date) return "";
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 该方法在linux环境下可能会得不到中文的 星期一、星期二。。。等，而是得到英文版的,Mon、Fri。。。之类的
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    @Deprecated
    public static String formatDate(Date date, Object... pattern) {
        if (null == date) return "";
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 两个时间相减得到的差，相差几天几小时几分几秒
     *
     * @param diffSeconds 小的时间
     * @return 返回对应的时间差
     */
    public static String DifferTimeStr(long diffSeconds) {
        if (diffSeconds >= 60) {
            long diffMins = diffSeconds / 60;
            if (diffMins >= 60) {
                long diffHours = diffMins / 60;
                if (diffHours >= 24) {
                    long diffDays = diffHours / 24;
                    long hours = diffHours % 24;
                    long mins = diffMins % 60;
                    long seconds = diffSeconds % 60;

                    return String.format("%s天%s小时%s分%s秒", diffDays, hours, mins, seconds);
                } else {
                    long mins = diffMins % 60;
                    long seconds = diffSeconds % 60;
                    return String.format("%s小时%s分%s秒", diffHours, mins, seconds);
                }
            } else {
                long seconds = diffSeconds % 60;
                return String.format("%s分%s秒", diffMins, seconds);
            }
        } else {
            return diffSeconds + "秒";
        }
    }

    public static void main(String[] args) {
        System.out.println(getCurrentAddDay(2));
    }
}
