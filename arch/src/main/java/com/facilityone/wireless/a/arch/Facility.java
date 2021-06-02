package com.facilityone.wireless.a.arch;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.facilityone.wireless.a.arch.ec.collect.CrashHandler;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.a.arch.utils.FMFileUtils;
import com.facilityone.wireless.a.arch.widget.FmClassicsFooter;
import com.facilityone.wireless.a.arch.widget.FmClassicsHeader;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.app.FMChannel;
import com.facilityone.wireless.basiclib.icon.FontResModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.listener.RecordFftDataListener;

import java.util.HashMap;
import java.util.Locale;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;
import okhttp3.OkHttpClient;


/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:业务全局初始化类
 * Date: 2018/10/15 4:45 PM
 */
public class Facility {
    private static final String CACHE_PATH = "com.fm.res";

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                FmClassicsHeader header = new FmClassicsHeader(context);
                return header;
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                FmClassicsFooter footer = new FmClassicsFooter(context);
                return footer;
            }
        });
    }

    public static void init(Context context) {
        init(context, BuildConfig.TEST_APP_SERVER, false);
    }

    public static void init(Context context, String appServer, boolean debug) {
        //TODO
        saveChannelParam();
        FM.init(context.getApplicationContext())
                .withIcon(new FontResModule())      //初始化字体图标
                .withIcon(new FontAwesomeModule())  //初始化字体图标
                .withFmRes(CACHE_PATH)              //媒体文件存储地址
                .configure(debug);

        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(debug) // 实际场景建议.debug(BuildConfig.DEBUG)
                /**
                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();

        initFmOkgo(context);
        initParams(appServer);
        if (ActivityCompat.checkSelfPermission(context.getApplicationContext()
                , Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            CrashUtils.init(FMFileUtils.getCrashPath());
            CrashHandler.getInstance().init(context);
        }

        //toast 样式设置
//        ToastUtils.setBgColor(context.getApplicationContext().getResources().getColor(R.color.bar_transparent));
//        ToastUtils.setGravity(Gravity.CENTER, 0, 20);
        initShare(context);
        initRecorder(context);
    }

    private static void initRecorder(Context context) {
        RecordManager.getInstance().init((Application) context.getApplicationContext(), false);
        RecordManager instance = RecordManager.getInstance();
        RecordManager.getInstance().changeRecordConfig(instance.getRecordConfig().setSampleRate(16000));
        RecordManager.getInstance().changeRecordConfig(instance.getRecordConfig().setEncodingConfig(AudioFormat.ENCODING_PCM_8BIT));
        RecordManager.getInstance().changeFormat(RecordConfig.RecordFormat.MP3);
        RecordManager.getInstance().changeRecordDir(FMFileUtils.getAudioPath());
        instance.setRecordFftDataListener(new RecordFftDataListener() {
            @Override
            public void onFftData(byte[] data) {
            }
        });
    }

    private static void initShare(Context context) {
        UMConfigure.init(context.getApplicationContext(), (String) FM.getConfiguration(FMChannel.CHANNEL_UMENG_KEY)
                , (String) FM.getConfiguration(FMChannel.CHANNEL_UMENG_VALUE), UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setQQZone((String) FM.getConfiguration(FMChannel.CHANNEL_QQZONE_KEY)
                , (String) FM.getConfiguration(FMChannel.CHANNEL_QQZONE_SECRET));
        PlatformConfig.setDing((String) FM.getConfiguration(FMChannel.CHANNEL_DING_DING_SECRET));
        PlatformConfig.setWeixin((String) FM.getConfiguration(FMChannel.CHANNEL_WEI_XIN_KEY)
                , (String) FM.getConfiguration(FMChannel.CHANNEL_WEI_XIN_SECRET));
    }

    private static void initFmOkgo(Context context) {
        //设置fm 需要的全局请求头和请求参数
//        String token = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.TOKEN);
//        if (!TextUtils.isEmpty(token)) {
//            FM.getConfigurator().withToken(token);
//            HttpHeaders httpHeaders = new HttpHeaders("Authorization", "Bearer " + token);
//            OkGo.getInstance().addCommonHeaders(httpHeaders);
//        }

        OkHttpClient.Builder builder = OkGo.getInstance().getOkHttpClient().newBuilder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(context)));
        OkGo.getInstance().init((Application) context.getApplicationContext()).setOkHttpClient(builder.build());

        FM.getConfigurator().withEmId(SPUtils.getInstance(SPKey.SP_MODEL_USER).getLong(SPKey.EM_ID));
        FM.getConfigurator().withEmName(SPUtils.getInstance(SPKey.SP_MODEL_USER).getString(SPKey.EM_NAME));
        FM.getConfigurator().withUserId(SPUtils.getInstance(SPKey.SP_MODEL_USER).getLong(SPKey.USER_ID));

        //TODO xcq
        Long projectId = SPUtils.getInstance(SPKey.SP_MODEL).getLong(SPKey.PROJECT_ID, -1L);
        projectId = 10L;

        FM.getConfigurator().withProjectId(projectId);

        HttpParams httpParams = new HttpParams("current_project", String.valueOf(projectId));

        httpParams.put("i18n", Locale.getDefault().toString());


        OkGo.getInstance().addCommonParams(httpParams);

    }

    private static void initParams(String appServer) {
        String localAppServer = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.APP_SERVER);
        String preAppServer = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.APP_PRE_SERVER);
        if (TextUtils.isEmpty(localAppServer)) {
            localAppServer = appServer;
        }

        if(!TextUtils.isEmpty(preAppServer) && !preAppServer.equals(appServer)){
            localAppServer = appServer;
        }

        if(TextUtils.isEmpty(preAppServer) || !preAppServer.equals(appServer)){
            SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.APP_PRE_SERVER, appServer);
        }

        SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.APP_SERVER, localAppServer);
        FM.getConfigurator().withApiHost(localAppServer);
    }

    private static void saveChannelParam() {
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
