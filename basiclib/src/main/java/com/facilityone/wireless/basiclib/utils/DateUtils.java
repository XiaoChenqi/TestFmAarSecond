package com.facilityone.wireless.basiclib.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.luck.picture.lib.tools.DateUtils.timeParseMinute;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:时间格式化工具
 * Date: 2018/6/26 下午4:52
 */
public class DateUtils {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_ALL = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_SECOND = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_HMS = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_YMD = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_HM = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_MD = new SimpleDateFormat("MM-dd", Locale.getDefault());

    /**
     * "MM/dd"
     */
    public static final String FORMAT_MONTH_DAY_TIME_TYPE_2 = "MM/dd";

    public static final String FORMAT_MONTH_DAY_TIME_TYPE_3 = "MM";

    /**
     * MS turn every minute
     *
     * @param duration Millisecond
     * @return Every minute
     */
    public static String timeParse(long duration) {
        String time = "";
        if (duration > 1000) {
            time = timeParseMinute(duration);
        } else {
            long minute = duration / 60000;
            long seconds = duration % 60000;
            long second = Math.round((float) seconds / 1000);
            if (minute < 10) {
                time += "0";
            }
            time += minute + ":";
            if (second < 10) {
                time += "0";
            }
            time += second;
        }
        return time;
    }

    /**
     * 格式化时间展示为05’10”
     */
    public static String formatRecordTime(long recTime, long maxRecordTime) {
        int time = (int) ((maxRecordTime - recTime) / 1000);
        return formatRecordTime(time);
    }

    @SuppressLint("DefaultLocale")
    public static String formatRecordTime(int time) {
        time = time / 1000;
        int minute = time / 60;
        int second = time % 60;
//        return String.format("%02d’%02d”", minute, second);
        return String.format(" %2d:%02d ", minute, second);
    }

    /**
     * 把时间戳转换成format格式的字符串
     *
     * @param time   时间戳
     * @param format 要转换的格式
     * @return 转换结果，要判空
     */
    public static String getFormatString(long time, String format) {
        if (time == 0) {
            return "";
        }

        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static int AT_LEAST_YESTERDAY = -1;
    public static int TODAY = 0;
    public static int AT_LEAST_TOMORROW = 1;

    /**
     * 今天最晚时间23：59：59 和 time比较，
     * 如果 差值/86400000>=1  time至少昨天的
     * 如果差值小于0 time至少是明天的
     * 等于0 就是今天
     *
     * @param time
     * @return
     */
    public static int compareTime(Long time) {
        int i = TODAY;
        // 获取当天23点59分59秒Date
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        long timeInMillis = calendar1.getTimeInMillis();
        long l = timeInMillis - time;
        double day = l / 86400000;
        if (l < 0) {
            //time 至少是明天的时间
            i = AT_LEAST_TOMORROW;
        }
        if (day >= 1) {
            //time至少是一天前
            i = AT_LEAST_YESTERDAY;
        }
        return i;
    }


    public static String addZero(int dayOrMonth) {
        if (dayOrMonth < 10) {
            return "0" + dayOrMonth;
        }

        return String.valueOf(dayOrMonth);

    }
}
