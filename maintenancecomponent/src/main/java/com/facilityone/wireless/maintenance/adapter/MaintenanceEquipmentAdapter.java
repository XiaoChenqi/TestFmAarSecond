package com.facilityone.wireless.maintenance.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.maintenance.R;
import com.facilityone.wireless.maintenance.model.MaintenanceService;

import java.util.List;

/**
 * Author：mobilefm Peter
 * <p/>
 * Email:
 * <p/>
 * description:
 * <p/>
 * Date: 2020-08-19 11:16
 */
public class MaintenanceEquipmentAdapter extends BaseQuickAdapter<MaintenanceService.MaintenanceEquipment, BaseViewHolder> {

    public MaintenanceEquipmentAdapter(@Nullable List<MaintenanceService.MaintenanceEquipment> data) {
        super(R.layout.adapter_maintenance_object_equipment_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaintenanceService.MaintenanceEquipment item) {
        if(item != null) {
            int layoutPosition = helper.getLayoutPosition();
            helper.setText(R.id.maintenance_object_equipment_item_code_tv, StringUtils.formatString(item.code));
            helper.setText(R.id.maintenance_object_equipment_item_name_tv, StringUtils.formatString(item.name));
            helper.setText(R.id.maintenance_object_equipment_item_type_tv, StringUtils.formatString(item.eqSystemName));
            helper.setText(R.id.maintenance_object_equipment_item_position_tv, StringUtils.formatString(item.location));

            if (layoutPosition != getData().size() - 1) {
                helper.setGone(R.id.maintenance_object_equipment_item_dash_line, true);
                helper.setGone(R.id.maintenance_object_equipment_item_secant_line, false);
            } else {
                helper.setGone(R.id.maintenance_object_equipment_item_dash_line, false);
                helper.setGone(R.id.maintenance_object_equipment_item_secant_line, true);
            }
        }
    }
}
