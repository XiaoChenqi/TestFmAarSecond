package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facilityone.wireless.a.arch.R;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/6/22 下午3:25
 */
public class CustomContentItemView extends LinearLayout implements View.OnClickListener {
    private TextView mTitleTv;
    private TextView mRedTv;
    private TextView mTipTv;
    private EditText mInputEt;
    private ImageView mIconIv;
    private ImageView mClearIconIv;
    private View mDashLine;
    private View mLine;

    public CustomContentItemView(Context context) {
        this(context, null);
    }

    public CustomContentItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.CommonContentItemViewStyle);
    }

    public CustomContentItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (isInEditMode()) {
            return;
        }
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.custom_content_item, this, true);
        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mRedTv = (TextView) findViewById(R.id.tv_red);
        mTipTv = (TextView) findViewById(R.id.tv_tip);
        mInputEt = (EditText) findViewById(R.id.et_input);
        mIconIv = (ImageView) findViewById(R.id.tip_icon);
        mClearIconIv = (ImageView) findViewById(R.id.clear_icon_iv);
        mDashLine = findViewById(R.id.dash_line);
        mLine = findViewById(R.id.line);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CommonContentItemView, defStyleAttr, 0);

        boolean showTip = array.getBoolean(R.styleable.CommonContentItemView_fm_content_item_show_tip, false);
        boolean showLine = array.getBoolean(R.styleable.CommonContentItemView_fm_content_item_show_line, false);
        boolean showRed = array.getBoolean(R.styleable.CommonContentItemView_fm_content_item_show_red, false);
        boolean showIcon = array.getBoolean(R.styleable.CommonContentItemView_fm_content_item_show_tip_icon, false);
        String title = array.getString(R.styleable.CommonContentItemView_fm_content_item_title);
        String tip = array.getString(R.styleable.CommonContentItemView_fm_content_item_tip);
        String hint = array.getString(R.styleable.CommonContentItemView_fm_content_item_et_hint);
        String input = array.getString(R.styleable.CommonContentItemView_fm_content_item_et_content);
        Drawable drawable = array.getDrawable(R.styleable.CommonContentItemView_fm_content_item_tip_icon);

        mRedTv.setVisibility(showRed ? VISIBLE : GONE);
        mDashLine.setVisibility(!showLine ? VISIBLE : GONE);
        mLine.setVisibility(showLine ? VISIBLE : GONE);
        mInputEt.setVisibility(!showTip ? VISIBLE : GONE);
        mTipTv.setVisibility(showTip ? VISIBLE : GONE);
        mIconIv.setVisibility(showIcon ? VISIBLE : INVISIBLE);
        mTitleTv.setText(title);
        mTipTv.setText(tip);
        if(drawable != null) {
            mIconIv.setBackground(drawable);
        }
        if (hint != null) {
            mInputEt.setHint(hint);
            mTipTv.setHint(hint);
        }
        if (input != null) {
            mInputEt.setText(input);
        }
        mClearIconIv.setOnClickListener(this);
    }

    public EditText getInputEt() {
        return mInputEt;
    }

    public String getInputText() {
        if (mInputEt != null) {
            return mInputEt.getText().toString();
        }
        return "";
    }

    public void setInputText(String input) {
        if (mInputEt != null && input != null) {
            mInputEt.setText(input);
        }
    }

    public TextView getTipTv() {
        return mTipTv;
    }

    public TextView getTitleTv() {
        return mTitleTv;
    }

    public String getTipText() {
        if (mTipTv != null) {
            return mTipTv.getText().toString();
        }
        return "";
    }

    public void setTipText(String tip) {
        if (mTipTv != null && tip != null) {
            mTipTv.setText(tip);
        }
    }

    public void setTipColor(@ColorRes int resID) {
        if (mTipTv != null) {
            mTipTv.setTextColor(getResources().getColor(resID));
        }
    }

    public void setInputColor(@ColorRes int resID) {
        if (mInputEt != null) {
            mInputEt.setTextColor(getResources().getColor(resID));
        }
    }

    public void showDashLine(boolean show) {
        mLine.setVisibility(show ? GONE : VISIBLE);
        mDashLine.setVisibility(show ? VISIBLE : GONE);
    }

    public void showRed(boolean show){
        if(mRedTv != null) {
            mRedTv.setVisibility(show ? VISIBLE : GONE);
        }
    }

    public void showClearIocn(boolean showClear){
        if(mClearIconIv != null) {
            mClearIconIv.setVisibility(showClear ? VISIBLE : GONE);
        }
        if(mIconIv != null) {
            mIconIv.setVisibility(showClear ? GONE : VISIBLE);
        }
    }

    public void showIcon(boolean showIcon){
        if(mIconIv != null) {
            mIconIv.setVisibility(showIcon ? VISIBLE : INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.clear_icon_iv) {
            mTipTv.setText("");
            showClearIocn(false);
        }
    }
}
