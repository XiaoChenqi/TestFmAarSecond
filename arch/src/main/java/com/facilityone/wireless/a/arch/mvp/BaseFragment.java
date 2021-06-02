package com.facilityone.wireless.a.arch.mvp;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.base.FMFragment;
import com.facilityone.wireless.a.arch.utils.NoDoubleClickListener;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.lzy.okgo.OkGo;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/3/28 下午3:15
 */
public abstract class BaseFragment<P extends IPresent> extends FMFragment implements IView<P> {

    private P p;
    private QMUITipDialog mDialog;
    private TextView mTip;


    private void initDialog() {
        QMUITipDialog.Builder builder = new QMUITipDialog.Builder(getContext());
        builder.setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING);
        String tip = FM.getApplication().getResources().getString(R.string.dialog_request_loading);
        builder.setTipWord(tip);
        mDialog = builder.create();
        mDialog.setCancelable(false);
    }

    public QMUITipDialog showLoading() {
        return showLoading(false);
    }

    public QMUITipDialog showLoading(boolean cancelable) {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.setCancelable(cancelable);
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

    public QMUITipDialog initProgressBarLoading() {
        QMUITipDialog.CustomBuilder builder = new QMUITipDialog.CustomBuilder(getContext());
        builder.setContent(R.layout.dialog_progress_loading);
        QMUITipDialog qmuiTipDialog = builder.create();
        mTip = (TextView) qmuiTipDialog.findViewById(R.id.tv_tip);
        return qmuiTipDialog;
    }

    public void setTipView(String tip) {
        if (mTip != null) {
            mTip.setText(StringUtils.formatString(tip));
        }
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
    public void onPause() {
        super.onPause();
        hideSoftInput();
    }

    @Override
    public void onDestroyView() {
        //清除未完成的网络请求
        OkGo.getInstance().cancelTag(this);

        //绑定的presenter解绑此view
        if (getPresenter() != null) {
            getPresenter().detachV();
        }

        p = null;
        dismissLoading();
        super.onDestroyView();
    }

    //加载view
    public View getLoadingView(ViewGroup viewGroup) {
        return getLoadingView(viewGroup, R.string.loading);
    }

    public View getLoadingView(ViewGroup viewGroup, String msg) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.loading_view, viewGroup, false);
        if (!TextUtils.isEmpty(msg)) {
            TextView msgTv = (TextView) inflate.findViewById(R.id.tv_msg);
            msgTv.setText(msg);
        }
        return inflate;
    }

    public View getLoadingView(ViewGroup viewGroup, @StringRes int msg) {
        return getLoadingView(viewGroup, getResources().getString(msg));
    }

    //出错view
    public View getErrorView(ViewGroup viewGroup) {
        return getErrorView(viewGroup, R.string.empty_network_error);
    }

    public View getErrorView(ViewGroup viewGroup, String msg) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.error_view, viewGroup, false);
        view.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                onNoDataOrErrorClick(view);
            }
        });
        if (!TextUtils.isEmpty(msg)) {
            TextView msgTv = (TextView) view.findViewById(R.id.tv_msg);
            msgTv.setText(msg);
        }
        return view;
    }

    public View getErrorView(ViewGroup viewGroup, @StringRes int msg) {
        return getErrorView(viewGroup, getResources().getString(msg));
    }

    //无数据view
    public View getNoDataView(ViewGroup viewGroup) {
        return getNoDataView(viewGroup, R.string.empty_no_data);
    }

    public View getNoDataView(ViewGroup viewGroup, String msg,@DrawableRes int drawable) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.empty_view, viewGroup, false);
        view.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                onNoDataOrErrorClick(view);
            }
        });

        if (!TextUtils.isEmpty(msg)) {
            TextView msgTv = (TextView) view.findViewById(R.id.tv_msg);
            msgTv.setText(msg);
        }
        if(drawable != -1) {
            ImageView emptyView = (ImageView) view.findViewById(R.id.empty_view_iv);
            emptyView.setBackgroundResource(drawable);
        }
        return view;
    }

    public View getNoDataView(ViewGroup viewGroup, @StringRes int msg, @DrawableRes int drawable) {
        return getNoDataView(viewGroup, getResources().getString(msg), drawable);
    }

    public View getNoDataView(ViewGroup viewGroup, @StringRes int msg) {
        return getNoDataView(viewGroup, getResources().getString(msg),-1);
    }

    /**
     * 无数据或者出错的时候点击处理事件
     */
    public void onNoDataOrErrorClick(View view) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setParallaxOffset(0.5F);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onViewCreated(view, savedInstanceState);
        if (isUseDialog()) {
            initDialog();
        }
    }

    public boolean isUseDialog() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }


}
