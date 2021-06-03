package com.facilityone.wireless.patrol.runalone;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.InitApplication;
import com.facilityone.wireless.a.arch.BaseApplication;
import com.facilityone.wireless.a.arch.Facility;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.patrol.BuildConfig;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/10/30 2:43 PM
 */
public class PatrolApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);
        UIRouter.getInstance().registerUI("patrol");
        Router.registerComponent("com.facilityone.wireless.workorder.applike.WorkorderApplike");
        Facility.init(this);
        //LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);
        //Router.registerComponent("com.facilityone.wireless.workorder.applike.WorkorderApplike");
        //Facility.init(this);
        //DBManager.getInstance();
    }
}
