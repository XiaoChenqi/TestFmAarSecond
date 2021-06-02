package com.facilityone.wireless.basiclib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:获取app当前的渠道号或application中指定的meta-data
 * Date: 2019/1/11 6:03 PM
 */
public class AppChannelUtils {

//    注：上面所说的key指的是清单文件中你在注册友盟统计时的"UMENG_CHANNEL"（即getChannelNumber(Context context, String key)方法的第二个参数key传入"UMENG_CHANNEL"），而不是"UMENG_APPKEY"，也不是任何一个value值
//    <!-- 友盟统计 -->
//    <meta-data
//    android:name="UMENG_APPKEY"
//    android:value="*****************" />
//    <meta-data
//    android:name="UMENG_CHANNEL"
//    android:value="${UMENG_CHANNEL_VALUE}" />
//    /**
//     * 在需要的地方调用上述方法
//     */
//    String channelNumber = getAppMetaData(getBaseContext(), "UMENG_CHANNEL");//获取app当前的渠道号
    /**
     * 获取app当前的渠道号或application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值,或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String channelNumber = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelNumber = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelNumber;
    }
}
