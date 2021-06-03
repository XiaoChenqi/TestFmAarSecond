package com.facilityone.wireless.demand.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.ec.module.OrdersBean;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.demand.R;
import com.facilityone.wireless.demand.module.DemandHelper;

import java.util.List;

/**
 * Created by peter.peng on 2019/6/20.
 */

public class DemandRelatedOrderAdapter extends BaseQuickAdapter<OrdersBean,BaseViewHolder> {
    public DemandRelatedOrderAdapter(@Nullable List<OrdersBean> data) {
        super(R.layout.item_related_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrdersBean item) {
        if(item != null) {
            StringBuffer buffer = new StringBuffer();
            String status = "";
            if(item.status != null) {
                status = DemandHelper.getWorkOrderStatusMap(mContext).get(item.status);
            }
            buffer.append(StringUtils.formatString(item.code));
            if(!TextUtils.isEmpty(status)) {
                buffer.append("(");
                buffer.append(status);
                buffer.append(")");
            }
            helper.setText(R.id.related_order_item_code_tv, buffer.toString());
            helper.setGone(R.id.related_order_item_dash_line, helper.getLayoutPosition() != getData().size() - 1);
        }
    }
}
