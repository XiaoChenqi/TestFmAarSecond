package com.facilityone.wireless.patrol.applike;

import com.facilityone.wireless.componentservice.patrol.PatrolService;
import com.facilityone.wireless.patrol.serviceimpl.PatrolServiceImpl;
import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:类似于application，注册此组件可用
 * Date: 2018/10/30 2:54 PM
 */
public class PatrolApplike implements IApplicationLike {

    UIRouter mUIRouter = UIRouter.getInstance();
    Router mRouter = Router.getInstance();

    @Override
    public void onCreate() {
        mUIRouter.registerUI("patrol");
        mRouter.addService(PatrolService.class.getSimpleName(), new PatrolServiceImpl());
    }

    @Override
    public void onStop() {
        mUIRouter.unregisterUI("patrol");
        mRouter.removeService(PatrolService.class.getSimpleName());
    }
}
