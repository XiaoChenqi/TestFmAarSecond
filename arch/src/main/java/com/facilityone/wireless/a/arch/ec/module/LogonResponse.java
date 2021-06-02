package com.facilityone.wireless.a.arch.ec.module;

import android.support.annotation.Keep;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/5/28 下午2:47
 */
@Keep
public class LogonResponse {
    public Long userId;
    public Integer userType;

    @Override
    public String toString() {
        return "LogonResponse{" +
                "userId=" + userId +
                ", userType=" + userType +
                '}';
    }
}
