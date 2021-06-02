package com.facilityone.wireless.basiclib.network.callback.check;

/**
 * Author：gary
 * <p/>
 * Email: xuhaozv@163.com
 * <p/>
 * description:观察者 观察网络是否连接
 * <p/>
 * Date: 2020-07-27 16:16
 */
public interface NetStateChangeObserver {
    void onNetDisconnected();

    void onNetConnected();
}

