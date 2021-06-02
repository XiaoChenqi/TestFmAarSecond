package com.facilityone.wireless.a.arch.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.blankj.utilcode.util.LogUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:基础activity fmFragment依赖的activity
 * Date: 2018/3/29 下午6:46
 */
public abstract class FMFragmentActivity extends CommonActivity {
    private QMUIWindowInsetLayout mFragmentContainer;
    private InputMethodManager imm;
    protected ImmersionBar mImmersionBar;

    @SuppressWarnings("SameReturnValue")
    protected abstract int getContextViewId();

    protected abstract FMFragment setRootFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContainer(savedInstanceState);
        LogUtils.d("Gary","MainActivity 启动。。。。。。。");
    }

    /**
     * 设置统一的父布局容器
     */
    private void initContainer(@Nullable Bundle savedInstanceState) {
        mFragmentContainer = new QMUIWindowInsetLayout(this);
        mFragmentContainer.setId(getContextViewId());
        setContentView(mFragmentContainer);
        //初始化沉浸式
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        if (savedInstanceState == null) {
            loadRootFragment(getContextViewId(), setRootFragment());
        }
    }

    public QMUIWindowInsetLayout getFragmentContainer() {
        return mFragmentContainer;
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);//.statusBarColorInt(getResources().getColor(R.color.bar_transparent));
        mImmersionBar.init();
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    public void finish() {
        super.finish();
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.imm = null;
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
    }
}
