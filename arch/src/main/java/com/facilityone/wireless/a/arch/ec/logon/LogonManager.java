package com.facilityone.wireless.a.arch.ec.logon;

import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.facilityone.wireless.a.arch.ec.module.LogonResponse;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.GsonUtils;
import com.fm.tool.network.callback.JsonCallback;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:登录管理
 * Date: 2018/5/28 下午2:41
 */
public class LogonManager {
    private static final String GRANT_TYPE_TOKEN = "password";
    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    private static final String APP_TYPE = "android";

    private static class Holder {
        private static final LogonManager LOGON_MANAGER = new LogonManager();
    }

    public static LogonManager getInstance() {
        return Holder.LOGON_MANAGER;
    }
String TAG = "zhouyang";
    public void logon(String username, String password, JsonCallback<BaseResponse<LogonResponse>> jsonCallback) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("source", "app");
        params.put("loginCode", username);
        params.put("loginPwd", password);
        params.put("appType", APP_TYPE);
        //TODO xcq
        //params.put("appVersion", AppUtils.getAppVersionName());
        params.put("appVersion", "0.0.0");
        String loginJson = "";
        try {
            loginJson = GsonUtils.toJson(params, false);
        } catch (Exception e) {
            loginJson = "";
        }
        //TODO xcq 改ip，不能写死，要写入保存，后面还要用
        String str = "http://192.168.1.66:8080";
        SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.APP_SERVER, str);
        SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.USER_EDIT_SERVER, true);
        FM.getConfigurator().withApiHost(str);
        //deleteData(dialog);//TODO 这个方法可能需要使用


        String tempUrl = FM.getApiHost()+UserUrl.LOGON_URL;
        //String tempUrl2 ="http://192.168.1.66"+UserUrl.LOGON_URL;
        Log.d(TAG, "logon: "+loginJson);

        Log.d("zhouyang", "logon: "+tempUrl+"  -~~~~~-   ");

        //ToDO 要修改okgo的头部参数

        OkGo.<BaseResponse<LogonResponse>>post(tempUrl)
                .isSpliceUrl(true)
                .upJson(loginJson)
                .execute(jsonCallback);
    }

    public void saveToken(LogonResponse data) {
        if (data == null) {
            return;
        }
        FM.getConfigurator().withUserId(data.userId);
    }
}
