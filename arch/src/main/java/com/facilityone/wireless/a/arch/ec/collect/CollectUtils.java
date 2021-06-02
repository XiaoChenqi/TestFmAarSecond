package com.facilityone.wireless.a.arch.ec.collect;

import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.app.FMChannel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：gary.xu
 * <p/>
 * Email: xuhaozv@163.com
 * <p/>
 * description:
 * <p/>
 * Date: 2017/9/7 16:58
 */
public class CollectUtils {
    public static final int LOGIN = 0;
    public static final int LOGOUT = 1;
    public static final int CHANGE = 2;
    public static final int NET_REQUEST = 3;
    public static final int CUSTOM = 4;
    public static final int OTHERS = 1000;

    public static final String SYSTEM_LOGIN = "SYSTEM_LOGIN";
    public static final String SYSTEM_LOGOUT = "SYSTEM_LOGOUT";
    public static final String SYSTEM_PAGE = "SYSTEM_PAGE";
    public static final String SYSTEM_NET_REQUEST = "SYSTEM_NET_REQUEST";
    public static final String SYSTEM_CUSTOM = "SYSTEM_CUSTOM";
    public static final String SYSTEM_OTHER = "SYSTEM_OTHER";

    public static List<CollectData.CollectSetting> collectData = new ArrayList<>();

    private static Map<String, CollectData.CollectUpload> collectUploadData = new HashMap<>();

    /**
     * 按照事件类型获取是否需要记录
     *
     * @param type
     * @return
     */
    public static boolean needCollect(int type) {
        for (CollectData.CollectSetting collectSetting : collectData) {
            if (collectSetting != null && collectSetting.eventType != null && collectSetting.eventType == type) {
                return collectSetting.isValid;
            }
        }
        return false;
    }

    /**
     * 按照事件编号获取是否需要记录
     *
     * @param code
     * @return
     */
    public static boolean needCollect(String code) {
        if (!TextUtils.isEmpty(code)) {
            for (CollectData.CollectSetting collectSetting : collectData) {
                if (code.equals(collectSetting.eventCode)) {
                    return collectSetting.isValid;
                }
            }
        }
        return false;
    }

    public static boolean needCollectByTarget(String target) {
        if (!TextUtils.isEmpty(target)) {
            for (CollectData.CollectSetting collectSetting : collectData) {
                if (target.equals(collectSetting.target)) {
                    return collectSetting.isValid;
                }
            }
        }
        return false;
    }

    private static CollectData.CollectUpload getCollectUploadBean() {
        CollectData.CollectUpload collectUpload = new CollectData.CollectUpload();
        collectUpload.appKey = FM.getConfiguration(FMChannel.CHANNEL_UPDATE_APP_KEY);
        collectUpload.appName = AppUtils.getAppName();
        collectUpload.channelCode = FM.getConfiguration(FMChannel.CHANNEL_UPDATE_CHANNEL);
        collectUpload.version = AppUtils.getAppVersionName();
        collectUpload.versionCode = AppUtils.getAppVersionCode();
        String username = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.USERNAME, "");
        collectUpload.userName = username;
        String projectName = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.PROJECT_NAME, "无项目名");
        collectUpload.project = projectName;
        collectUpload.origin = 0;
        collectUpload.startTime = System.currentTimeMillis();
        return collectUpload;
    }

    private static String getTagString(BaseFragment fragment) {
        return fragment.getClass().getSimpleName();
    }

    public static void pageStart(BaseFragment context) {
        String tag = getTagString(context);
        if (collectUploadData.get(tag) == null) {
            CollectData.CollectUpload upload = getCollectUploadBean();
            upload.startTime = System.currentTimeMillis();
            collectUploadData.put(tag, upload);
        }
    }

    public static void pageStart(BaseFragment context, Integer eventType) {
        String tag = getTagString(context);
        if (collectUploadData.get(tag) == null) {
            CollectData.CollectUpload upload = getCollectUploadBean();
            upload.eventType = eventType;
            collectUploadData.put(tag, upload);
        }
    }

    public static void pageStart(BaseFragment context, String eventCode) {
        String tag = getTagString(context);
        if (collectUploadData.get(tag) == null) {
            CollectData.CollectUpload upload = getCollectUploadBean();
            upload.eventCode = eventCode;
            collectUploadData.put(tag, upload);
        }
    }

    public static void pageStart(BaseFragment context, Integer eventType, String eventCode) {
        String tag = getTagString(context);
        if (collectUploadData.get(tag) == null) {
            CollectData.CollectUpload upload = getCollectUploadBean();
            upload.eventCode = eventCode;
            upload.eventType = eventType;
            collectUploadData.put(tag, upload);
        }
    }

    public static void pageStart(BaseFragment context, Integer eventType, String eventCode, JsonObject object) {
        String tag = getTagString(context);
        if (collectUploadData.get(tag) == null) {
            CollectData.CollectUpload upload = getCollectUploadBean();
            upload.eventCode = eventCode;
            upload.eventType = eventType;
            upload.extData = object;
            collectUploadData.put(tag, upload);
        }
    }

    public static void targetPageStart(BaseFragment context, String page) {
        if (needCollectByTarget(page)) {
            String tag = getTagString(context);
            if (collectUploadData.get(tag) == null) {
                CollectData.CollectUpload upload = getCollectUploadBean();
                upload.eventCode = SYSTEM_PAGE;
                upload.eventType = CHANGE;
                JsonObject object = new JsonObject();
                object.addProperty("targetKey", page);
                upload.extData = object;
                collectUploadData.put(tag, upload);
            }
        }
    }

    public static void targetPageEnd(BaseFragment context, String page) {
        if (needCollectByTarget(page)) {
            String tag = getTagString(context);
            CollectData.CollectUpload collectUpload = collectUploadData.get(tag);
            if (collectUpload == null) {
                CollectData.CollectUpload upload = getCollectUploadBean();
                upload.eventCode = SYSTEM_PAGE;
                upload.eventType = CHANGE;
                upload.endTime = System.currentTimeMillis();
                JsonObject object = new JsonObject();
                object.addProperty("targetKey", page);
                upload.extData = object;
                collectUploadData.put(tag, upload);
            } else {
                collectUpload.endTime = System.currentTimeMillis();
            }
        }
    }

    public static void pageEnd(BaseFragment context) {
        String tag = getTagString(context);
        CollectData.CollectUpload collectUpload = collectUploadData.get(tag);
        if (collectUpload == null) {
            CollectData.CollectUpload upload = getCollectUploadBean();
            upload.endTime = System.currentTimeMillis();
            collectUploadData.put(tag, upload);
        } else {
            collectUpload.endTime = System.currentTimeMillis();
        }
    }


    public static void saveCollectData() {
        if (collectUploadData.size() > 0) {
            List<CollectData.CollectUpload> mapValuesList = new ArrayList<>(collectUploadData.values());
            Gson gson = new Gson();
            String json = gson.toJson(mapValuesList);
            SPUtils.getInstance(SPKey.SP_MODEL_COLLECT).put(SPKey.COLLECT_DATA, json);
        }
    }

    public static String obtainCollectData() {
        return SPUtils.getInstance(SPKey.SP_MODEL_COLLECT).getString(SPKey.COLLECT_DATA, null);
    }
}
