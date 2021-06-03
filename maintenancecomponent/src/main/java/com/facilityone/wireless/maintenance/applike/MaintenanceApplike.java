package com.facilityone.wireless.maintenance.applike;

import com.facilityone.wireless.componentservice.maintenance.MaintenanceService;
import com.facilityone.wireless.maintenance.serviceimpl.MaintenanceServiceImpl;
import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Created by peter.peng on 2018/11/15.
 */

public class MaintenanceApplike implements IApplicationLike {

    UIRouter mUiRouter = UIRouter.getInstance();
    Router mRouter = Router.getInstance();
    @Override
    public void onCreate() {
        mUiRouter.registerUI("maintenance");
        mRouter.addService(MaintenanceService.class.getSimpleName(),new MaintenanceServiceImpl());
    }

    @Override
    public void onStop() {
        mUiRouter.unregisterUI("maintenance");
        mRouter.removeService(MaintenanceService.class.getSimpleName());
    }
}
