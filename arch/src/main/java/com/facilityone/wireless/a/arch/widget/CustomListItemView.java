package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.facilityone.wireless.a.arch.R;


/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/6/4 上午11:21
 */
public class CustomListItemView extends LinearLayout {

    private ImageView mIcon;
    private ImageView mTipIcon;
    private ImageView mTipRedIcon;
    private TextView mTitle;
    private TextView mTipTxt;
    private View mLineView;

    public CustomListItemView(Context context) {
        this(context, null);
    }

    public CustomListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.CommonListItemViewStyle);
    }

    public CustomListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (isInEditMode()) {
            return;
        }
        LayoutInflater.from(context).inflate(R.layout.mine_custom_list_item, this, true);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CommonListItemView, defStyleAttr, 0);
        boolean showTip = array.getBoolean(R.styleable.CommonListItemView_fm_list_item_show_tip, false);
        boolean showLine = array.getBoolean(R.styleable.CommonListItemView_fm_list_item_show_line, false);
        String title = array.getString(R.styleable.CommonListItemView_fm_list_item_title);
        String tip = array.getString(R.styleable.CommonListItemView_fm_list_item_tip);
        Drawable drawable = array.getDrawable(R.styleable.CommonListItemView_fm_list_item_image);
        boolean showRed = array.getBoolean(R.styleable.CommonListItemView_fm_list_item_show_red_icon, false);
        boolean showRight = array.getBoolean(R.styleable.CommonListItemView_fm_list_item_show_right_icon, false);
        int tipColor = array.getColor(R.styleable.CommonListItemView_fm_list_item_tip_color, ContextCompat.getColor(context,R.color.grey_6));

        mIcon = (ImageView) findViewById(R.id.icon);
        mTipIcon = (ImageView) findViewById(R.id.tip_icon);
        mTipRedIcon = (ImageView) findViewById(R.id.tip_red_icon);
        mTitle = (TextView) findViewById(R.id.title);
        mTipTxt = (TextView) findViewById(R.id.tip);
        mLineView = findViewById(R.id.line);

        mTipTxt.setVisibility(showTip ? VISIBLE : GONE);
        mTipTxt.setTextColor(tipColor);
        mLineView.setVisibility(showLine ? VISIBLE : GONE);
        mTipTxt.setText(tip == null ? "" : tip);
        mTitle.setText(title == null ? "" : title);
        if (drawable != null) {
            mIcon.setImageDrawable(drawable);
        }

        if (showRed) {
            ViewGroup.LayoutParams lp = mTipRedIcon.getLayoutParams();
            lp.height = SizeUtils.dp2px(7);
            lp.width = SizeUtils.dp2px(7);
            mTipRedIcon.setLayoutParams(lp);
            Drawable bg = getResources().getDrawable(R.drawable.circle_red_bg);
            mTipRedIcon.setBackgroundDrawable(bg);
        }
        mTipIcon.setVisibility(showRight ? VISIBLE : INVISIBLE);
    }

    public void showRedPoint(boolean show) {
        mTipRedIcon.setVisibility(show ? VISIBLE : GONE);
    }

    public void setTitle(String title) {
        mTitle.setText(title == null ? "" : title);
    }

    public void setTip(String tip) {
        mTipTxt.setText(tip == null ? "" : tip);
    }

    public void setTipTextColor(@ColorInt int color) {
        if(mTipTxt != null) {
            mTipTxt.setTextColor(color);
        }
    }
}
