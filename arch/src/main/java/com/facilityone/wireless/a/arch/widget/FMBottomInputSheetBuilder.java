package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.basiclib.utils.DataUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.Locale;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:输入dialog
 * Date: 2018/7/2 上午10:15
 */
public class FMBottomInputSheetBuilder implements TextWatcher, View.OnClickListener {

    public Context mContext;
    private QMUIBottomSheet mDialog;
    private EditText mDescEt;
    private TextView mCountTv;

    private int mMaxNumber = 200;

    private int selectionStart;
    private int selectionEnd;
    private CharSequence temp;
    private TextView mTitle;
    private TextView mTipTv;
    private Button mBtn;
    private Button mLeftBtn;
    private Button mRightBtn;
    private LinearLayout mLLTwoBtn;
    private String showTip;
    private boolean singleNeedInput;//单个按钮的时候是否需要输入内容才可以保存
    private boolean twoNeedLeftInput;//两个按钮的时候是否需要输入内容才可以保存
    private boolean twoNeedRightInput;//两个按钮的时候是否需要输入内容才可以保存

    public FMBottomInputSheetBuilder(Context context) {
        this.mContext = context;
        showTip = context.getString(R.string.arch_work_content_hint);
        singleNeedInput = true;
        twoNeedLeftInput = true;
        twoNeedRightInput = true;
    }

