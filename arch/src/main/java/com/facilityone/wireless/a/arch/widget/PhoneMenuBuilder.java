package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facilityone.wireless.a.arch.R;
import com.qmuiteam.qmui.widget.QMUIWrapContentScrollView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;

import java.util.ArrayList;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/6/30 下午9:19
 */
public class PhoneMenuBuilder extends QMUIDialogBuilder {

    protected LinearLayout mMenuItemContainer;
    protected ArrayList<View> mMenuItemViews;
    protected QMUIWrapContentScrollView mContentScrollView;
    protected LinearLayout.LayoutParams mMenuItemLp;

    public PhoneMenuBuilder(Context context) {
        super(context);
        mMenuItemViews = new ArrayList<>();
    }

    public void clear() {
        mMenuItemViews.clear();
    }

    public PhoneMenuBuilder addItems(CharSequence[] items, final DialogInterface.OnClickListener listener) {
        for (int i = 0; i < items.length; i++) {
            CharSequence item = items[i];
            LayoutInflater inflater = LayoutInflater.from(getBaseContext());
            View view = inflater.inflate(R.layout.dialog_phone, null);
            TextView tvTel = (TextView) view.findViewById(R.id.tv_tel);
            tvTel.setText(item);
            View line = view.findViewById(R.id.phone_line);
            if(i == item.length() - 1) {
                line.setVisibility(View.GONE);
            }else {
                line.setVisibility(View.VISIBLE);
            }
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(mDialog, finalI);
                    }
                }
            });
            mMenuItemViews.add(view);
        }
        return this;
    }

    @Override
    protected void onCreateContent(QMUIDialog dialog, ViewGroup parent, Context context) {

        mMenuItemContainer = new LinearLayout(context);
        mMenuItemContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mMenuItemContainer.setLayoutParams(layoutParams);

        mMenuItemLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMenuItemLp.gravity = Gravity.CENTER_VERTICAL;

        for (View menuItemView : mMenuItemViews) {
            mMenuItemContainer.addView(menuItemView, mMenuItemLp);
        }

        mContentScrollView = new QMUIWrapContentScrollView(context);
        mContentScrollView.setMaxHeight(getContentAreaMaxHeight());
        mContentScrollView.addView(mMenuItemContainer);
        mContentScrollView.setVerticalScrollBarEnabled(false);
        parent.addView(mContentScrollView);
    }
}
