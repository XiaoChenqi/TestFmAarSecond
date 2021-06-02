package com.facilityone.wireless.basiclib.utils.callback;

import java.util.WeakHashMap;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:回调管理类
 * Date: 2018/3/19 下午12:13
 */
public class CallbackManager {

    private static final WeakHashMap<Object, IGlobalCallback> CALLBACKS = new WeakHashMap<>();

    private static final class Holder {
        private static final CallbackManager INSTANCE = new CallbackManager();
    }

    public static CallbackManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 清空所有回调
     */
    protected void clearICallback() {
        CALLBACKS.clear();
    }

    /**
     * 添加全局回调
     *
     * @param tag
     * @param callback
     * @return
     */
    public CallbackManager addCallback(Object tag, IGlobalCallback callback) {
        CALLBACKS.put(tag, callback);
        return this;
    }

    /**
     * 获取回调
     * @param tag
     * @return
     */
    public IGlobalCallback getCallback(Object tag) {
        return CALLBACKS.get(tag);
    }


    private CallbackManager() {
    }
}
