package com.facilityone.wireless.workorder.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.utils.NoDoubleClickListener;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderHelper;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:关联设备列表
 * Date: 2018/9/21 上午11:05
 */
public class WorkorderDeviceAdapter extends BaseQuickAdapter<WorkorderService.WorkOrderEquipmentsBean, BaseViewHolder> {

    private boolean mOpt;
    private boolean needScan;
    private OnItemClick mOnItemClick;

    public WorkorderDeviceAdapter(@Nullable List<WorkorderService.WorkOrderEquipmentsBean> data, boolean opt, boolean needScan) {
        super(R.layout.adapter_workorder_device_item, data);
        mOpt = opt;
        this.needScan = needScan;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WorkorderService.WorkOrderEquipmentsBean item) {
        helper.setText(R.id.tv_code, StringUtils.formatString(item.equipmentCode));
        helper.setText(R.id.tv_title, StringUtils.formatString(item.equipmentName));
        helper.setText(R.id.tv_desc, StringUtils.formatString(item.failureDesc));
        helper.setText(R.id.tv_deal, StringUtils.formatString(item.repairDesc));
        helper.setText(R.id.tv_location, StringUtils.formatString(item.location));


        int currentPosition = helper.getLayoutPosition();
        helper.setGone(R.id.view_long_bottom, currentPosition == getData().size() - 1);
        helper.setGone(R.id.view_short_bottom, currentPosition != getData().size() - 1);
        helper.addOnClickListener(R.id.ll_content);

        if (!mOpt) {
            SwipeMenuLayout swipeMenuLayout = helper.getView(R.id.swipeMenuLayout);
            swipeMenuLayout.setSwipeEnable(false);
        }

        if (needScan && item.finished != null) {
            helper.setGone(R.id.fault_device_stat_tv, true);
            helper.setText(R.id.fault_device_stat_tv, WorkorderHelper.getWoEquipStatMap(mContext).get(item.finished));
            switch (item.finished) {
                case WorkorderConstant.WO_EQU_STAT_UNFINISH:
                    helper.setTextColor(R.id.fault_device_stat_tv, mContext.getResources()
                            .getColor(R.color.red_ff3a30));
                    break;
                case WorkorderConstant.WO_EQU_STAT_FINISHED:
                    helper.setTextColor(R.id.fault_device_stat_tv, mContext.getResources()
                            .getColor(R.color.green_5eba15));
                    break;
            }
        } else {
            helper.setGone(R.id.fault_device_stat_tv, false);
            helper.setText(R.id.fault_device_stat_tv, "");
        }

        helper.setOnClickListener(R.id.btn_delete, new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ((SwipeMenuLayout) helper.itemView).quickClose();
                if (mOnItemClick != null) {
                    mOnItemClick.onBtnDelete(item, helper.getLayoutPosition());
                }
            }
        });
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onBtnDelete(WorkorderService.WorkOrderEquipmentsBean device, int position);
    }
}
