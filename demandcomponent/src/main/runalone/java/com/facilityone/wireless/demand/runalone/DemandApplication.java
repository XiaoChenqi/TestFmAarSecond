package com.facilityone.wireless.demand.runalone;


import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.a.arch.BaseApplication;
import com.facilityone.wireless.a.arch.Facility;
import com.facilityone.wireless.demand.BuildConfig;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/3/7 上午10:14
 */
public class DemandApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);
        UIRouter.getInstance().registerUI("demand");
        Router.registerComponent("com.facilityone.wireless.workorder.applike.WorkorderApplike");
        Facility.init(this);
    }
}
