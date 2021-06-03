package com.facilityone.wireless.maintenance.runalone;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.InitApplication;
import com.facilityone.wireless.a.arch.BaseApplication;
import com.facilityone.wireless.a.arch.Facility;
import com.facilityone.wireless.maintenance.BuildConfig;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Created by peter.peng on 2018/11/15.
 */

public class MaintenanceApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);
        UIRouter.getInstance().registerUI("maintenance");
        Router.registerComponent("com.facilityone.wireless.workorder.applike.WorkorderApplike");
        Facility.init(this);
    }


}
