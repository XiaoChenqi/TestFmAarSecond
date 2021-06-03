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
 * Date: 2020-08-19 11:53
 */
public class MaintenanceSpaceAdapter extends BaseQuickAdapter<MaintenanceService.Space, BaseViewHolder> {

    public MaintenanceSpaceAdapter(@Nullable List<MaintenanceService.Space> data) {
        super(R.layout.adapter_maintenance_object_space_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaintenanceService.Space item) {
        if(item != null) {
            int layoutPosition = helper.getLayoutPosition();
            helper.setText(R.id.maintenance_object_space_item_location_tv, StringUtils.formatString(item.location));

            if (layoutPosition != getData().size() - 1) {
                helper.setGone(R.id.maintenance_object_space_item_dash_line, true);
                helper.setGone(R.id.maintenance_object_space_item_secant_line, false);
            } else {
                helper.setGone(R.id.maintenance_object_space_item_dash_line, false);
                helper.setGone(R.id.maintenance_object_space_item_secant_line, true);
            }
        }
    }
}
