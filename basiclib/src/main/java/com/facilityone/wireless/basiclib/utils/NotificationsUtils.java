package com.facilityone.wireless.basiclib.utils;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:通知是否打开
 * Date: 2018/6/15 下午5:50
 */
public class NotificationsUtils {

    public static boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        return !manager.areNotificationsEnabled();
    }
}