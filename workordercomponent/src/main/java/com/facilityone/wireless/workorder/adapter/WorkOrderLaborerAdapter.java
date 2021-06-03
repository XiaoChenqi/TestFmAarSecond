package com.facilityone.wireless.workorder.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.utils.NoDoubleClickListener;
import com.facilityone.wireless.a.arch.widget.PhoneMenuBuilder;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderHelper;
import com.facilityone.wireless.workorder.module.WorkorderLaborerService;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/13 下午12:24
 */
public class WorkOrderLaborerAdapter extends BaseQuickAdapter<WorkorderLaborerService.WorkorderLaborerBean, BaseViewHolder> {

    public Context mContext;

    public WorkOrderLaborerAdapter(@Nullable List<WorkorderLaborerService.WorkorderLaborerBean> data, Context context) {
        super(R.layout.adapter_workorder_laborer_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final WorkorderLaborerService.WorkorderLaborerBean item) {
        if (item.canOpt) {
            helper.setGone(R.id.ex_right, true);
            helper.setBackgroundRes(R.id.work_order_person_ll, R.drawable.list_item_bg);
        } else {
            helper.setGone(R.id.ex_right, false);
            helper.setBackgroundColor(R.id.work_order_person_ll, mContext.getResources().getColor(R.color.white));
        }

        int layoutPosition = helper.getLayoutPosition();
        helper.setGone(R.id.view_ex_dash, layoutPosition != getData().size() - 1);

        helper.setText(R.id.work_order_person_name_tv, StringUtils.formatString(item.laborer));
        final String phone = item.phone;
        if (!TextUtils.isEmpty(phone)) {
            helper.getView(R.id.work_order_person_phone_tv).setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    final String[] split = phone.split("/");
                    PhoneMenuBuilder builder = new PhoneMenuBuilder(mContext);
                    builder.addItems(split, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PhoneUtils.dial(split[which]);
                            dialog.dismiss();
                        }
                    });
                    builder.create(R.style.fmDefaultDialog).show();
                }
            });
            helper.setGone(R.id.work_order_person_phone_tv, true);
        } else {
            helper.setGone(R.id.work_order_person_phone_tv, false);
        }
        helper.setGone(R.id.work_order_person_responsible_tv, item.responsible == null ? false : item.responsible);
        if (item.status != null) {
            String value = WorkorderHelper.getLaborerStatusMap(mContext).get(item.status);
            helper.setText(R.id.work_order_person_finish_status_tv, value);
            switch (item.status) {
                case WorkorderConstant.WORKORDER_STATUS_PERSONAL_UN_ACCEPT:
                    helper.setTextColor(R.id.work_order_person_finish_status_tv, mContext.getResources().getColor(R.color.workorder_red));
                    break;
                case WorkorderConstant.WORKORDER_STATUS_PERSONAL_ACCEPT:
                    helper.setTextColor(R.id.work_order_person_finish_status_tv, mContext.getResources().getColor(R.color.workorder_green));
                    break;
                case WorkorderConstant.WORKORDER_STATUS_PERSONAL_BACK:
                    helper.setTextColor(R.id.work_order_person_finish_status_tv, mContext.getResources().getColor(R.color.workorder_orange));
                    break;
                case WorkorderConstant.WORKORDER_STATUS_PERSONAL_SUBMIT:
                    helper.setTextColor(R.id.work_order_person_finish_status_tv, mContext.getResources().getColor(R.color.workorder_blue));
                    break;
            }
            helper.setGone(R.id.work_order_person_finish_status_tv, true);
        } else {
            helper.setGone(R.id.work_order_person_finish_status_tv, false);
        }

        String actualArrivalDateTime = "";
        String actualCompletionDateTime = "";
        if (item.actualArrivalDateTime != null) {
            actualArrivalDateTime = TimeUtils.millis2String(item.actualArrivalDateTime, DateUtils.SIMPLE_DATE_FORMAT_ALL);
        }
        if (item.actualCompletionDateTime != null) {
            actualCompletionDateTime = TimeUtils.millis2String(item.actualCompletionDateTime, DateUtils.SIMPLE_DATE_FORMAT_ALL);
        }

        StringBuffer sb = new StringBuffer();

        if (!TextUtils.isEmpty(actualArrivalDateTime) && !TextUtils.isEmpty(actualCompletionDateTime)) {
            sb.append(String.format("%s - %s", actualArrivalDateTime, actualCompletionDateTime));
        } else {
            if (!TextUtils.isEmpty(actualArrivalDateTime)) {
                sb.append(String.format("%s -", actualArrivalDateTime));
            }

            if (!TextUtils.isEmpty(actualCompletionDateTime)) {
                sb.append(String.format("- %s", actualCompletionDateTime));
            }
        }
        helper.setText(R.id.work_order_person_work_time_tv, sb.toString());
    }
}
