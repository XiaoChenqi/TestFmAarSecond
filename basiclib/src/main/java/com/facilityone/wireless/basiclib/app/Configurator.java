package com.facilityone.wireless.basiclib.app;

import android.content.Context;
import android.os.Handler;

import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.Utils;
import com.fm.tool.network.interceptor.HttpLoggingInterceptor;
import com.fm.tool.network.utils.NetWorkUtils;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:全局配置
 * Date: 2018/4/8 下午6:04
 */
public class Configurator {

    private static final HashMap<Object, Object> FM_CONFIGS = new HashMap<>();
    private static final Handler HANDLER = new Handler();
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();

    private Configurator() {
        FM_CONFIGS.put(ConfigType.CONFIG_READY, false);
        FM_CONFIGS.put(ConfigType.HANDLER, HANDLER);
    }

    public static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    final HashMap<Object, Object> getFMConfigs() {
        return FM_CONFIGS;
    }

    //添加字体
    public final Configurator withIcon(IconFontDescriptor descriptor) {
        ICONS.add(descriptor);
        return this;
    }

    //设置主机地址
    public final Configurator withApiHost(String host) {
        FM_CONFIGS.put(ConfigType.API_HOST, host);
        return this;
    }

    //设置全局token
    public final Configurator withToken(String token) {
        FM_CONFIGS.put(ConfigType.TOKEN, token);
        return this;
    }

    public final Configurator withUserId(Long userId) {
        FM_CONFIGS.put(ConfigType.USER_ID, userId);
        return this;
    }

    public final Configurator withProjectId(Long projectId) {
        FM_CONFIGS.put(ConfigType.PROJECT_ID, projectId);
        return this;
    }

    public final Configurator withEmId(Long emId) {
        FM_CONFIGS.put(ConfigType.EM_ID, emId);
        return this;
    }

    public final Configurator withEmName(String emName) {
        FM_CONFIGS.put(ConfigType.EM_NAME, emName);
        return this;
    }

    //全局资源路径
    public final Configurator withFmRes(String path) {
        StringBuilder sb;
        if (SDCardUtils.isSDCardEnableByEnvironment()) {
            sb = new StringBuilder(SDCardUtils.getSDCardPathByEnvironment());
        } else {
            sb = new StringBuilder(FM.getApplication().getCacheDir().getAbsolutePath());
        }
        sb.append("/").append(path).append("/");
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }

        FM_CONFIGS.put(ConfigType.FM_CACHE_RES, sb.toString());
        return this;
    }

    public final void configure(boolean debug) {
        //初始化android工具类
        Utils.init((Context) FM_CONFIGS.get(ConfigType.APPLICATION_CONTEXT));
        initIcons();
        //初始化okhttp
        if (debug) {
            NetWorkUtils.initOkhttp((Context) FM_CONFIGS.get(ConfigType.APPLICATION_CONTEXT), HttpLoggingInterceptor.Level.BODY);
        } else {
            NetWorkUtils.initOkhttp((Context) FM_CONFIGS.get(ConfigType.APPLICATION_CONTEXT));
        }
        FM_CONFIGS.put(ConfigType.CONFIG_READY, true);
    }

    private void initIcons() {
        if (ICONS.size() > 0) {
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i < ICONS.size(); i++) {
                initializer.with(ICONS.get(i));
            }
        }
    }

    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key) {
        checkConfiguration();
        return (T) FM_CONFIGS.get(key);
    }

    //检查配置
    private void checkConfiguration() {
        final boolean isReady = (boolean) FM_CONFIGS.get(ConfigType.CONFIG_READY);
        if (!isReady) {
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }

    //静态内部类线程安全模式--懒汉单例模式
    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }

}
