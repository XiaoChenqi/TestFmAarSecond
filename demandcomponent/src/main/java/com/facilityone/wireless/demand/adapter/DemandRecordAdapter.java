package com.facilityone.wireless.demand.adapter;

import android.support.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.widget.TriMouthView;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.SystemDateUtils;
import com.facilityone.wireless.demand.R;
import com.facilityone.wireless.demand.module.DemandConstant;
import com.facilityone.wireless.demand.module.DemandService;

import java.util.Date;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/1 上午1:04
 */
public class DemandRecordAdapter extends BaseQuickAdapter<DemandService.RecordsBean, BaseViewHolder> {

    public DemandRecordAdapter(@Nullable List<DemandService.RecordsBean> data) {
        super(R.layout.view_time_line, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DemandService.RecordsBean item) {
        if (item != null) {
            TriMouthView mouthView = helper.getView(R.id.service_demand_detail_record_content_tv);
            mouthView.getmContentIv().setText(item.handler != null ? item.handler
                    + "\n" + (item.content != null ? (item.content.contains("<br/>") ?
                    item.content.replaceAll("<br/>", "\n") : item.content) : "")
                    : "");
            item.date = item.date == null ? SystemDateUtils.getCurrentTimeMillis() : item.date;
            helper.setText(R.id.service_demand_detail_record_date_tv, TimeUtils.date2String(new Date(item.date), DateUtils.SIMPLE_DATE_FORMAT));
            if (item.recordType != null) {
                helper.setVisible(R.id.service_demand_detail_record_top_line, true);
                helper.setVisible(R.id.service_demand_detail_record_bottom_line, true);
                int idDrawable = R.drawable.service_control_label_create_demand;
                switch (item.recordType) {
                    case DemandConstant.DEMAND_DETAIL_RECORD_DEMAND_CREATED:
                        idDrawable = R.drawable.service_control_label_create_demand;
                        helper.setVisible(R.id.service_demand_detail_record_top_line, false);
                        break;
                    case DemandConstant.DEMAND_DETAIL_RECORD_DEMAND_APPROVAL:
                        idDrawable = R.drawable.service_control_label_approval;
                        break;
                    case DemandConstant.DEMAND_DETAIL_RECORD_WORKORDER_CREATE:
                        idDrawable = R.drawable.service_control_label_create_work_order;
                        break;
                    case DemandConstant.DEMAND_DETAIL_RECORD_DEMAND_PROGRESS:
                        idDrawable = R.drawable.service_control_label_progress;
                        break;
                    case DemandConstant.DEMAND_DETAIL_RECORD_DEMAND_EVALUATED:
                        idDrawable = R.drawable.service_control_label_evaluated;
                        break;
                    case DemandConstant.DEMAND_DETAIL_RECORD_DEMAND_COMPLETED:
                        idDrawable = R.drawable.service_control_label_completed;
                        helper.setVisible(R.id.service_demand_detail_record_bottom_line, false);
                        break;
                }
                helper.setBackgroundRes(R.id.service_demand_detail_priority_view, idDrawable);
            }
        }
    }
}
