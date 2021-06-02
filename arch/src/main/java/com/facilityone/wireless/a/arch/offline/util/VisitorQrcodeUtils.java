package com.facilityone.wireless.a.arch.offline.util;

import android.text.TextUtils;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:访客二维码工具
 * Date: 2018/10/31 5:28 PM
 */
public class VisitorQrcodeUtils {
    /**
     * 从二维码中解析出点位的code
     * 访客|访客单|访客单id|访客单编号|公司或产品标识
     * VISITOR|ORDER|001|code|F-ONE
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
        if (data.length > 4) {
            result = data[2];
        }
        return result;
    }
}
