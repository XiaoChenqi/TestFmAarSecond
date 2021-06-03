package com.facilityone.wireless.workorder.runalone;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.InitApplication;
import com.facilityone.wireless.a.arch.BaseApplication;
import com.facilityone.wireless.a.arch.Facility;
import com.facilityone.wireless.workorder.BuildConfig;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/3 下午4:00
 */
public class WorkOrderApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);
        UIRouter.getInstance().registerUI("workorder");
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);
        //UIRouter.getInstance().registerUI("demand");
        Router.registerComponent("com.facilityone.wireless.workorder.applike.WorkorderApplike");
        Facility.init(this);
    }
}
