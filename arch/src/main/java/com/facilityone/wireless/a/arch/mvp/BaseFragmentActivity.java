package com.facilityone.wireless.a.arch.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.base.FMFragmentActivity;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.PermissionHelper;
import com.lzy.okgo.OkGo;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.umeng.socialize.UMShareAPI;

import me.yokeyword.fragmentation.anim.DefaultNoAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/3/28 下午2:30
 */
public abstract class BaseFragmentActivity<P extends IPresent> extends FMFragmentActivity implements IView<P>, PermissionHelper.OnPermissionDeniedListener, PermissionHelper.OnPermissionGrantedListener {

    private P p;
    private QMUITipDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
        PermissionHelper.requestAll(this, this);
    }

    private void initDialog() {
        QMUITipDialog.Builder builder = new QMUITipDialog.Builder(this);
        builder.setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING);
        String tip = FM.getApplication().getResources().getString(R.string.dialog_request_loading);
        builder.setTipWord(tip);
        mDialog = builder.create();
    }

    public QMUITipDialog showLoading() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
        return mDialog;
    }

    public QMUITipDialog dismissLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        return mDialog;
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
    protected void onDestroy() {
        //清除未完成的网络请求
        OkGo.getInstance().cancelTag(this);

        //绑定的presenter解绑此view
        if (getPresenter() != null) {
            getPresenter().detachV();
        }
        p = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionDenied() {
        ToastUtils.showShort(R.string.arch_permission_deny_tip);
    }

    @Override
    public void onPermissionGranted() {

    }

    /**
     * 限制SwipeBack的条件,默认栈内Fragment数 <= 1时 , 优先滑动退出Activity , 而不是Fragment
     *
     * @return true: Activity优先滑动退出;  false: Fragment优先滑动退出
     */
    @Override
    public boolean swipeBackPriority() {
        return super.swipeBackPriority();
    }

    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultNoAnimator();
    }
}
