package com.facilityone.wireless.a.arch.mvp;

import com.facilityone.wireless.basiclib.utils.GsonUtils;

import java.lang.ref.SoftReference;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:所有Presenter需要继承的基类，此类完成一些内部的初始化工作
 * Date: 2018/3/13 下午4:54
 */
public class BasePresenter<V extends IView> implements IPresent<V> {

    private SoftReference<V> v;

    //通过弱引用绑定view
    @Override
    public void attachV(V view) {
        v = new SoftReference<>(view);
    }

    @Override
    public void detachV() {
        if (v.get() != null) {
            v.clear();
        }
        v = null;
    }

    /**
     * 获取绑定的view
     *
     * @return
     */
    protected V getV() {
        if (v == null || v.get() == null) {
            throw new IllegalStateException("v can not be null");
        }
        return v.get();
    }

    protected String toJson(Object o) {
        try {
            return GsonUtils.toJson(o, false);
        } catch (Exception e) {
            return "";
        }
    }
}
