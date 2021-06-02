package com.facilityone.wireless.basiclib.utils;

import com.blankj.utilcode.util.SizeUtils;

import java.text.DecimalFormat;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:处理数据
 * Date: 2018/6/14 下午5:54
 */
public class DataUtils {

    /**
     * 金额 格式化
     */
    private static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("###,###,###,##0.00");
    /**
     * 每三位以逗号进行分隔 格式化
     */
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat(",###");

    /**
     * 隐藏手机中间4位号码
     * 130****0000
     *
     * @param mobile_phone 手机号码
     * @return 130****0000
     */
    public static String hideMobilePhone4(String mobile_phone) {
        if (mobile_phone.length() != 11) {
            return "手机号码不正确";
        }
        return mobile_phone.substring(0, 3) + "****" + mobile_phone.substring(7, 11);
    }

    /**
     * 金额格式化
     *
     * @param value 数值
     * @return
     */
    public static String getAmountValue(double value) {
        return AMOUNT_FORMAT.format(value);
    }

    /**
     * 数字格式化
     *
     * @param value 数值
     * @return
     */
    public static String getNumberValue(int value) {
        return NUMBER_FORMAT.format(value);
    }

    //根据时间长短计算语音条宽度:220dp
    public synchronized static int getVoiceLineWight(int seconds) {
        //1-2s是最短的。2-10s每秒增加一个单位。10-60s每10s增加一个单位。
        if (seconds <= 2) {
            return SizeUtils.dp2px(60);
        } else if (seconds > 2 && seconds <= 10) {
            //90~170
            return SizeUtils.dp2px(60 + 7 * seconds);
        } else {
            //170~220
            return SizeUtils.dp2px(130 + 8 * (seconds / 10));
        }
    }
}
