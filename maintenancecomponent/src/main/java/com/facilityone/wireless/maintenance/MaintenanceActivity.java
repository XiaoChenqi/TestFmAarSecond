package com.facilityone.wireless.maintenance;

import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMFragment;
import com.facilityone.wireless.a.arch.mvp.BaseFragmentActivity;
import com.facilityone.wireless.componentservice.common.empty.EmptyFragment;
import com.facilityone.wireless.componentservice.common.permissions.CommonConstant;
import com.facilityone.wireless.maintenance.fragment.MaintenanceFragment;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;

@RouteNode(path = "/maintenanceHome", desc = "计划性维护")
public class MaintenanceActivity extends BaseFragmentActivity implements EmptyFragment.OnGoFragmentListener {

    //再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0L;
    private EmptyFragment mInstance;

    @Override
    protected int getContextViewId() {
        return R.id.maintenance_main_id;
    }

    @Override
    protected FMFragment setRootFragment() {
        //todo xcq
        //这边应该加个登录的判断
        mInstance = EmptyFragment.getInstance(CommonConstant.MESSAGE_MAINTANCE);
        mInstance.setOnGoFragmentListener(this);

//        Bundle bundle = new Bundle();
//        UIRouter.getInstance().openUri(this, "DDComp://maintenance/maintenanceHome", bundle);

        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }

    @Override
    public void goFragment(Bundle bundle) {
        mInstance.startWithPop(MaintenanceFragment.getInstance(bundle));
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                this.finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                ToastUtils.showShort(R.string.maintenance_press_exit_again);
            }
        }
    }

    @Override
    public Object createPresenter() {
        return null;
    }

}
