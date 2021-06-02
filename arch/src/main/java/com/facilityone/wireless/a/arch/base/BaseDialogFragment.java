package com.facilityone.wireless.a.arch.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.mvp.IPresent;
import com.facilityone.wireless.a.arch.mvp.IView;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:实现沉浸式的基类 dialog
 * Date: 2018/7/6 下午5:05
 */
public abstract class BaseDialogFragment<P extends IPresent> extends DialogFragment implements IView<P> {

    protected Activity mActivity;
    protected View mRootView;

    protected ImmersionBar mImmersionBar;
    protected Window mWindow;
    protected int mWidth;  //屏幕宽度
    protected int mHeight;  //屏幕高度
    private P p;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);  //点击外部消失
        mWindow = dialog.getWindow();
        //测量宽高
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            mWidth = dm.widthPixels;
            mHeight = dm.heightPixels;
        } else {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            mWidth = metrics.widthPixels;
            mHeight = metrics.heightPixels;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        return mRootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        initData();
        setListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this, getDialog());
        mImmersionBar.init();
    }


    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * view与数据绑定
     */
    protected void initView() {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }

    /**
     * 获取presenter并绑定此view
     *
     * @return
     */
    @Override
    public P getPresenter() {
        if (p == null) {
            p = createPresenter();
            if (p != null) {
                p.attachV(this);
            }
        }
        return p;
    }

    @Override
    public void onDestroyView() {
        //绑定的presenter解绑此view
        if (getPresenter() != null) {
            getPresenter().detachV();
        }

        p = null;
        super.onDestroyView();
    }

    /**
     * 找到activity的控件
     *
     * @param <T> the type parameter
     * @param id  the id
     * @return the t
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(@IdRes int id) {
        return (T) mRootView.findViewById(id);
    }
}
