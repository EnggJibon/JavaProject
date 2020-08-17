/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.util;

import com.kmcj.karte.conf.CnfSystem;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fumisato
 */
public final class DateFormat {

    public final static String DEFAULT_TIMEZONE = "Asia/Tokyo";
    public final static String DATE_FORMAT = "yyyy/MM/dd";
    public final static String DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public final static String DATETIME_NOSECONDS_FORMAT = "yyyy/MM/dd HH:mm";
    public final static String DATETIMEMILL_FORMAT = "yyyy/MM/dd HH:mm:ss.SSS";
    public final static String DATE_FORMAT_YEAR = "yyyy";
    public final static String DATE_FORMAT_MONTH = "yyyy/MM";
    public final static String DATE_FORMAT_MONTHDAY = "MM/dd";
    public final static String DATETIME_ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public final static String DATETIME_EXPANSION_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    
    public final static String TIME_SECONDS = ":00";
    public final static String HYPHEN_DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * dateDiff()のパラメータ,単位"年"
     */
    public final static byte DIFF_YEAR = 0;

    /**
     * dateDiff()のパラメータ,単位"月"
     */
    public final static byte DIFF_MONTH = 1;

    /**
     * dateDiff()のパラメータ,単位"日"
     */
    public final static byte DIFF_DAY = 2;

    /**
     * dateDiff()のパラメータ,単位"時"
     */
    public final static byte DIFF_HOUR = 3;

    /**
     * dateDiff()のパラメータ,単位"分"
     */
    public final static byte DIFF_MINUTE = 4;

    /**
     * dateDiff()のパラメータ,単位"秒"
     */
    public final static byte DIFF_SECONDE = 5;

    public static String dateFormat(Date date, String timeZone) {
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(tz);
        return sdf.format(date);
    }

    public static String dateTimeFormat(Date date, String timeZone) {
        TimeZone tz = TimeZone.getTimeZone(
                (timeZone == null || timeZone.equals("")) ? DEFAULT_TIMEZONE : timeZone);
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        sdf.setTimeZone(tz);
        return sdf.format(date);
    }

    public static java.util.Date strToDate(String strDate) {
        if (strDate == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdf.parse(strDate);
        } catch (ParseException p) {
            return null;
        }
    }
    
    public static java.util.Date hyphenStrToDate(String strDate) {
        if (strDate == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(HYPHEN_DATE_FORMAT);
        try {
            return sdf.parse(strDate);
        } catch (ParseException p) {
            return null;
        }
    }
    
    public static java.util.Date strToDatetimeISO(String strDate) {
        if (strDate == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_ISO_FORMAT);
        try {
            return sdf.parse(strDate);
        } catch (ParseException p) {
            return null;
        }
    }

    public static java.util.Date strToMonth(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_MONTH);
        try {
            return sdf.parse(strDate);
        } catch (ParseException p) {
            return null;
        }
    }
    
