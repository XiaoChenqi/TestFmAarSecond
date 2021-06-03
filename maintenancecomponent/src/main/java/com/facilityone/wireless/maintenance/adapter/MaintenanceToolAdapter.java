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
 * 计划性维护详情维护内容物料适配器
 */

public class MaintenanceToolAdapter extends BaseQuickAdapter<MaintenanceService.Tool,BaseViewHolder>{

    public MaintenanceToolAdapter(@Nullable List<MaintenanceService.Tool> data) {
        super(R.layout.adapter_maintenance_tool_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaintenanceService.Tool item) {
        if(item != null) {
            helper.setText(R.id.maintenance_tool_item_name_tv, StringUtils.formatString(item.name));
            helper.setText(R.id.maintenance_tool_item_model_tv,StringUtils.formatString(item.model));
            if(item.amount != null) {
                helper.setText(R.id.maintenance_tool_item_amount_tv,item.amount.intValue()+"");
            }else {
                helper.setText(R.id.maintenance_tool_item_amount_tv,"");
            }

            helper.setGone(R.id.maintenance_tool_item_dash_line,helper.getLayoutPosition() != getData().size() - 1);
            helper.setGone(R.id.maintenance_tool_item_secant_line,helper.getLayoutPosition() == getData().size() - 1);

        }
    }
}
