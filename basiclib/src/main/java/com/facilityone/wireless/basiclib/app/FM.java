package com.facilityone.wireless.basiclib.app;

import android.content.Context;
import android.os.Handler;

import java.util.HashMap;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:初始化配置工具
 * Date: 2018/4/8 下午10:21
 */
public class FM {

    public static Configurator init(Context context) {
        getFMConfigs().put(ConfigType.APPLICATION_CONTEXT, context.getApplicationContext());
        return Configurator.getInstance();
    }

    public static Configurator getConfigurator() {
        return Configurator.getInstance();
    }
    
    public static HashMap<Object, Object> getFMConfigs() {
        return Configurator.getInstance().getFMConfigs();
    }

    public static Context getApplication() {
        return getConfiguration(ConfigType.APPLICATION_CONTEXT);
    }

    public static <T> T getConfiguration(Object key) {
        return getConfigurator().getConfiguration(key);
    }

    public static Handler getHandler() {
        return getConfiguration(ConfigType.HANDLER);
    }

    public static String getApiHost() {
        return getConfiguration(ConfigType.API_HOST);
    }

    public static String getToken() {
        return getConfiguration(ConfigType.TOKEN);
    }

    public static Long getUserId() {
        return getConfiguration(ConfigType.USER_ID);
    }
    
    public static Long getProjectId() {
        return getConfiguration(ConfigType.PROJECT_ID);
    }

    public static Long getEmId() {
        return getConfiguration(ConfigType.EM_ID);
    }
    
    public static String getEmName() {
        return getConfiguration(ConfigType.EM_NAME);
    }
    
    public static String getCachePath() {
        return getConfiguration(ConfigType.FM_CACHE_RES);
    }
}
