package com.example.testfmaarsecond;

import android.app.Application;

import com.facilityone.wireless.InitApplication;
import com.facilityone.wireless.a.arch.Facility;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.app.FMChannel;

import java.util.HashMap;


public class MainApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //saveChannelParam();
//        Facility.init(this, "http://192.168.1.66:8080", BuildConfig.DEBUG);
//        DBManager.getInstance();

        saveChannelParam();
        Facility.init(this, "http://192.168.1.66:8080", BuildConfig.DEBUG);
        DBManager.getInstance();
    }


    /**
     * 不同渠道参数不同（向下传递）这里如果提供给别人在封装一下 不对外 直接写死 内部调用一下即可
     */
    private void saveChannelParam() {
        HashMap<Object, Object> fmConfigs = FM.getFMConfigs();
        fmConfigs.put(FMChannel.CHANNEL_APP_SERVER, BuildConfig.SERVER_URL);
        fmConfigs.put(FMChannel.CHANNEL_APP_KEY, BuildConfig.APP_KEY);
        fmConfigs.put(FMChannel.CHANNEL_APP_SECRET, BuildConfig.APP_SECRET);
        fmConfigs.put(FMChannel.CHANNEL_UMENG_KEY, BuildConfig.UMENG_CHANNEL_KEY);
        fmConfigs.put(FMChannel.CHANNEL_UMENG_VALUE, BuildConfig.UMENG_CHANNEL_VALUE);
        fmConfigs.put(FMChannel.CHANNEL_UPDATE_APP_KEY, BuildConfig.UPDATE_APP_KEY);
        fmConfigs.put(FMChannel.CHANNEL_UPDATE_CHANNEL, BuildConfig.UPDATE_CHANNEL);
        fmConfigs.put(FMChannel.CHANNEL_CUSTOMER_CODE, BuildConfig.CUSTOMER_CODE);
        fmConfigs.put(FMChannel.CHANNEL_QQZONE_KEY, BuildConfig.QQZONE_KEY);
        fmConfigs.put(FMChannel.CHANNEL_QQZONE_SECRET, BuildConfig.QQZONE_SECRET);
        fmConfigs.put(FMChannel.CHANNEL_DING_DING_SECRET, BuildConfig.DING_DING_SECRET);
        fmConfigs.put(FMChannel.CHANNEL_WEI_XIN_KEY, BuildConfig.WEI_XIN_KEY);
        fmConfigs.put(FMChannel.CHANNEL_WEI_XIN_SECRET, BuildConfig.WEI_XIN_SECRET);
    }
}