    public static String dateToStrMonth(Date strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_MONTH);
        return sdf.format(strDate);
    }

    public static java.util.Date strToDatetime(String strDatetime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        try {
            return sdf.parse(strDatetime);
        } catch (ParseException p) {
            return null;
        }
    }

    public static java.util.Date strToDateMill(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIMEMILL_FORMAT);
        try {
            return sdf.parse(strDate);
        } catch (ParseException p) {
            return null;
        }
    }

    public static String dateToStrMill(java.util.Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIMEMILL_FORMAT);
        return sdf.format(date);
    }

    /**
     * Dateのフォーマット
     *
     * @param dateYearStr
     * @param formatStr
     * @return
     */
    public static String formatDateYear(String dateYearStr, String formatStr) {

        StringBuilder yearStr = new StringBuilder();

        String[] dateStr = dateYearStr.split("/");

        if (dateStr.length == 3) {

            if (dateStr[0].length() == 2) {

                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YEAR);
                Date date = new Date();
                String formatYear = sdf.format(date).substring(0, 2);

                yearStr.append(formatYear).append(dateYearStr);

            } else {
                yearStr.append(dateYearStr);
            }
        }

        String newDateStr = "";

        try {

            SimpleDateFormat format = new SimpleDateFormat(formatStr);

            // 日付自動計算機能を禁止する
            format.setLenient(false);

            Date date = format.parse(yearStr.toString());

            newDateStr = format.format(date);

        } catch (ParseException ex) {
            return "-1";
        }

        return newDateStr;
    }

    /**
     * Dateから、文字列に変換する
     *
     * @param date
     * @param dateType
     * @return
     */
    public static String dateToStr(java.util.Date date, String dateType) {

        if (null == date) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateType);
        return sdf.format(date);
    }

    /**
     * 該当日付の前日を取得
     *
     * @param date
     * @return
     */
    public static Date getBeforeDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 該当日付の翌日を取得
     *
     * @param date
     * @return
     */
    public static Date getAfterDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 該当日付の前週を取得
     *
     * @param date
     * @return
     */
    public static Date getBeforeWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 該当日付の翌週を取得
     *
     * @param date
     * @return
     */
    public static Date getAfterWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_MONTH, +1);
        date = calendar.getTime();
        return date;
    }

    /**
     * daysBetween
     *
     * @param smdate
     * @param bdate
     * @return
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException ex) {
            Logger.getLogger(DateFormat.class.getName()).log(Level.SEVERE, null, ex);

            return 0;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 該当日付前数日を取得
     *
     * @param date
     * @param d
     * @return
     */
    public static Date getBeforeDays(Date date, int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -d);
        date = calendar.getTime();
        return date;
    }

    /**
     * 該当日付後数日を取得
     *
     * @param date
     * @param d
     * @return
     */
    public static Date getAfterDays(Date date, int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, d);
        date = calendar.getTime();
        return date;
    }

    /**
     * 該当日付前数月を取得
     *
     * @param date
     * @param m
     * @return
     */
    public static Date getBeforeMonths(Date date, int m) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -m);
        date = calendar.getTime();
        return date;
    }

    /**
     * 該当日付後数月を取得
     *
     * @param date
     * @param m
     * @return
     */
    public static Date getAfterMonths(Date date, int m) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, m);
        date = calendar.getTime();
        return date;
    }

    /**
     * 現時点の日付（yyyy/mm/dd）を取得
     *
     * @return
     */
    public static String getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return df.format(localDate);
    }

    /**
     * 現時点の時間（yyyy/mm/dd HH:mm:ss）を取得
     *
     * @return
     */
    public static String getCurrentDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        return df.format(localDateTime);
    }

    /**
     * 日別初期リストを作成
     *
     * @param date
     * @return
     */
    public static List<String> getDayList(Date date) {
        List<String> dayList = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            dayList.add(dateToStr(getBeforeDays(date, i), DATE_FORMAT));
        }
        return dayList;
    }

    /**
     * 週別初期リストを作成
     * @param dateStart
     * @param dateEnd
     * @return 
     */
    public static List<String> getWeekList(Date dateStart, Date dateEnd)
    {
        List<String> weekList = new ArrayList<>();
        for (int i = 11; i >= 0; i--)
        {
            String weekStart = dateToStr(getBeforeDays(dateStart, i * 7), DATE_FORMAT);
            String weekEnd = dateToStr(getBeforeDays(dateEnd, i * 7), DATE_FORMAT);
            weekList.add(weekStart.concat(" - ").concat(weekEnd));
        }
        return weekList;
    }

    /**
     * 月別初期リストを作成
     * @param date
     * @return 
     */
    public static List<String> getMonthList(Date date)
    {
        List<String> monthList = new ArrayList<>();
        for (int i = 11; i >= 0; i--)
        {
            monthList.add(dateToStr(getBeforeMonths(date, i), DATE_FORMAT).substring(0, 7));
        }
        
        return monthList;
    }

    public static int getMonths(String dateStart, String dateEnd) {
        int months = 0;
        SimpleDateFormat sdf  = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        Calendar cs = Calendar.getInstance();
        Calendar ce = Calendar.getInstance();

        try {
            cs.setTime(sdf.parse(dateStart));
            ce.setTime(sdf.parse(dateEnd));
        } catch (ParseException ex) {
            return months;
        }
        int yearStart = cs.get(Calendar.YEAR);
        int yearEnd = ce.get(Calendar.YEAR);
        int monthStart = cs.get(Calendar.MONTH);
        int monthEnd = ce.get(Calendar.MONTH);

        if (yearStart == yearEnd) {
            months = monthEnd - monthStart;
        } else {
            months = 12 * (yearEnd - yearStart) + (monthEnd - monthStart);
        }
        return months;
    }
    /**
     * 指定日付の月曜日を取得する
     *
     * @param date
     *
     * @return
     */
    public static String getWeekMonday(Date date) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        //  指定の日付の順番を取得する
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);

        // 指定の日付は最終日の場合
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        // 最初日は月曜日に設定する
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        return sdf.format(cal.getTime());

    }

    /**
     * 指定日付の日曜日を取得する
     *
     * @param date
     *
     * @return
     */
    public static String getWeekSunday(Date date) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        //  指定の日付の順番を取得する
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);

        // 指定の日付は最終日の場合
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        // 最初日は月曜日に設定する
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);

        cal.add(Calendar.DATE, 6);

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        return sdf.format(cal.getTime());

    }

    /**
     * 指定日付の月に対して、FirstDayを取得する
     *
     * @param date
     *
     * @return
     */
    public static String getFirstDay(Date date) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return format.format(calendar.getTime());
    }

    /**
     * 指定日付の月に対して、LastDayを取得する
     *
     * @param date
     *
     * @return
     */
    public static String getLastDay(Date date) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // nextMonthを取得
        calendar.add(Calendar.MONTH, 1);
        // nextMonthのFirstDayを取得
        calendar.set(Calendar.DATE, 1);
        // 本月のLastDayを取得
        calendar.add(Calendar.DATE, -1);

        return format.format(calendar.getTime());
    }

    /**
     * @see #dateDiff(Calendar, Calendar, byte)
     *
     * @param dateFrom
     * @param dateTo
     * @param unit
     * @return
     */
    public static long dateDiff(Date dateFrom, Date dateTo, byte unit) {
        Calendar from = Calendar.getInstance();
        from.setTime(dateFrom);
        Calendar to = Calendar.getInstance();
        to.setTime(dateTo);
        return dateDiff(from, to, unit);
    }

    /**
     * 開始時間と終了時間の期間計算、 unit期間の単位
     *
     * @param dateFrom
     * @param dateTo
     * @param unit
     *
     * @return
     */
    @SuppressWarnings("static-access")
    public static long dateDiff(Calendar dateFrom, Calendar dateTo, byte unit) {
        long diff = dateTo.getTimeInMillis() - dateFrom.getTimeInMillis();
        long interval = 0;
        switch (unit) {
            case 0: {
                Calendar from = dateFrom;
                Calendar to = dateTo;
                interval = to.get(to.YEAR) - from.get(from.YEAR);
                from.set(from.YEAR, to.get(to.YEAR));
                if (from.after(to)) {
                    if (interval < 0) {
                        interval++;
                    } else {
                        interval--;
                    }
                }
                break;
            }
            case 1: {
                int year = dateTo.get(dateTo.YEAR) - dateFrom.get(dateFrom.YEAR);
                int month = dateTo.get(dateTo.MONTH) - dateFrom.get(dateFrom.MONTH);
                Calendar from = dateFrom;
                Calendar to = dateTo;
                from.set(from.YEAR, dateTo.get(dateTo.YEAR));
                from.set(from.MONTH, dateTo.get(dateTo.MONTH));
                interval = year * 12 + month;
                if (from.after(to)) {
                    if (interval < 0) {
                        interval++;
                    } else {
                        interval--;
                    }
                }
                break;
            }
            case 2:
                interval = (int) (diff / (1000 * 60 * 60 * 24));
                break;

            case 3:
                interval = (int) (diff / (1000 * 60 * 60));
                break;

            case 4:
                interval = (int) (diff / (1000 * 60));
                break;

            case 5:
                interval = (int) (diff / 1000);
                break;

            default:
                interval = diff;
        }
        return interval;
    }

    /**
     * 
     * daysCalculate(dateFrom, dateTo)
     * 
     * @param dateFrom
     * @param dateTo
     * @param businessTime
     *
     * @return 
     *
     */
    public static long daysCalculate(String dateFrom, String dateTo, String businessTime) {

        if (dateFrom == null || dateTo == null) {
            return -1;
        }

        // 取得开始日
        String dayFrom = dateFrom.substring(0, 10);

        // 取得结束日
        String dayTo = dateTo.substring(0, 10);

        StringBuilder tempStart = new StringBuilder();

        StringBuilder tempEnd = new StringBuilder();

        if (dayFrom.equals(dayTo)) {
            return 0;
        } else {
            
            String businessDateStr = tempStart.append(dayFrom).append(" ").append(businessTime).toString();
            String businessDateEnd = tempEnd.append(dayTo).append(" ").append(businessTime).toString();
            
            return dateDiff(strToDatetime(businessDateStr),strToDatetime(businessDateEnd), DIFF_DAY);
        }
    }
    
     /**
     * 
     * formatTime
     * 
     * @param businessTime
     *
     * @return 
     *
     */
    public static String formatTime(String businessTime) {

        StringBuilder businessTimeStr = new StringBuilder();

        String[] timeStr = businessTime.split(":");

        if (timeStr.length == 2) {

            if (timeStr[0].length() == 2) {

                businessTimeStr.append(businessTime).append(TIME_SECONDS);

            } else {

                businessTimeStr.append("0").append(businessTime).append(TIME_SECONDS);
            }
        }

        return businessTimeStr.toString();
    }
    
    /**
     * 
     * @param date
     * @param cnfSystem
     * @return 
     */
    public static String getBusinessDate(String date, CnfSystem cnfSystem) {
        if (strToDatetime(date) == null) {
            return "";
        }
        String businessTime = DateFormat.formatTime(cnfSystem.getConfigValue());
        if (date.substring(11, 16).compareTo(businessTime) > 0) {
            return date.substring(0, 10);
        } else {
            return DateFormat.dateToStr(DateFormat.getBeforeDay(DateFormat.strToDate(date)), DATE_FORMAT);
        }
    }

    /**
     * 
     * @param date
     * @param cnfSystem
     * @return 
     */
    public static String getBusinessDate2(String date, CnfSystem cnfSystem) {
        if (strToDatetime(date) == null) {
            return "";
        }
        String businessTime = DateFormat.formatTime(cnfSystem.getConfigValue());
        if (date.substring(11, 19).compareTo(businessTime) >= 0) {
            return date.substring(0, 10);
        } else {
            return DateFormat.dateToStr(DateFormat.getBeforeDay(DateFormat.strToDate(date)), DATE_FORMAT);
        }
    }
    
    /**
     * 週の初めの日を取得する。週の初めの日はシステム設定の指定(business_start_day_of_week)を渡す
     * @param firstDaySetting
     * @param date
     * @return 
     */
    public static java.util.Date getFirstDayOfWeek(int firstDaySetting, java.util.Date date) {
        int firstDayOfWeek = Calendar.MONDAY;
        java.util.Date answer = null;
        switch (firstDaySetting) {
            case 1: //Monday
                firstDayOfWeek = Calendar.MONDAY;
                break;
            case 2: //Tuesday
                firstDayOfWeek = Calendar.TUESDAY;
                break;
            case 3: //Wednesday
                firstDayOfWeek = Calendar.WEDNESDAY;
                break;
            case 4: //Thursday
                firstDayOfWeek = Calendar.THURSDAY;
                break;
            case 5: //Friday
                firstDayOfWeek = Calendar.FRIDAY;
                break;
            case 6: //Saturday
                firstDayOfWeek = Calendar.SATURDAY;
                break;
            case 7: //Sunday
                firstDayOfWeek = Calendar.SUNDAY;
                break;
        }
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            cal.setTime(date);
            cal.add(Calendar.DATE, i * (-1));
            if (cal.get(Calendar.DAY_OF_WEEK) == firstDayOfWeek) {
                answer = cal.getTime();
                break;
            }
        }
        return answer;
    }
}