    public QMUIBottomSheet build() {
        mDialog = new QMUIBottomSheet(mContext);
        View contentView = buildViews();
        mDialog.setContentView(contentView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return mDialog;
    }

    private View buildViews() {
        View wrapperView = View.inflate(mContext, getContentViewLayoutId(), null);
        buildViews(wrapperView);
        mDescEt = (EditText) wrapperView.findViewById(R.id.et_input);
        mCountTv = (TextView) wrapperView.findViewById(R.id.tv_count);
        mTitle = (TextView) wrapperView.findViewById(R.id.tv_title);
        mTipTv = (TextView) wrapperView.findViewById(R.id.tv_tip);
        mBtn = (Button) wrapperView.findViewById(R.id.btn_save);
        mLeftBtn = (Button) wrapperView.findViewById(R.id.btn_left);
        mRightBtn = (Button) wrapperView.findViewById(R.id.btn_right);
        mLLTwoBtn = (LinearLayout) wrapperView.findViewById(R.id.ll_two_btn);

        if(mDescEt != null) {
            mDescEt.addTextChangedListener(this);
        }
        if(mBtn != null) {
            mBtn.setOnClickListener(this);
        }
        if(mLeftBtn != null) {
            mLeftBtn.setOnClickListener(this);
        }
        if(mRightBtn != null) {
            mRightBtn.setOnClickListener(this);
        }
        setInputNumber(mMaxNumber);
        return wrapperView;
    }

    public int getContentViewLayoutId() {
        return R.layout.fm_bottom_sheet_input;
    }

    public void buildViews(View wrapperView){

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        temp = s;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int number = s.length();
        selectionStart = mDescEt.getSelectionStart();
        selectionEnd = mDescEt.getSelectionEnd();
        if (temp.length() > mMaxNumber) {
            s.delete(selectionStart - 1, selectionEnd);
            int tempSelection = selectionStart;
            mDescEt.setText(s);
            mDescEt.setSelection(tempSelection);
        }
        setInputNumber(mMaxNumber - number < 0 ? 0 : mMaxNumber - number);
    }

    private FMBottomInputSheetBuilder setInputNumber(int value) {
        mCountTv.setText(String.format(Locale.getDefault(), mContext.getString(R.string.arch_char_no_more), DataUtils.getNumberValue(value)));
        return this;
    }

    public FMBottomInputSheetBuilder setTitle(String title) {
        if(mTitle != null) {
            mTitle.setText(title);
        }
        return this;
    }

    public FMBottomInputSheetBuilder setTitle(@StringRes int title) {
        setTitle(mContext.getResources().getString(title));
        return this;
    }

    public FMBottomInputSheetBuilder setTip(String tip) {
        if(mTipTv != null) {
            mTipTv.setText(tip);
        }
        return this;
    }

    public FMBottomInputSheetBuilder setTip(@StringRes int tip) {
        setTip(mContext.getResources().getString(tip));
        return this;
    }

    public FMBottomInputSheetBuilder setShowTip(boolean showTip){
        if(mTipTv != null) {
            mTipTv.setVisibility(showTip ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public FMBottomInputSheetBuilder setDesc(String desc) {
        mDescEt.setText(desc);
        return this;
    }

    public FMBottomInputSheetBuilder setDesc(@StringRes int desc) {
        setDesc(mContext.getResources().getString(desc));
        return this;
    }

    public FMBottomInputSheetBuilder setDescHint(String desc) {
        mDescEt.setHint(desc);
        return this;
    }

    public FMBottomInputSheetBuilder setDescHint(@StringRes int desc) {
        setDescHint(mContext.getResources().getString(desc));
        return this;
    }

    public FMBottomInputSheetBuilder setBtnText(String btn) {
        if(mBtn != null) {
            mBtn.setText(btn);
        }
        return this;
    }

    public FMBottomInputSheetBuilder setBtnText(@StringRes int btn) {
        setBtnText(mContext.getResources().getString(btn));
        return this;
    }

    public FMBottomInputSheetBuilder setLeftBtnText(String btn) {
        if(mLeftBtn != null) {
            mLeftBtn.setText(btn);
        }
        return this;
    }

    public FMBottomInputSheetBuilder setLeftBtnText(@StringRes int btn) {
        setLeftBtnText(mContext.getResources().getString(btn));
        return this;
    }

    public FMBottomInputSheetBuilder setRightBtnText(String btn) {
        if(mRightBtn != null) {
            mRightBtn.setText(btn);
        }
        return this;
    }

    public FMBottomInputSheetBuilder setRightBtnText(@StringRes int btn) {
        setRightBtnText(mContext.getResources().getString(btn));
        return this;
    }

    public FMBottomInputSheetBuilder setSingleNeedInput(boolean singleNeedInput) {
        this.singleNeedInput = singleNeedInput;
        return this;
    }

    public FMBottomInputSheetBuilder setTwoBtnLeftInput(boolean twoNeedInput) {
        this.twoNeedLeftInput = twoNeedInput;
        return this;
    }

    public FMBottomInputSheetBuilder setTwoBtnRightInput(boolean twoNeedInput) {
        this.twoNeedRightInput = twoNeedInput;
        return this;
    }

    public FMBottomInputSheetBuilder setSingleBtnBg(@DrawableRes int resID) {
        getSingleBtn().setBackgroundResource(resID);
        return this;
    }

    public EditText getDescEt() {
        return mDescEt;
    }

    public TextView getTitle() {
        return mTitle;
    }

    public Button getSingleBtn() {
        return mBtn;
    }

    public Button getLeftBtn() {
        return mLeftBtn;
    }

    public Button getRightBtn() {
        return mRightBtn;
    }

    public LinearLayout getLLTwoBtn() {
        return mLLTwoBtn;
    }

    public void setShowTip(String showTip) {
        this.showTip = showTip;
    }

    public FMBottomInputSheetBuilder setMaxNumber(int maxNumber) {
        this.mMaxNumber = maxNumber;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (mOnSaveInputListener != null && mDescEt != null) {
            String input = mDescEt.getText().toString();
            if (v.getId() == R.id.btn_save) {
                if (singleNeedInput && TextUtils.isEmpty(input)) {
                    ToastUtils.showShort(showTip);
                    return;
                }
                mOnSaveInputListener.onSaveClick(mDialog, input);

            } else if (v.getId() == R.id.btn_left) {
                if (twoNeedLeftInput && TextUtils.isEmpty(input)) {
                    ToastUtils.showShort(showTip);
                    return;
                }
                mOnSaveInputListener.onLeftClick(mDialog, input);
            } else if (v.getId() == R.id.btn_right) {
                if (twoNeedRightInput && TextUtils.isEmpty(input)) {
                    ToastUtils.showShort(showTip);
                    return;
                }
                mOnSaveInputListener.onRightClick(mDialog, input);
            }
        }
    }


    private OnInputBtnClickListener mOnSaveInputListener;

    public void setOnSaveInputListener(OnInputBtnClickListener onSaveInputListener) {
        mOnSaveInputListener = onSaveInputListener;
    }

    public interface OnInputBtnClickListener {
        void onSaveClick(QMUIBottomSheet dialog, String input);

        void onLeftClick(QMUIBottomSheet dialog, String input);

        void onRightClick(QMUIBottomSheet dialog, String input);
    }
}
