package com.facilityone.wireless.a.arch.utils;

import android.view.View;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:防止多次点击
 * Date: 2018/3/21 上午10:45
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {

    //在次可以点击的时间间隔
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View view) {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastClickTime > MIN_CLICK_DELAY_TIME){
            lastClickTime = currentTime;
            onNoDoubleClick(view);
        }
    }

    protected abstract void onNoDoubleClick(View view);
}
