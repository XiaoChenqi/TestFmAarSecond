package com.facilityone.wireless.demand.adapter;

import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.demand.R;
import com.facilityone.wireless.demand.module.DemandConstant;
import com.facilityone.wireless.demand.module.DemandHelper;
import com.facilityone.wireless.demand.module.DemandService;

import java.util.Date;

/**
 * Created by: owen.
 * Date: on 2018/6/22 上午11:57.
 * Description: 需求列表通用适配器
 * email:
 */

public class DemandListAdapter extends BaseQuickAdapter<DemandService.DemandBean, BaseViewHolder> {

    private final int type;

    public DemandListAdapter(int type) {
        super(R.layout.item_demand);
        this.type = type;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DemandService.DemandBean item) {

        if (item != null) {
            helper.setGone(R.id.ll_quick_show, type != DemandConstant.DEMAND_REQUES_QUERY);
            helper.setGone(R.id.bottom_line_view, type != DemandConstant.DEMAND_REQUES_QUERY);
            int layoutPosition = helper.getLayoutPosition();
            if (type == DemandConstant.DEMAND_REQUES_QUERY && layoutPosition == getData().size() - 1) {
                helper.setGone(R.id.bottom_line_view, true);
                helper.setGone(R.id.center_line_view, false);
            } else {
                helper.setGone(R.id.center_line_view, true);
            }
            helper.setGone(R.id.view_placeholder, type != DemandConstant.DEMAND_REQUES_QUERY);
            helper.setText(R.id.demand_code_tv, TextUtils.isEmpty(item.code) ? "" : item.code);
            helper.setText(R.id.demand_describe_tv, TextUtils.isEmpty(item.desc) ? "" : item.desc);
            if (item.createDate != null) {
                String date2String = TimeUtils.date2String(new Date(item.createDate), DateUtils.SIMPLE_DATE_FORMAT_ALL);
                helper.setText(R.id.demand_date_tv, date2String);
            }

            if (item.origin != null) {
                helper.setVisible(R.id.demand_origin_tv, true);
                switch (item.origin) {
                    case DemandConstant.DEMAND_ORIGIN_WEB:
                        helper.setBackgroundRes(R.id.demand_origin_tv, R.drawable.service_control_orgin_webside);
                        break;
                    case DemandConstant.DEMAND_ORIGIN_APP:
                        helper.setBackgroundRes(R.id.demand_origin_tv, R.drawable.service_control_orgin_smartphone);
                        break;
                    case DemandConstant.DEMAND_ORIGIN_WECHAT:
                        helper.setBackgroundRes(R.id.demand_origin_tv, R.drawable.service_control_orgin_webchart);
                        break;
                    case DemandConstant.DEMAND_ORIGIN_EMAIL:
                        helper.setBackgroundRes(R.id.demand_origin_tv, R.drawable.service_control_orgin_email);
                        break;
                        case DemandConstant.DEMAND_ORIGIN_DINGDING:
                        //TODO    xcq
                        helper.setBackgroundRes(R.id.demand_origin_tv, R.drawable.service_control_orgin_email);
                        //helper.setBackgroundRes(R.id.demand_origin_tv, R.drawable.service_control_origin_dingding);
                        break;
                }
            } else {
                helper.setVisible(R.id.demand_origin_tv, false);
            }


            if (item.status != null) {
                helper.setVisible(R.id.demand_status_tv, true);
                helper.setText(R.id.demand_status_tv, DemandHelper.getDemandStatusMap(mContext).get(item.status));
                switch (item.status) {
                    case DemandConstant.DEMAND_STATUS_CREATED:
                        helper.setBackgroundRes(R.id.demand_status_tv, R.drawable.demand_fill_created_bg);
                        break;
                    case DemandConstant.DEMAND_STATUS_PROGRESS:
                        helper.setBackgroundRes(R.id.demand_status_tv, R.drawable.demand_fill_progress_bg);
                        break;
                    case DemandConstant.DEMAND_STATUS_COMPLETED:
                        helper.setBackgroundRes(R.id.demand_status_tv, R.drawable.demand_fill_completed_bg);
                        break;
                    case DemandConstant.DEMAND_STATUS_EVALUATED:
                        helper.setBackgroundRes(R.id.demand_status_tv, R.drawable.demand_fill_evaluated_bg);
                        break;
                    case DemandConstant.DEMAND_STATUS_CANCEL:
                        helper.setBackgroundRes(R.id.demand_status_tv, R.drawable.demand_fill_cancel_bg);
                        break;
                }
            } else {
                helper.setVisible(R.id.demand_status_tv, false);
            }

            helper.setText(R.id.demand_type_tv, StringUtils.formatString(item.type));
            if(item.orders != null && item.orders.size() > 0) {
                helper.setGone(R.id.demand_related_order_ll,true);
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < item.orders.size(); i++) {
                    DemandService.RelatedOrder order = item.orders.get(i);
                    String status = "";
                    if(order.status != null) {
                        status = DemandHelper.getWorkOrderStatusMap(mContext).get(order.status);
                    }
                    if(i != 0) {
                        buffer.append("、");
                    }
                    buffer.append(StringUtils.formatString(order.code));
                    if(!TextUtils.isEmpty(status)) {
                        buffer.append("(");
                        buffer.append(status);
                        buffer.append(")");
                    }
                }
                helper.setText(R.id.demand_related_order_tv, buffer.toString());
            }else {
                helper.setGone(R.id.demand_related_order_ll,false);
            }
        }
    }
}
