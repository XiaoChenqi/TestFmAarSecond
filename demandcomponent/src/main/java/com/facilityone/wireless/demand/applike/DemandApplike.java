package com.facilityone.wireless.demand.applike;

import com.facilityone.wireless.componentservice.demand.DemandService;
import com.facilityone.wireless.demand.serviceimpl.DemandServiceImpl;
import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:类似于application，注册此组件可用
 * Date: 2018/3/7 上午10:15
 */
public class DemandApplike implements IApplicationLike {

    UIRouter mUIRouter =UIRouter.getInstance();
    Router mRouter = Router.getInstance();

    @Override
    public void onCreate() {
        mUIRouter.registerUI("demand");
        mRouter.addService(DemandService.class.getSimpleName(),new DemandServiceImpl());
    }

    @Override
    public void onStop() {
        mUIRouter.unregisterUI("demand");
        mRouter.removeService(DemandService.class.getSimpleName());
    }
}
