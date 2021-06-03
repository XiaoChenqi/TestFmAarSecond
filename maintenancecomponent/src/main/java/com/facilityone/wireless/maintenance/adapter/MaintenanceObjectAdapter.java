package com.facilityone.wireless.maintenance.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.maintenance.R;
import com.facilityone.wireless.maintenance.model.MaintenanceConstant;
import com.facilityone.wireless.maintenance.model.MaintenanceService;

import java.util.List;

/**
 * Created by peter.peng on 2019/4/15.
 */

public class MaintenanceObjectAdapter extends BaseMultiItemQuickAdapter<MaintenanceService.MaintenanceObject, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MaintenanceObjectAdapter(List<MaintenanceService.MaintenanceObject> data) {
        super(data);
        addItemType(MaintenanceConstant.TYPE_TITLE, R.layout.adapter_maintenance_object_item_title);
        addItemType(MaintenanceConstant.TYPE_EQUIPEMNT, R.layout.adapter_maintenance_object_equipment_item);
        addItemType(MaintenanceConstant.TYPE_SPACE, R.layout.adapter_maintenance_object_space_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaintenanceService.MaintenanceObject item) {
        if (item != null) {
            int layoutPosition = helper.getLayoutPosition();
            switch (helper.getItemViewType()) {
                case MaintenanceConstant.TYPE_TITLE:
                    helper.setText(R.id.maintenance_object_item_title, StringUtils.formatString(item.title));
                    if(helper.getLayoutPosition() == 0) {
                        helper.setGone(R.id.maintenance_object_item_space, false);
                    }
                    break;
                case MaintenanceConstant.TYPE_EQUIPEMNT:
                    helper.setText(R.id.maintenance_object_equipment_item_code_tv, StringUtils.formatString(item.code));
                    helper.setText(R.id.maintenance_object_equipment_item_name_tv, StringUtils.formatString(item.name));
                    helper.setText(R.id.maintenance_object_equipment_item_type_tv, StringUtils.formatString(item.eqSystemName));
                    helper.setText(R.id.maintenance_object_equipment_item_position_tv, StringUtils.formatString(item.location));

                    if (layoutPosition != getData().size() - 1) {
                        helper.setGone(R.id.maintenance_object_equipment_item_dash_line, true);
                        helper.setGone(R.id.maintenance_object_equipment_item_secant_line, false);
                        if (layoutPosition + 1 < getData().size()) {
                            MaintenanceService.MaintenanceObject maintenanceObject = getData().get(layoutPosition + 1);
                            if (maintenanceObject.getItemType() == MaintenanceConstant.TYPE_TITLE) {
                                helper.setGone(R.id.maintenance_object_equipment_item_dash_line, false);
                                helper.setGone(R.id.maintenance_object_equipment_item_secant_line, true);
                            }
                        }
                    } else {
                        helper.setGone(R.id.maintenance_object_equipment_item_dash_line, false);
                        helper.setGone(R.id.maintenance_object_equipment_item_secant_line, true);
                    }
                    break;
                case MaintenanceConstant.TYPE_SPACE:
                    helper.setText(R.id.maintenance_object_space_item_location_tv, StringUtils.formatString(item.location));

                    if (layoutPosition != getData().size() - 1) {
                        helper.setGone(R.id.maintenance_object_space_item_dash_line, true);
                        helper.setGone(R.id.maintenance_object_space_item_secant_line, false);
                        if (layoutPosition + 1 < getData().size()) {
                            MaintenanceService.MaintenanceObject maintenanceObject = getData().get(layoutPosition + 1);
                            if (maintenanceObject.getItemType() == MaintenanceConstant.TYPE_TITLE) {
                                helper.setGone(R.id.maintenance_object_space_item_dash_line, false);
                                helper.setGone(R.id.maintenance_object_space_item_secant_line, true);
                            }
                        }
                    } else {
                        helper.setGone(R.id.maintenance_object_space_item_dash_line, false);
                        helper.setGone(R.id.maintenance_object_space_item_secant_line, true);
                    }
                    break;
            }
        }
    }
}
