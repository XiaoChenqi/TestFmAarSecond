package com.facilityone.wireless.workorder.fragment;

import android.view.Gravity;
import android.view.View;

import com.blankj.utilcode.util.KeyboardUtils;
import com.facilityone.wireless.a.arch.base.BaseDialogFragment;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.presenter.WorkQueryMenuDialogPresenter;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单查询页面dialog菜单
 * Date: 2018/7/6 下午5:16
 */
public class WorkQueryMenuDialogFragment extends BaseDialogFragment<WorkQueryMenuDialogPresenter> {

    private View mTopView;

    @Override
    public WorkQueryMenuDialogPresenter createPresenter() {
        return new WorkQueryMenuDialogPresenter();
    }

    @Override
    public QMUITipDialog showLoading() {
        return null;
    }

    @Override
    public QMUITipDialog dismissLoading() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_workorder_query_menu;
    }

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.TOP | Gravity.END);
        mWindow.setWindowAnimations(R.style.RightDialog);
        mWindow.setLayout(mWidth * 2 / 3, mHeight);
    }

    @Override
    protected void initView() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KeyboardUtils.hideSoftInput(mActivity);
    }
}
