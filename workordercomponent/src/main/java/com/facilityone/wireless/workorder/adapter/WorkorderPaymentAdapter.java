package com.facilityone.wireless.workorder.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderHelper;
import com.facilityone.wireless.workorder.module.WorkorderService;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by: owen.
 * Date: on 2018/12/11 下午4:48.
 * Description:
 * email:
 */

public class WorkorderPaymentAdapter extends BaseQuickAdapter<WorkorderService.PaymentsBean, BaseViewHolder> {

    public WorkorderPaymentAdapter(@Nullable List<WorkorderService.PaymentsBean> data) {
        super(R.layout.item_workorder_payment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkorderService.PaymentsBean item) {
        helper.addOnClickListener(R.id.work_order_item_main_rl);
        int position = helper.getLayoutPosition();
        if (item != null) {
            helper.setText(R.id.work_order_code_tv, StringUtils.formatString(item.code));
            if (item.createDateTime != null) {
                Date date = new Date(item.createDateTime);
                helper.setText(R.id.work_order_time_tv, TimeUtils.date2String(date, DateUtils.SIMPLE_DATE_FORMAT_ALL));
            }
            if(!TextUtils.isEmpty(item.cost)) {
                double parseDouble = Double.parseDouble(item.cost);
                DecimalFormat decimalFormat = new DecimalFormat("##0.00");
                String format = decimalFormat.format(parseDouble);
                helper.setText(R.id.work_order_cost_tv, "¥ " + format);
            }else {
                helper.setText(R.id.work_order_cost_tv, "");
            }
            helper.setText(R.id.work_order_customer_tv, StringUtils.formatString(item.customer));
            helper.setText(R.id.work_order_position_tv, StringUtils.formatString(item.location));

            if (item.status != null) {
                helper.setGone(R.id.work_order_state_tv, true);
                helper.setText(R.id.work_order_state_tv, WorkorderHelper.getPaymentStatus(mContext).get(item.status));
                int resId = R.drawable.fm_workorder_tag_fill_orange_background;
                switch (item.status) {
                    case WorkorderConstant.WORKORDER_PAYMENT_UNPAY:
                        resId = R.drawable.fm_workorder_tag_fill_orange_background;
                        break;
                    case WorkorderConstant.WORKORDER_PAYMENT_PAIED:
                        resId = R.drawable.fm_workorder_tag_fill_green_background;
                        break;
                    case WorkorderConstant.WORKORDER_PAYMENT_INVOICE:
                        resId = R.drawable.fm_workorder_tag_fill_blue_background;
                        break;
                    case WorkorderConstant.WORKORDER_PAYMENT_CLOSE:
                        resId = R.drawable.workorder_fill_grey_background;
                        break;
                    case WorkorderConstant.WORKORDER_PAYMENT_INVALID:
                        resId = R.drawable.fm_workorder_tag_fill_red_background;
                        break;
                }
                helper.setBackgroundRes(R.id.work_order_state_tv, resId);
            } else {
                helper.setGone(R.id.work_order_state_tv, false);
            }
        }
        if (position == getData().size() - 1) {
            helper.setGone(R.id.bottom_solid_line, true);
            helper.setGone(R.id.bottom_dot_line, false);
        } else {
            helper.setGone(R.id.bottom_solid_line, false);
            helper.setGone(R.id.bottom_dot_line, true);
        }
    }
}
