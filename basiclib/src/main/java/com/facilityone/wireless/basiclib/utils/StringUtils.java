package com.facilityone.wireless.basiclib.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:字符串工具
 * Date: 2018/6/29 下午2:41
 */
public class StringUtils {

    /**
     * 输入的字符串保留两位小数 支持四舍五入
     */
    public static String formatStringCost(String cost) {
        if (TextUtils.isEmpty(cost)) {
            return "";
        }
        try {
            Double d = Double.parseDouble(cost);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//格式化设置
            String format = decimalFormat.format(d);
            return format;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    //把null转为""
    public static String formatString(String value) {
        return formatString(value, "");
    }

    public static Double string2Double(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        } else {
            try {
                return Double.parseDouble(value);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String formatString(String value, String defaultValue) {
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 格式化double类型保留两位小数
     *
     * @param d
     * @return
     */
    public static String double2String(Double d) {
        if (d == null) {
            return "0.00";
        }
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置
        return decimalFormat.format(d);
    }

    /**
     * 保留两位小数，四舍五入的一个老土的方法，如0.33568 转为33.57
     *
     * @param d
     * @return
     */
    public static Float formatFloat(Float d) {
        return (float) Math.round(d * 100 * 100) / 100;
    }

    public static String formatFloatCost(float cost) {
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置
        return decimalFormat.format(cost);
    }

    public static String formatSingleCost(float cost) {
        DecimalFormat decimalFormat = new DecimalFormat("##0.0");//格式化设置
        return decimalFormat.format(cost);
    }

    /**
     * 获取图表中y轴应该取等分的最大值
     *
     * @param maxValue 填充数据的最大值
     * @param count    显示个数
     * @return
     */
    public static int getYMaxByMaxValue(int maxValue, int count) {
        count -= 1;
        int yMax = maxValue;
        if (count > 0) {
            int yStep = maxValue / count;
            int temp = yStep;
            int k = 1;
            while (temp > 10) {
                k *= 10;
                temp /= 10;
            }

            if (maxValue > temp * k || temp == 0) {
                temp++;
            }
            yMax = temp * k * count;
        } else {
            yMax = 10;
        }

        return yMax;
    }

    public static String getValue(Integer number) {
        if (number == null) {
            return "0";
        } else {
            return String.valueOf(number);
        }
    }

    public static String getValue(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }


    public static boolean isCarNumber(String carNumber) {
        String carNumRegex = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})";
        if (TextUtils.isEmpty(carNumber)) {
            return false;
        } else {
            return carNumber.matches(carNumRegex);
        }
    }

    /**
     * 判断是不是电话号(固定电话)
     *
     * @param phone
     * @return
     */
    public static boolean isTelephone(String phone) {
        String regex = "^[0-9+\\-\\s]+$";
        if (!TextUtils.isEmpty(phone)) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }

        return false;
    }

    /**
     * 判断是不是手机号码
     *
     * @param phone
     * @return
     */
    public static boolean isMobilePhone(String phone) {
        String regex = "^1[0-9]{10}$";
        if (!TextUtils.isEmpty(phone)) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }

        return false;
    }


    /**
     * 清除科学计数法
     *
     * @return
     */
    public static String clearFE(Object object) {
        if (object == null) {
            return "";
        }

        BigDecimal b = new BigDecimal(object.toString());
        return b.toPlainString();
    }


    /**
     * 使用正则表达式去掉多余的“.”与“0”
     *
     * @param str
     * @return
     */
    public static String subZeroAndDot(String str) {
        if (!TextUtils.isEmpty(str) && str.indexOf('.') >= 0) {
            // 去掉多余的0
            String newStr = str.replaceAll("0+?$", "");
            // 如最后一位是.则去掉
            return newStr.replaceAll("[.]$", "");
        }
        return str;
    }

    /**
     * 判断字符串是否为数字
     * 包括负数
     *
     * @param str
     * @return
     */
    public static boolean isNumerEX(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[+-]?(([1-9][0-9]*(\\.[0-9]+)*)|0(\\.[0-9]+)?)$");
        if (pattern.matcher(str).matches()) {
            return true;
        } else {
            return false;
        }
    }
}
