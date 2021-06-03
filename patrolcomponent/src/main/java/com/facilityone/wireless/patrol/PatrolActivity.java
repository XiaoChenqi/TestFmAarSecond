package com.facilityone.wireless.patrol;

import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMFragment;
import com.facilityone.wireless.a.arch.mvp.BaseFragmentActivity;
import com.facilityone.wireless.componentservice.common.empty.EmptyFragment;
import com.facilityone.wireless.componentservice.common.permissions.CommonConstant;
import com.facilityone.wireless.patrol.fragment.NfcFragment;
import com.facilityone.wireless.patrol.fragment.PatrolMenuFragment;
import com.luojilab.router.facade.annotation.RouteNode;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检首页
 * Date: 2018/7/3 下午4:07
 */

@RouteNode(path = "/patrolHome", desc = "巡检首页")
public class PatrolActivity extends BaseFragmentActivity implements EmptyFragment.OnGoFragmentListener {


    //再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0L;
    private EmptyFragment mInstance;

    @Override
    protected int getContextViewId() {
        return R.id.patrol_main_id;
    }

    @Override
    protected FMFragment setRootFragment() {
        mInstance = EmptyFragment.getInstance(CommonConstant.MESSAGE_PATROL);
        mInstance.setOnGoFragmentListener(this);
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }

    @Override
    public void goFragment(Bundle bundle) {
        PatrolMenuFragment temp = PatrolMenuFragment.getInstance(bundle);
        mInstance.startWithPop(temp);
//        NfcFragment temp = new NfcFragment();
//        mInstance.startWithPop(temp);
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
                ToastUtils.showShort(R.string.patrol_press_exit_again);
            }
        }
    }

    @Override
    public Object createPresenter() {
        return null;
    }
}
