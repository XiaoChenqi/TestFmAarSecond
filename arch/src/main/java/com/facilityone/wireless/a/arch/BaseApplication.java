package com.facilityone.wireless.a.arch;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/3/6 上午10:03
 */
public class BaseApplication extends Application {
//    private RefWatcher refWatcher;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        //初始化内存泄漏检测工具LeakCanary
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        refWatcher = LeakCanary.install(this);
//        
//    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        BaseApplication application = (BaseApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
}
