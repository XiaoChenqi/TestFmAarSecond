package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.basiclib.utils.DataUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;

import java.util.Locale;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:带有基数的EditText
 * Date: 2018/7/5 下午3:00
 */
public class EditNumberView extends LinearLayout implements TextWatcher {
    private int mMaxNumber  = 200;//最大数字

    private Context mContext;
    private EditText mDescEt;
    private TextView mCountTv;
    private CharSequence temp;
    private int selectionStart;
    private int selectionEnd;

    public EditNumberView(Context context) {
        this(context, null);
    }

    public EditNumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.EditNumberViewStyle);
    }

    public EditNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        if (isInEditMode()) {
            return;
        }

        LayoutInflater.from(context).inflate(R.layout.fm_edit_number_text_view, this, true);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditNumberView, defStyleAttr, 0);
        String desc = array.getString(R.styleable.EditNumberView_fm_edit_number_desc);
        mMaxNumber  = array.getInteger(R.styleable.EditNumberView_fm_edit_number_count, mMaxNumber);

        mDescEt = (EditText) findViewById(R.id.et_desc);
        mCountTv = (TextView) findViewById(R.id.tv_count);

        if (!TextUtils.isEmpty(desc)) {
            mDescEt.setHint(desc);
        }
        setInputNumber(mMaxNumber);
        mDescEt.addTextChangedListener(this);
    }

    public EditText getDescEt() {
        return mDescEt;
    }

    public String getDesc() {
        if (mDescEt != null) {
            return mDescEt.getText().toString();
        }
        return null;
    }

    public void setDesc(String desc) {
        if (mDescEt != null) {
            mDescEt.setText(StringUtils.formatString(desc));
        }
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
        setInputRemainingNumber(mMaxNumber - number < 0 ? 0 : mMaxNumber - number);

    }

    private void setInputRemainingNumber(int value) {
        mCountTv.setText(String.format(Locale.getDefault(), mContext.getString(R.string.arch_char_no_more_remaining), DataUtils.getNumberValue(value)));
    }

    private void setInputNumber(int value) {
        mCountTv.setText(String.format(Locale.getDefault(), mContext.getString(R.string.arch_char_no_more), DataUtils.getNumberValue(value)));
    }
}
