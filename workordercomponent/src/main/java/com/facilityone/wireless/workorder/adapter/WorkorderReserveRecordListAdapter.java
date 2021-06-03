package com.facilityone.wireless.workorder.adapter;

import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderHelper;
import com.facilityone.wireless.workorder.module.WorkorderService;

import java.util.Date;

/**
 * Created by peter.peng on 2018/12/3.
 * 工单物资预订记录列表界面适配器
 */

public class WorkorderReserveRecordListAdapter extends BaseQuickAdapter<WorkorderService.WorkorderReserveRocordBean, BaseViewHolder> {

    public WorkorderReserveRecordListAdapter() {
        super(R.layout.adapter_workorder_reserve_record_list_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkorderService.WorkorderReserveRocordBean item) {
        if (item != null) {
            helper.setText(R.id.reserve_list_item_code_tv, StringUtils.formatString(item.reservationCode));
            helper.setText(R.id.reserve_list_item_person_name_tv, StringUtils.formatString(item.reservationPersonName));
            if (item.reservationDate != null) {
                helper.setText(R.id.reserve_list_item_date_tv, TimeUtils.date2String(new Date(item.reservationDate), DateUtils.SIMPLE_DATE_FORMAT_YMD));
            } else {
                helper.setText(R.id.reserve_list_item_date_tv, "");
            }
            helper.setText(R.id.reserve_list_item_storage_name_tv, StringUtils.formatString(item.warehouseName));
            helper.setText(R.id.reserve_list_item_workorder_code_tv, StringUtils.formatString(item.woCode));
            if(!TextUtils.isEmpty(item.operateDesc)) {
                helper.setGone(R.id.reserve_list_item_reason_ll,true);
                helper.setText(R.id.reserve_list_item_reason_tv, StringUtils.formatString(item.operateDesc));
            }else {
                helper.setGone(R.id.reserve_list_item_reason_ll,false);
            }

            if (item.status != null) {
                helper.setVisible(R.id.reserve_list_item_status_tv, true);
                helper.setText(R.id.reserve_list_item_status_tv, StringUtils.formatString(WorkorderHelper.getInventoryStatusMap(mContext).get(item.status)));
                int resId = R.drawable.reserve_record_tag_fill_orange_background;
                switch (item.status) {
                    case WorkorderConstant.RESERVE_STATUS_VERIFY_WAIT:
                        resId = R.drawable.reserve_record_tag_fill_orange_background;
                        break;
                    case WorkorderConstant.RESERVE_STATUS_VERIFY_PASS:
                        resId = R.drawable.reserve_record_tag_fill_blue_background;
                        break;
                    case WorkorderConstant.RESERVE_STATUS_VERIFY_BACK:
                        resId = R.drawable.reserve_record_tag_fill_red_background;
                        break;
                    case WorkorderConstant.RESERVE_STATUS_DELIVERIED:
                    case WorkorderConstant.RESERVE_STATUS_CANCEL:
                    case WorkorderConstant.RESERVE_STATUS_CANCEL_BOOK:
                        resId = R.drawable.reserve_record_tag_fill_grey_background;
                        break;
                }
                helper.setBackgroundRes(R.id.reserve_list_item_status_tv, resId);
            } else {
                helper.setGone(R.id.reserve_list_item_status_tv, false);
                helper.setText(R.id.reserve_list_item_status_tv, "");
            }

            helper.setGone(R.id.reserve_list_item_dash_line, helper.getLayoutPosition() != getData().size() - 1);
            helper.setGone(R.id.reserve_list_item_secant_line, helper.getLayoutPosition() == getData().size() - 1);
        }
    }
}
