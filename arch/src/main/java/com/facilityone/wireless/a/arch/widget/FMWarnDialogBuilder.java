package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.utils.NoDoubleClickListener;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.joanzapata.iconify.widget.IconTextView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:警告弹窗
 * Date: 2018/7/18 下午5:48
 */
public class FMWarnDialogBuilder extends QMUIDialogBuilder<FMWarnDialogBuilder> {

    private IconTextView mItvIcon;
    private TextView mTvTitle;
    private TextView mTvTip;
    private Button mBtnCancel;
    private Button mBtnSure;
    private View mBtnCenterV;
    private int mCancelTextColor;
    private int mSureTextColor;

    private Context mContext;
    private OnBtnClickListener mOnBtnCancelClickListener;
    private OnBtnClickListener mOnBtnSureClickListener;

    private String title, tip, icon, btnCancel, btnSure;
    private boolean showIcon, showSureBlueBg ,ancelVisiable;

    public FMWarnDialogBuilder(Context context) {
        super(context);
        mContext = context;
        showIcon = true;
        ancelVisiable = true;
        showSureBlueBg = false;
    }

    @Override
    protected void onCreateContent(final QMUIDialog dialog, ViewGroup parent, Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_warn_tip, null, false);
        mItvIcon = (IconTextView) view.findViewById(R.id.itv_icon);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvTip = (TextView) view.findViewById(R.id.tv_tip);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnSure = (Button) view.findViewById(R.id.btn_sure);
        mBtnCenterV = view.findViewById(R.id.btn_center_v);
        mBtnCancel.setTextColor(mCancelTextColor == 0 ? mContext.getResources().getColor(R.color.grey_9) : mContext.getResources().getColor(mCancelTextColor));
        mBtnSure.setTextColor(mSureTextColor == 0 ? mContext.getResources().getColor(R.color.colorPrimary) : mContext.getResources().getColor(mSureTextColor));

        mTvTitle.setText(StringUtils.formatString(title, mContext.getString(R.string.arch_warning)));
        mItvIcon.setVisibility(View.GONE);
        mItvIcon.setText(StringUtils.formatString(icon, mContext.getResources().getString(R.string.icon_warn)));
        if (TextUtils.isEmpty(tip)) {
            mTvTip.setVisibility(View.GONE);
        } else {
            mTvTip.setVisibility(View.VISIBLE);
            mTvTip.setText(tip);
        }

        mBtnCancel.setText(StringUtils.formatString(btnCancel, mContext.getString(R.string.arch_cancel)));
        mBtnCancel.setVisibility(ancelVisiable?View.VISIBLE:View.GONE);
        mBtnCenterV.setVisibility(ancelVisiable?View.VISIBLE:View.GONE);
        mBtnSure.setText(StringUtils.formatString(btnSure, mContext.getString(R.string.arch_confirm)));

//        mBtnSure.setBackgroundResource(showSureBlueBg ? R.drawable.btn_blue_selector_bg : R.drawable.btn_red_selector_bg);

        mBtnCancel.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (mOnBtnCancelClickListener != null) {
                    mOnBtnCancelClickListener.onClick(dialog, view);
                } else {
                    dialog.dismiss();
                }
            }
        });

        mBtnSure.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (mOnBtnSureClickListener != null) {
                    mOnBtnSureClickListener.onClick(dialog, view);
                }
            }
        });
        parent.addView(view);
    }

    public FMWarnDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public FMWarnDialogBuilder setTitle(@StringRes int title) {
        return setTitle(mContext.getResources().getString(title));
    }

    public FMWarnDialogBuilder setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public FMWarnDialogBuilder setIcon(@StringRes int icon) {
        return setIcon(mContext.getResources().getString(icon));
    }

    public FMWarnDialogBuilder setIconVisible(boolean showIcon) {
        this.showIcon = showIcon;
        return this;
    }

    public FMWarnDialogBuilder setTip(String tip) {
        this.tip = tip;
        return this;
    }

    public FMWarnDialogBuilder setTip(@StringRes int tip) {
        return setTip(mContext.getResources().getString(tip));
    }

    public FMWarnDialogBuilder setCancel(String cancel) {
        this.btnCancel = cancel;
        return this;
    }

    public FMWarnDialogBuilder setCancel(@StringRes int cancel) {
        return setCancel(mContext.getResources().getString(cancel));
    }

    public FMWarnDialogBuilder setSure(String sure) {
        this.btnSure = sure;
        return this;
    }
    
    public FMWarnDialogBuilder setSureBluBg(boolean bgBlue) {
        this.showSureBlueBg = bgBlue;
        return this;
    }

    public FMWarnDialogBuilder setSure(@StringRes int sure) {
        return setSure(mContext.getResources().getString(sure));
    }

    public IconTextView getItvIcon() {
        return mItvIcon;
    }

    public void setItvIcon(IconTextView itvIcon) {
        mItvIcon = itvIcon;
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        mTvTitle = tvTitle;
    }

    public TextView getTvTip() {
        return mTvTip;
    }

    public void setTvTip(TextView tvTip) {
        mTvTip = tvTip;
    }

    public Button getBtnCancel() {
        return mBtnCancel;
    }

    public void setBtnCancel(Button btnCancel) {
        mBtnCancel = btnCancel;
    }

    public Button getBtnSure() {
        return mBtnSure;
    }

    public void setBtnSure(Button btnSure) {
        mBtnSure = btnSure;
    }

    public FMWarnDialogBuilder addOnBtnCancelClickListener(OnBtnClickListener onBtnClickListener) {
        mOnBtnCancelClickListener = onBtnClickListener;
        return this;
    }

    public FMWarnDialogBuilder addOnBtnSureClickListener(OnBtnClickListener onBtnClickListener) {
        mOnBtnSureClickListener = onBtnClickListener;
        return this;
    }

    public FMWarnDialogBuilder setCancelVisiable(boolean visiable) {
        this.ancelVisiable = visiable;
        return this;
    }


    public void setCancelTextColor(int mCancelTextColor) {
        this.mCancelTextColor = mCancelTextColor;
    }

    public void setSureTextColor(int mSureTextColor) {
        this.mSureTextColor = mSureTextColor;
    }

    public interface OnBtnClickListener {
        void onClick(QMUIDialog dialog, View view);
    }
}
