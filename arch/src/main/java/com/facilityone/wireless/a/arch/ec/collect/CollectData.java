package com.facilityone.wireless.a.arch.ec.collect;

import com.google.gson.JsonObject;

/**
 * Author：gary.xu
 * <p/>
 * Email: xuhaozv@163.com
 * <p/>
 * description:
 * <p/>
 * Date: 2017/9/7 16:24
 */
public class CollectData {

    public static class CollectSetting{
        public String eventCode;
        public Integer eventType;
        public String target;
        public String desc;
        public boolean isValid;
    }

    public static class CollectUpload{
        public String appName;
        public String appKey;
        public String channelCode;
        public String version;
        public Integer versionCode;
        public String userName;
        public String project;
        public Integer origin;//来源 0 android 1 ios
        public String eventCode;
        public Integer eventType;
        public String desc;
        public Long startTime;
        public Long endTime;
        public JsonObject extData;
    }
}
