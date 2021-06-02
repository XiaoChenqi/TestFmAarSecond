package com.facilityone.wireless.basiclib.utils;

import java.util.Calendar;

/**
 * Created by: owen.
 * Date: on 2018/6/27 下午5:27.
 * Description:
 * email:
 */

public class SystemDateUtils {

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static void showMonth(Boolean isPre, Calendar calendarBeg, Calendar calendarEnd) {
        if (isPre != null) {
            if (isPre) {
                calendarBeg.add(Calendar.MONTH,-1);
                calendarEnd.add(Calendar.MONTH,-1);
            } else {
                calendarBeg.add(Calendar.MONTH,1);
                calendarEnd.add(Calendar.MONTH,1);
            }
        }else{
            calendarEnd.setTime(calendarBeg.getTime());
        }
        calendarBeg.set(Calendar.DAY_OF_MONTH, 1);
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        setCalendarPreciseValue(calendarBeg, calendarEnd);
    }

    public static void setCalendarPreciseValue(Calendar calendarStart, Calendar calendarEnd, int year, int month,int day) {
        calendarStart.set(Calendar.YEAR, year);
        calendarStart.set(Calendar.MONTH, month);
        calendarStart.set(Calendar.DAY_OF_MONTH, day);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);//精确到毫秒

        calendarEnd.set(Calendar.YEAR, year);
        calendarEnd.set(Calendar.MONTH, month);
        calendarEnd.set(Calendar.DAY_OF_MONTH, day);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);//精确到毫秒
    }

    public static void setCalendarPreciseValue(Calendar calendarStart, Calendar calendarEnd, int year, int month) {
        calendarStart.set(Calendar.YEAR, year);
        calendarStart.set(Calendar.MONTH, month);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);//精确到毫秒

        calendarEnd.set(Calendar.YEAR, year);
        calendarEnd.set(Calendar.MONTH, month);
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);//精确到毫秒
    }

    public static void setCalendarPreciseValue(Calendar calendarStart, Calendar calendarEnd) {
        calendarStart.set(Calendar.YEAR, calendarStart.get(Calendar.YEAR));
        calendarStart.set(Calendar.MONTH, calendarStart.get(Calendar.MONTH));
        calendarStart.set(Calendar.DAY_OF_MONTH, calendarStart.get(Calendar.DATE));
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);//精确到毫秒

        calendarEnd.set(Calendar.YEAR, calendarEnd.get(Calendar.YEAR));
        calendarEnd.set(Calendar.MONTH, calendarEnd.get(Calendar.MONTH));
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.get(Calendar.DATE));
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);//精确到毫秒
    }

    public static void setCalendarValue(Calendar calendar, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }
}
