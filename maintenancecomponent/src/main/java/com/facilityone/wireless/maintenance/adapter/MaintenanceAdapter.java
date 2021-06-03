package com.facilityone.wireless.maintenance.adapter;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
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
 * Created by peter.peng on 2018/11/16.
 */

public class MaintenanceAdapter extends BaseMultiItemQuickAdapter<MaintenanceService.MaintenanceCalendarBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MaintenanceAdapter(List<MaintenanceService.MaintenanceCalendarBean> data) {
        super(data);
        addItemType(MaintenanceConstant.TYPE_TITLE, R.layout.adapter_maintenance_item_title);
        addItemType(MaintenanceConstant.TYPE_CONTENT, R.layout.adapter_maintenance_item);
    }


    @Override
    protected void convert(BaseViewHolder helper, MaintenanceService.MaintenanceCalendarBean item) {
        if (item != null) {

            switch (helper.getItemViewType()) {
                case MaintenanceConstant.TYPE_TITLE:
                    helper.setText(R.id.maintenance_item_title, item.title);
                    break;
                case MaintenanceConstant.TYPE_CONTENT:
                    helper.setText(R.id.maintenance_item_name_tv, StringUtils.formatString(item.pmName));

                    if (item.dateTodo != null) {
                        helper.setText(R.id.maintenance_item_time_tv, TimeUtils.date2String(new Date(item.dateTodo), DateUtils.SIMPLE_DATE_FORMAT_YMD));
                    } else {
                        helper.setText(R.id.maintenance_item_time_tv, "");
                    }

                    if (item.genStatus != null && item.genStatus && item.woIds != null && item.woIds.size() > 0) {
                        helper.setVisible(R.id.maintenance_item_workorder_tag_tv, true);
                    } else {
                        helper.setGone(R.id.maintenance_item_workorder_tag_tv, false);
                    }

                    if (item.status != null) {
                        helper.setVisible(R.id.maintenance_item_status_tag_tv, true);
                        helper.setText(R.id.maintenance_item_status_tag_tv, MaintenanceHelper.getMaintenanceStatusMap(mContext).get(item.status));
                        int resId = R.drawable.maintenance_finished_tag_fill_green_background;
                        switch (item.status) {
                            case MaintenanceConstant.MAINTENANCE_WORKORDER_UNDO:
                                resId = R.drawable.maintenance_undo_tag_fill_blue_background;
                                break;
                            case MaintenanceConstant.MAINTENANCE_WORKORDER_DOING:
                                resId = R.drawable.maintenance_doing_tag_fill_orange_background;
                                break;
                            case MaintenanceConstant.MAINTENANCE_WORKORDER_FINISHED:
                                resId = R.drawable.maintenance_finished_tag_fill_green_background;
                                break;
                            case MaintenanceConstant.MAINTENANCE_WORKORDER_MISS:
                                resId = R.drawable.maintenance_miss_tag_fill_red_background;
                                break;
                        }
                        helper.setBackgroundRes(R.id.maintenance_item_status_tag_tv, resId);
                    } else {
                        helper.setText(R.id.maintenance_item_status_tag_tv, "");
                        helper.setGone(R.id.maintenance_item_status_tag_tv, false);
                    }

                    int layoutPosition = getData().indexOf(item);
                    if (layoutPosition != getData().size() - 1) {
                        helper.setGone(R.id.maintenance_item_dash_line, true);
                        helper.setGone(R.id.maintenance_item_secant_line, false);
                        if (layoutPosition + 1 < getData().size()) {
                            MaintenanceService.MaintenanceCalendarBean maintenanceCalendarBean = getData().get(layoutPosition + 1);
                            if (maintenanceCalendarBean.getItemType() == MaintenanceConstant.TYPE_TITLE) {
                                helper.setGone(R.id.maintenance_item_dash_line, false);
                                helper.setGone(R.id.maintenance_item_secant_line, true);
                            }
                        }
                    } else {
                        helper.setGone(R.id.maintenance_item_dash_line, false);
                        helper.setGone(R.id.maintenance_item_secant_line, true);
                    }
                    break;
            }
        }
    }


}
