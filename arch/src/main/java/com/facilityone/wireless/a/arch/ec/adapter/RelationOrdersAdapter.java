package com.facilityone.wireless.a.arch.ec.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.ec.module.OrdersBean;
import com.facilityone.wireless.basiclib.utils.StringUtils;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/6/30 下午7:30
 */
public class RelationOrdersAdapter extends BaseQuickAdapter<OrdersBean, BaseViewHolder> {

    public RelationOrdersAdapter(@Nullable List<OrdersBean> data) {
        super(R.layout.item_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrdersBean item) {
        helper.setText(R.id.tv_order_id, StringUtils.formatString(item.code));
        helper.setGone(R.id.dash_line, helper.getLayoutPosition() != getData().size() - 1);
    }
}
