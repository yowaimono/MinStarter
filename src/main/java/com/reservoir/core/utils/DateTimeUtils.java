package com.reservoir.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    // 日期格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // 获取当前日期
    public static String getCurrentDate() {
        return formatDate(new Date(), DATE_FORMAT);
    }

    // 获取当前日期或时间
    public static String getCurrentDateOrTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    // 获取当前日期时间
    public static String getCurrentDateTime() {
        return formatDate(new Date(), DATETIME_FORMAT);
    }

    // 格式化日期
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    // 解析日期字符串为 Date 对象
    public static Date parseDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateStr);
    }

    // 获取指定日期的年份
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    // 获取指定日期的月份
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1; // 月份从 0 开始，所以需要加 1
    }

    // 获取指定日期的日
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    // 获取指定日期是星期几
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    // 增加天数
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    // 增加月份
    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    // 增加年份
    public static Date addYears(Date date, int years) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }

    // 计算两个日期之间的天数差
    public static int getDaysBetween(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diffTime = endTime - startTime;
        return (int) (diffTime / (1000 * 60 * 60 * 24));
    }

    // 比较两个日期或时间
    public static int compareDates(Date date1, Date date2) {
        return date1.compareTo(date2);
    }

    // 获取当前 LocalDateTime
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    // 格式化 LocalDateTime
    public static String formatLocalDateTime(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    // 解析 LocalDateTime 字符串
    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    // 将 Date 转换为 LocalDateTime
    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // 将 LocalDateTime 转换为 Date
    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    // 增加 LocalDateTime 的天数
    public static LocalDateTime addDays(LocalDateTime localDateTime, int days) {
        return localDateTime.plusDays(days);
    }

    // 增加 LocalDateTime 的月份
    public static LocalDateTime addMonths(LocalDateTime localDateTime, int months) {
        return localDateTime.plusMonths(months);
    }

    // 增加 LocalDateTime 的年份
    public static LocalDateTime addYears(LocalDateTime localDateTime, int years) {
        return localDateTime.plusYears(years);
    }

    // 比较两个 LocalDateTime
    public static int compareLocalDateTimes(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        return localDateTime1.compareTo(localDateTime2);
    }

    // 获取当前 ZonedDateTime
    public static ZonedDateTime getCurrentZonedDateTime(ZoneId zoneId) {
        return ZonedDateTime.now(zoneId);
    }

    // 获取当前 ZonedDateTime
    public static ZonedDateTime getCurrentZonedDateTime(String zone) {
        ZoneId zoneId = ZoneId.of(zone);
        return ZonedDateTime.now(zoneId);
    }

    // 格式化 ZonedDateTime
    public static String formatZonedDateTime(ZonedDateTime zonedDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return zonedDateTime.format(formatter);
    }


    // 解析 ZonedDateTime 字符串
    public static ZonedDateTime parseZonedDateTime(String dateTimeStr, String format,String zone) {
        ZoneId zoneId = ZoneId.of(zone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return ZonedDateTime.parse(dateTimeStr, formatter).withZoneSameInstant(zoneId);
    }

    // 将 Date 转换为 ZonedDateTime
    public static ZonedDateTime convertDateToZonedDateTime(Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId);
    }

    // 将 Date 转换为 ZonedDateTime
    public static ZonedDateTime convertDateToZonedDateTime(Date date, String zone) {
        ZoneId zoneId = ZoneId.of(zone);
        return date.toInstant().atZone(zoneId);
    }

    // 将 ZonedDateTime 转换为 Date
    public static Date convertZonedDateTimeToDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    // 增加 ZonedDateTime 的天数
    public static ZonedDateTime addDays(ZonedDateTime zonedDateTime, int days) {
        return zonedDateTime.plusDays(days);
    }

    // 增加 ZonedDateTime 的月份
    public static ZonedDateTime addMonths(ZonedDateTime zonedDateTime, int months) {
        return zonedDateTime.plusMonths(months);
    }

    // 增加 ZonedDateTime 的年份
    public static ZonedDateTime addYears(ZonedDateTime zonedDateTime, int years) {
        return zonedDateTime.plusYears(years);
    }

    // 比较两个 ZonedDateTime
    public static int compareZonedDateTimes(ZonedDateTime zonedDateTime1, ZonedDateTime zonedDateTime2) {
        return zonedDateTime1.compareTo(zonedDateTime2);
    }

    // 获取当前 OffsetDateTime
    public static OffsetDateTime getCurrentOffsetDateTime(ZoneOffset zoneOffset) {
        return OffsetDateTime.now(zoneOffset);
    }

    // 格式化 OffsetDateTime
    public static String formatOffsetDateTime(OffsetDateTime offsetDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return offsetDateTime.format(formatter);
    }

    // 解析 OffsetDateTime 字符串
    public static OffsetDateTime parseOffsetDateTime(String dateTimeStr, String format, ZoneOffset zoneOffset) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return OffsetDateTime.parse(dateTimeStr, formatter).withOffsetSameInstant(zoneOffset);
    }

    // 将 Date 转换为 OffsetDateTime
    public static OffsetDateTime convertDateToOffsetDateTime(Date date, ZoneOffset zoneOffset) {
        return date.toInstant().atOffset(zoneOffset);
    }

    // 将 OffsetDateTime 转换为 Date
    public static Date convertOffsetDateTimeToDate(OffsetDateTime offsetDateTime) {
        return Date.from(offsetDateTime.toInstant());
    }

    // 增加 OffsetDateTime 的天数
    public static OffsetDateTime addDays(OffsetDateTime offsetDateTime, int days) {
        return offsetDateTime.plusDays(days);
    }

    // 增加 OffsetDateTime 的月份
    public static OffsetDateTime addMonths(OffsetDateTime offsetDateTime, int months) {
        return offsetDateTime.plusMonths(months);
    }

    // 增加 OffsetDateTime 的年份
    public static OffsetDateTime addYears(OffsetDateTime offsetDateTime, int years) {
        return offsetDateTime.plusYears(years);
    }

    // 比较两个 OffsetDateTime
    public static int compareOffsetDateTimes(OffsetDateTime offsetDateTime1, OffsetDateTime offsetDateTime2) {
        return offsetDateTime1.compareTo(offsetDateTime2);
    }


}