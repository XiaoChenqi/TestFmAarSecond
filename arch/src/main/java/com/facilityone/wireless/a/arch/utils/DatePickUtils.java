package com.facilityone.wireless.a.arch.utils;

import android.app.Activity;
import android.view.ViewGroup;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.basiclib.utils.SystemDateUtils;

import java.util.Calendar;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:时间选择
 * Date: 2018/7/10 下午3:06
 */
public class DatePickUtils {

    public static final boolean[] YEAR_MONTH = { true, true, false, false, false, false };
    public static final boolean[] YEAR_MONTH_DAY = { true, true, true, false, false, false };
    public static final boolean[] YEAR_MONTH_DAY_H_M = { true, true, true, true, true, false };

    private static final int DIVIDER_COLOR = 0XFFC3C3C3;
    private static final int TITLE_BG_COLOR = 0XFFECECEC;
    private static final int SUBMIT_COLOR = 0XFF3E8DC4;
    private static final int CANCEL_COLOR = 0XFF3E8DC4;
    private static final int TEXT_X_OFFSET = 15;
    private static final float LINE_SPACING_MULTIPLIER = 2F;


    /**
     * 前后三十年的 年月选择
     *
     * @param context              上下文
     * @param selectCalendar       弹窗显示的月份
     * @param onTimeSelectListener 选中后的监听
     */
    public static void pickDateDefault(Activity context, Calendar selectCalendar, OnTimeSelectListener onTimeSelectListener) {
        pickDateDefault(context, selectCalendar, onTimeSelectListener, YEAR_MONTH);
    }

    public static void pickDateDefaultYMD(Activity context, Calendar selectCalendar, OnTimeSelectListener onTimeSelectListener) {
        pickDateDefault(context, selectCalendar, onTimeSelectListener, YEAR_MONTH_DAY);
    }

    public static void pickDateDefaultYMDHM(Activity context, Calendar selectCalendar, OnTimeSelectListener onTimeSelectListener) {
        pickDateDefault(context, selectCalendar, onTimeSelectListener, YEAR_MONTH_DAY_H_M);
    }

    public static void pickDateAfterToday(Activity context, Calendar selectCalendar, OnTimeSelectListener onTimeSelectListener) {
        pickDateAfterToday(context, selectCalendar, onTimeSelectListener, YEAR_MONTH_DAY_H_M);
    }


    public static void pickDateDefaultYMDToday(Activity context, Calendar selectCalendar, OnTimeSelectListener onTimeSelectListener) {
        pickDateRangeToday(context, selectCalendar, onTimeSelectListener, YEAR_MONTH_DAY);
    }

    public static void pickDateDefault(Activity context,
                                       Calendar selectCalendar,
                                       OnTimeSelectListener onTimeSelectListener,
                                       boolean[] showType) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - 30, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedDate.get(Calendar.YEAR) + 30, 11, 31);
        new TimePickerBuilder(context, onTimeSelectListener)
                .setType(showType)
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(DIVIDER_COLOR)
                .setTitleBgColor(TITLE_BG_COLOR)
                .setSubmitColor(SUBMIT_COLOR)//确定按钮文字颜色
                .setCancelColor(CANCEL_COLOR)//取消按钮文字颜色
                .setSubCalSize(15)//确定和取消文字大小
                .setContentTextSize(15)
                .setTextColorCenter(0XFF666666)
                .setLineSpacingMultiplier(LINE_SPACING_MULTIPLIER)
                .setTextXOffset(TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET)
                .setDate(selectCalendar)
                .setRangDate(startDate, endDate)
                .setDecorView((ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content))
                .build()
                .show();
    }

    /**
     * 现在时间之后
     *
     * @param context
     * @param selectCalendar
     * @param onTimeSelectListener
     * @param showType
     */
    public static void pickDateAfterToday(Activity context,
                                          Calendar selectCalendar,
                                          OnTimeSelectListener onTimeSelectListener,
                                          boolean[] showType) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedDate.get(Calendar.YEAR) + 30, 11, 31);
        new TimePickerBuilder(context, onTimeSelectListener)
                .setType(showType)
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(DIVIDER_COLOR)
                .setTitleBgColor(TITLE_BG_COLOR)
                .setSubmitColor(SUBMIT_COLOR)//确定按钮文字颜色
                .setCancelColor(CANCEL_COLOR)//取消按钮文字颜色
                .setSubCalSize(15)//确定和取消文字大小
                .setContentTextSize(15)
                .setTitleSize(15)
                .setTextColorCenter(0XFF666666)
                .setLineSpacingMultiplier(LINE_SPACING_MULTIPLIER)
                .setTextXOffset(TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET)
                .setTitleText(context.getString(R.string.arch_select_date))
                .setDate(selectCalendar)
                .setRangDate(startDate, endDate)
                .setDecorView((ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content))
                .build()
                .show();
    }

    /**
     * 范围到今天
     *
     * @param context
     * @param selectCalendar
     * @param onTimeSelectListener
     * @param showType
     */
    public static void pickDateRangeToday(Activity context,
                                          Calendar selectCalendar,
                                          OnTimeSelectListener onTimeSelectListener,
                                          boolean[] showType) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - 30, 0, 1);
        Calendar endDate = Calendar.getInstance();
        new TimePickerBuilder(context, onTimeSelectListener)
                .setType(showType)
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(DIVIDER_COLOR)
                .setTitleBgColor(TITLE_BG_COLOR)
                .setSubmitColor(SUBMIT_COLOR)//确定按钮文字颜色
                .setCancelColor(CANCEL_COLOR)//取消按钮文字颜色
                .setSubCalSize(15)//确定和取消文字大小
                .setContentTextSize(15)
                .setTextColorCenter(0XFF666666)
                .setLineSpacingMultiplier(LINE_SPACING_MULTIPLIER)
                .setTextXOffset(TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET)
                .setDate(selectCalendar)
                .setRangDate(startDate, endDate)
                .setDecorView((ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content))
                .build()
                .show();
    }

    public static void pickDateOnTime(Activity context,
                                      Calendar selectCalendar,
                                      Calendar show,
                                      OnTimeSelectListener onTimeSelectListener,
                                      boolean[] showType) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        SystemDateUtils.setCalendarPreciseValue(startDate, endDate, selectCalendar.get(Calendar.YEAR), selectCalendar.get(Calendar.MONTH));
        new TimePickerBuilder(context, onTimeSelectListener)
                .setType(showType)
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(DIVIDER_COLOR)
                .setTitleBgColor(TITLE_BG_COLOR)
                .setSubmitColor(SUBMIT_COLOR)//确定按钮文字颜色
                .setCancelColor(CANCEL_COLOR)//取消按钮文字颜色
                .setSubCalSize(15)//确定和取消文字大小
                .setContentTextSize(15)
                .setTextColorCenter(0XFF666666)
                .setLineSpacingMultiplier(LINE_SPACING_MULTIPLIER)
                .setTextXOffset(TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET, TEXT_X_OFFSET)
                .setDate(show)
                .setRangDate(startDate, endDate)
                .setDecorView((ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content))
                .build()
                .show();
    }
}
