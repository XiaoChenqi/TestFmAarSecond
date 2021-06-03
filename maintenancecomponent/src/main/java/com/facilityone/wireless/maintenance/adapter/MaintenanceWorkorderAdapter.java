package com.facilityone.wireless.maintenance.adapter;

import android.support.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.maintenance.R;
import com.facilityone.wireless.maintenance.model.MaintenanceConstant;
import com.facilityone.wireless.maintenance.model.MaintenanceHelper;
import com.facilityone.wireless.maintenance.model.MaintenanceService;

import java.util.Date;
import java.util.List;

/**
 * Created by peter.peng on 2018/11/21.
 * 计划性维护详情维护工单适配器
 */

public class MaintenanceWorkorderAdapter extends BaseQuickAdapter<MaintenanceService.MaintenanceWorkOrder, BaseViewHolder> {

    public MaintenanceWorkorderAdapter(@Nullable List<MaintenanceService.MaintenanceWorkOrder> data) {
        super(R.layout.adapter_maintenance_workorder_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaintenanceService.MaintenanceWorkOrder item) {
        if (item != null) {
            helper.setText(R.id.maintenance_workorder_code_tv, StringUtils.formatString(item.code));

            if (item.createDateTime != null) {
                helper.setText(R.id.maintenance_workorder_create_time_tv, TimeUtils.date2String(new Date(item.createDateTime), DateUtils.SIMPLE_DATE_FORMAT));
            } else {
                helper.setGone(R.id.maintenance_workorder_create_time_tv, false);
                helper.setText(R.id.maintenance_workorder_create_time_tv, "");
            }

            helper.setText(R.id.maintenance_workorder_applicant_name_tv, StringUtils.formatString(item.applicantName));
            helper.setText(R.id.maintenance_workorder_position_tv, StringUtils.formatString(item.location));

            if (item.status != null) {
                helper.setVisible(R.id.maintenance_workorder_status_tv, true);
                helper.setText(R.id.maintenance_workorder_status_tv, MaintenanceHelper.getMaintenanceWorkorderStatusMap(mContext).get(item.status));
                int resId = R.drawable.maintenance_workorder_tag_fill_created_bg;
                switch (item.status) {
                    case MaintenanceConstant.WORKORDER_STATUS_CREATED:
                        resId = R.drawable.maintenance_workorder_tag_fill_created_bg;
                        break;
                    case MaintenanceConstant.WORKORDER_STATUS_PUBLISHED:
                        resId = R.drawable.maintenance_workorder_tag_fill_published_bg;
                        break;
                    case MaintenanceConstant.WORKORDER_STATUS_PROCESS:
                        resId = R.drawable.maintenance_workorder_tag_fill_process_bg;
                        break;
                    case MaintenanceConstant.WORKORDER_STATUS_SUSPENDED_GO:
                        resId = R.drawable.maintenance_workorder_tag_fill_suspended_go_bg;
                        break;
                    case MaintenanceConstant.WORKORDER_STATUS_TERMINATED:
                        resId = R.drawable.maintenance_workorder_tag_fill_terminated_bg;
                        break;
                    case MaintenanceConstant.WORKORDER_STATUS_COMPLETED:
                        resId = R.drawable.maintenance_workorder_tag_fill_completed_bg;
                        break;
                    case MaintenanceConstant.WORKORDER_STATUS_VERIFIED:
                        resId = R.drawable.maintenance_workorder_tag_fill_verified_bg;
                        break;
                    case MaintenanceConstant.WORKORDER_STATUS_ARCHIVED:
                        resId = R.drawable.maintenance_workorder_tag_fill_archived_bg;
                        break;
                    case MaintenanceConstant.WORKORDER_STATUS_APPROVAL:
                        resId = R.drawable.maintenance_workorder_tag_fill_approval_bg;
                        break;
                    case MaintenanceConstant.WORKORDER_STATUS_SUSPENDED_NO:
                        resId = R.drawable.maintenance_workorder_tag_fill_suspended_no_bg;
                        break;
                }
                helper.setBackgroundRes(R.id.maintenance_workorder_status_tv, resId);
            } else {
                helper.setGone(R.id.maintenance_workorder_status_tv, false);
            }

            helper.setGone(R.id.maintenance_workorder_item_dash_line, helper.getLayoutPosition() != getData().size() - 1);
            helper.setGone(R.id.maintenance_workorder_item_secant_line, helper.getLayoutPosition() == getData().size() - 1);

        }
    }
}
