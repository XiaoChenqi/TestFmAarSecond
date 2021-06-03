package com.facilityone.wireless.maintenance.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.maintenance.R;
import com.facilityone.wireless.maintenance.model.MaintenanceService;

import java.util.List;

/**
 * Created by peter.peng on 2018/11/21.
 * 计划性维护详情维护内容步骤适配器
 */

public class MaintenanceStepAdapter extends BaseQuickAdapter<MaintenanceService.Step,BaseViewHolder>{

    public MaintenanceStepAdapter(@Nullable List<MaintenanceService.Step> data) {
        super(R.layout.adapter_maintenance_step_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaintenanceService.Step item) {
        if(item != null) {
            helper.setText(R.id.maintenance_step_item_step_index_tv,helper.getLayoutPosition() + 1 + "");
            helper.setText(R.id.maintenance_step_item_group_tv, StringUtils.formatString(item.workTeamName));
            helper.setText(R.id.maintenance_step_item_content_tv,StringUtils.formatString(item.step));

            helper.setGone(R.id.maintenance_step_item_dash_line,helper.getLayoutPosition() != getData().size() - 1);
            helper.setGone(R.id.maintenance_step_item_secant_line,helper.getLayoutPosition() == getData().size() - 1);
        }
    }
}
