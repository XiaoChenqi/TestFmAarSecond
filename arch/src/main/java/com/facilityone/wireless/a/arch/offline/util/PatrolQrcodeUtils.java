package com.facilityone.wireless.a.arch.offline.util;

import android.text.TextUtils;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检二维码工具
 * Date: 2018/10/31 5:28 PM
 */
public class PatrolQrcodeUtils {
    /**
     * 从二维码中解析出点位的code
     *
     * @param qrcode
     * @return
     */
    public static String parseSpotCode(String qrcode) {
        String result = "";
        if (TextUtils.isEmpty(qrcode)) {
            return result;
        }
        String tmp = qrcode.replace(" ", "");
        String[] data = tmp.split("\\|");
        if (data.length > 1) {
            result = data[0];
        }
        return result;
    }
}
