package com.facilityone.wireless.a.arch.mvp;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:Presenter需呀实现的接口传递View
 * Date: 2018/3/13 下午4:50
 */
public interface IPresent<V> {

    /**绑定view*/
    void attachV(V view);

    /**解绑view*/
    void detachV();
}
