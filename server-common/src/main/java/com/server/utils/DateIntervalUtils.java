package com.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 近一周、近一个月、近三个月、近半年、近一年时间段内日期显示
 *
 * @author 851543
 * @date 2025年7月16日
 **/
public class DateIntervalUtils {

    /**
     * 最近一周的所有日期
     *
     * @return
     */
    public static List<String> getNearlyWeekDates() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        // 过去七天
        c.setTime(new Date());
        String today = format.format(new Date());
        c.add(Calendar.DATE, -7);
        Date d = c.getTime();
        String day = format.format(d);
        List<String> result = getBetweenDates(day, today, false);
        return result;
    }

    /**
     * 最近一个月的所有日期
     *
     * @return
     */
    public static List<String> getNearlyMonthDates() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        // 过去一月
        c.setTime(new Date());
        String today = format.format(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format.format(m);
        List<String> result = getBetweenDates(mon, today, false);

        return result;
    }

    /**
     * 最近三个月的所有日期
     *
     * @return
     */
    public static List<String> getNearlyThMonthDates() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        // 过去一月
        c.setTime(new Date());
        String today = format.format(new Date());
        c.add(Calendar.MONTH, -3);
        Date m = c.getTime();
        String mon = format.format(m);
        List<String> result = getBetweenDates(mon, today, false);

        return result;
    }

    /**
     * 最近半年的所有日期
     *
     * @return
     */
    public static List<String> getNearlySixMonthDates() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        // 过去一月
        c.setTime(new Date());
        String today = format.format(new Date());
        c.add(Calendar.MONTH, -6);
        Date m = c.getTime();
        String mon = format.format(m);
        List<String> result = getBetweenDates(mon, today, false);

        return result;
    }

    /**
     * 最近一年的所有月份
     *
     * @return
     */
    public static List<String> getNearlyYearDates() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        // 过去一年
        c.setTime(new Date());
        String today = format.format(new Date());
        c.add(Calendar.MONTH, -12);
        Date y = c.getTime();
        String year = format.format(y);
        // 如果要获取近一年内的所有月份
        // List<String> result = getMonthsBetweenDates(year, today);
        // 如果要获取近一年内的所有日期
        List<String> result = getBetweenDates(year, today, false);
        return result;
    }

    /**
     * 补全给定时间内的所有周，包含最开始的
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getWeeksBetweenDates(String startTime, String endTime) {
        List<String> result = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        try {
            start = format.parse(startTime);
            end = format.parse(endTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(start);
            result.add(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.WEEK_OF_YEAR));
            while (calendar.getTime().before(end)) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                String weekStr = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.WEEK_OF_YEAR);
                result.add(weekStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 补全给定起止时间区间内的所有日期
     *
     * @param startTime
     * @param endTime
     * @param isIncludeStartTime
     * @return
     */
    public static List<String> getBetweenDates(String startTime, String endTime, boolean isIncludeStartTime) {
        List<String> result = new ArrayList<>();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // 定义起始日期
            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
            // 定义结束日期 可以去当前月也可以手动写日期。
            Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
            // 定义日期实例
            Calendar dd = Calendar.getInstance();
            // 设置日期起始时间
            dd.setTime(d1);
            if (isIncludeStartTime) {
                result.add(format.format(d1));
            }
            // 判断是否到结束日期
            while (dd.getTime().before(d2)) {
                // 进行当前日期加1
                dd.add(Calendar.DATE, 1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String str = sdf.format(dd.getTime());
                result.add(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 补全给定时间区内的所有月份
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getMonthsBetweenDates(String startTime, String endTime) {
        List<String> result = new ArrayList<>();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            // 定义起始日期
            Date d1 = new SimpleDateFormat("yyyy-MM").parse(startTime);
            // 定义结束日期 可以去当前月也可以手动写日期。
            Date d2 = new SimpleDateFormat("yyyy-MM").parse(endTime);
            // 定义日期实例
            Calendar dd = Calendar.getInstance();
            // 设置日期起始时间
            dd.setTime(d1);
            result.add(format.format(d1));
            // 判断是否到结束日期
            while (dd.getTime().before(d2)) {
                // 进行当前日期月份加1
                dd.add(Calendar.MONTH, 1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                String str = sdf.format(dd.getTime());
                result.add(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

