package com.facilityone.wireless.basiclib.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:网络工具
 * Date: 2018/11/29 12:20 PM
 */
public class FMNetWorkUtils {

    /**
     * 获取wifi mac
     *
     * @param context
     * @return
     */
    public static String getWifiMac(Context context) {
        Context applicationContext = context.getApplicationContext();
        WifiManager wifi = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled()) {
            wifi.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        return wifiInfo.getBSSID();
    }

}
