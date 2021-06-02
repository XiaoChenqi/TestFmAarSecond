package com.facilityone.wireless.a.arch.ec.utils;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:RecyclerView工具
 * Date: 2018/10/25 4:18 PM
 */
public class RecyclerViewUtil {

    /**
     * 设置recyclerView高度 防止空页面上移
     *
     * @param match
     * @param recyclerView
     */
    public static void setHeightMatchParent(boolean match, RecyclerView recyclerView) {
        ViewGroup.LayoutParams lp = recyclerView.getLayoutParams();
        lp.height = match ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT;
        recyclerView.setLayoutParams(lp);
    }
}
