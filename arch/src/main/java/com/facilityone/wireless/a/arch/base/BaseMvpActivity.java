package com.facilityone.wireless.a.arch.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.utils.NoDoubleClickListener;
import com.facilityone.wireless.basiclib.app.FM;
import com.gyf.barlibrary.ImmersionBar;
import com.joanzapata.iconify.widget.IconTextView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description: 基础无侧滑(带返回以及标题)
 * Date: 2018/5/16 下午3:25
 */
public abstract class BaseMvpActivity extends FragmentActivity {

    protected QMUITopBarLayout mTopBarLayout;
    private QMUITipDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        initView();
        initTopbar();
        initLeftBack();
        initDialog();
    }

    private  void initLeftBack(){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mTopBarLayout.addLeftView(getLeftBackView(), R.id.topbar_left_back_id, lp);
    }

    public void initTopbar() {
        ImmersionBar.with(this)
                .titleBar(mTopBarLayout)
                .init();
    }

    private void initView() {
        mTopBarLayout = (QMUITopBarLayout) findViewById(setTitleBar());
        if (mTopBarLayout == null) {
            throw new NullPointerException("the topbar id is null");
        }
    }

    private View getLeftBackView() {
        IconTextView view = (IconTextView) LayoutInflater.from(this).inflate(R.layout.fm_topbar_back, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return view;
    }

    public TextView setTitle(String title) {
        TextView textView = null;
        if (mTopBarLayout != null) {
            textView = mTopBarLayout.setTitle(title);
            return textView;
        }
        return textView;
    }

    /**
     * 添加右边文本菜单
     *
     * @param text
     * @param id
     */
    public void setRightTextButton(String text, @IdRes int id) {
        Button button = null;
        if (mTopBarLayout != null) {
            button = mTopBarLayout.addRightTextButton(text, id);
        }
        if (button == null) {
            return;
        }
        button.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                onRightTextMenuClick(view);
            }
        });
    }

    public void onRightTextMenuClick(View view) {
    }

    private void initDialog() {
        QMUITipDialog.Builder builder = new QMUITipDialog.Builder(this);
        builder.setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING);
        String tip = FM.getApplication().getResources().getString(R.string.dialog_request_loading);
        builder.setTipWord(tip);
        mDialog = builder.create();
        mDialog.setCancelable(false);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }

    protected abstract int setLayout();

    protected abstract int setTitleBar();
}
