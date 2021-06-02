package com.facilityone.wireless.basiclib.utils.callback;

import android.support.annotation.Nullable;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:全局监听返回
 * Date: 2018/3/19 下午12:22
 */
public interface IGlobalCallback<T> {
    void executeCallback(@Nullable  T args);
}

