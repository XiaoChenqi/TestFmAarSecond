package com.facilityone.wireless.basiclib.network.callback;

import android.app.Activity;

import com.facilityone.wireless.basiclib.R;
import com.facilityone.wireless.basiclib.app.FM;
import com.fm.tool.network.callback.JsonCallback;
import com.lzy.okgo.request.base.Request;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:带dialog的网络请求回调
 * Date: 2018/4/9 下午3:15
 */
public abstract class DialogCallback<T> extends JsonCallback<T> {

    private QMUITipDialog mDialog;

    public DialogCallback(Activity activity) {
        this(activity, null);
    }

    public DialogCallback(Activity activity, String tip) {
        super();
        initDialog(activity, tip);
    }

    private void initDialog(Activity activity, String tip) {
        QMUITipDialog.Builder builder = new QMUITipDialog.Builder(activity);
        builder.setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING);
        if (tip == null) {
            tip = FM.getApplication().getResources().getString(R.string.dialog_request_loading);
        }
        builder.setTipWord(tip);
        mDialog = builder.create();
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void onFinish() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
