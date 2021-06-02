package com.facilityone.wireless.a.arch.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.a.arch.BuildConfig;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.app.FMChannel;

import java.util.HashMap;
import java.util.Locale;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/5/29 下午5:59
 */
public class UrlUtils {

    /**
     * 获取系统语言
     * @return
     */
    public static String getSystemLanguage() {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage() + "_" + locale.getCountry();
        return language;
    }

    /**
     * 根据id获取图片真实路径
     *
     * @param id 图片id
     * @return url
     */
    public static String getImagePath(String id) {
        if (TextUtils.isEmpty(id)) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(FM.getApiHost());
        sb.append(id);

        LogUtils.d(sb.toString());
        return sb.toString();
    }


    //附件地址
    public static String getAttachmentPath(String id) {
        if (TextUtils.isEmpty(id)) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(FM.getApiHost());
        sb.append(id);

        LogUtils.d(sb.toString());
        return sb.toString();
    }

    public static String getMediaPath(String id) {
        if (TextUtils.isEmpty(id)) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(FM.getApiHost());
        sb.append(id);

        LogUtils.d(sb.toString());
        return sb.toString();
    }

    /**
     * 获取音频时长
     *
     * @param mUri
     * @return
     */
    public static int getRingDuring(String mUri) {
        String duration = null;
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();

        try {
            if (mUri != null) {
                HashMap<String, String> headers = null;
                if (headers == null) {
                    headers = new HashMap<>();
                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                }
                mmr.setDataSource(mUri, headers);
            }

            duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
            return Integer.parseInt(duration);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        } finally {
            mmr.release();
        }
    }

    ///file/download/{IMAGE_FILE_NAME} App管理平台获取图片
    public static String getAppManagerImage(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(BuildConfig.SERVER_UPDATE_URL);
        sb.append("/file/download/");
        sb.append(fileName);
        return sb.toString();
    }

    //访客分享链接
    public static String getShareUrl(Long orderId) {
        if (orderId == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(FM.getApiHost());
        sb.append("/wechat/view/visitor/invitation/share/");
        sb.append(FM.getProjectId() + "/");
        sb.append(orderId);
        return sb.toString();
    }

    //apk下载链接
    public static String getApkDownloadUrl() {
        if (TextUtils.isEmpty((String)FM.getConfiguration(FMChannel.CHANNEL_CUSTOMER_CODE))) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(BuildConfig.SERVER_UPDATE_URL);
        sb.append("/quickdownload/customer/");
        sb.append((String)FM.getConfiguration(FMChannel.CHANNEL_CUSTOMER_CODE));
        return sb.toString();
    }

    public static String getServerImage(String name) {
        if(TextUtils.isEmpty(name)){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(FM.getApiHost());
        sb.append("/resource/img/kb/");
        sb.append(name);
        return sb.toString();
    }
    public static String getKnowledgeDetailUrl(Long id){
        if (id==null){
            return "";
        }
        String androidID = DeviceUtils.getDeviceId();
        StringBuffer sb = new StringBuffer();
        sb.append(FM.getApiHost());
        sb.append("/m/v1/repository/detail/");
        sb.append(getSystemLanguage());
        sb.append("?access_token=");
        sb.append("&docId=");
        sb.append(id);
        sb.append("&device_id=");
        if (!TextUtils.isEmpty(androidID)){
            sb.append(androidID);
        }
        sb.append("&current_projectId=");
        sb.append(FM.getProjectId());
        sb.append("&device-type=");
        sb.append("android");
        return  sb.toString();
    }
}
