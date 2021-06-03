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

public class MaintenanceMaterialAdapter extends BaseQuickAdapter<MaintenanceService.Material,BaseViewHolder>{

    public MaintenanceMaterialAdapter(@Nullable List<MaintenanceService.Material> data) {
        super(R.layout.adapter_maintenance_material_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaintenanceService.Material item) {
        if(item != null) {
            helper.setText(R.id.maintenance_material_item_name_tv, StringUtils.formatString(item.name));
            helper.setText(R.id.maintenance_material_item_model_tv,StringUtils.formatString(item.model));
            helper.setText(R.id.maintenance_material_item_brand_tv,StringUtils.formatString(item.brand));
            if(item.amount != null) {
                helper.setText(R.id.maintenance_material_item_amount_tv,item.amount.intValue()+"");
            }else {
                helper.setText(R.id.maintenance_material_item_amount_tv,"");
            }

            helper.setGone(R.id.maintenance_material_item_dash_line,helper.getLayoutPosition() != getData().size() - 1);
            helper.setGone(R.id.maintenance_material_item_secant_line,helper.getLayoutPosition() == getData().size() - 1);

        }
    }
}
