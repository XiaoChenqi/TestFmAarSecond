package com.facilityone.wireless.a.arch.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.facilityone.wireless.a.arch.R;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:请出网格带加载更多功能
 * Date: 2018/6/26 下午5:16
 */
public class FMLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.fm_load_more_view;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.loading;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.fail;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.end;
    }
}
