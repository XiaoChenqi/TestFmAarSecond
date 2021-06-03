package com.facilityone.wireless.workorder.applike;

import com.facilityone.wireless.componentservice.workorder.WorkorderService;
import com.facilityone.wireless.workorder.serviceimpl.WorkorderServiceImpl;
import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:类似于application，注册此组件可用
 * Date: 2018/7/3 下午4:10
 */
public class WorkorderApplike implements IApplicationLike {

    UIRouter mUIRouter = UIRouter.getInstance();
    Router mRouter = Router.getInstance();

    @Override
    public void onCreate() {
        mUIRouter.registerUI("workorder");
        mRouter.addService(WorkorderService.class.getSimpleName(), new WorkorderServiceImpl());
    }

    @Override
    public void onStop() {
        mUIRouter.unregisterUI("workorder");
        mRouter.removeService(WorkorderService.class.getSimpleName());
    }
}
