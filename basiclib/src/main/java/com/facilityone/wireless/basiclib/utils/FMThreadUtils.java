package com.facilityone.wireless.basiclib.utils;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description: 构造一个固定线程数目的线程池
 * Date: 2018/4/4 下午4:23
 */
public class FMThreadUtils {
    private static final int MAX_THREAD_COUNT = 5;//最大线程数

    private FMThreadUtils() {
    }

    private static class Holder {
        private static final ThreadPoolUtils INSTANCE =
                new ThreadPoolUtils(ThreadPoolUtils.FixedThread, MAX_THREAD_COUNT);
    }

    public static ThreadPoolUtils getInstance() {
        return Holder.INSTANCE;
    }

}
