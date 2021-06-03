package com.facilityone.wireless.workorder.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.utils.NoDoubleClickListener;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/9/21 上午11:05
 */
public class WorkorderSpaceAdapter extends BaseQuickAdapter<WorkorderService.WorkOrderLocationsBean, BaseViewHolder> {

    private boolean mOpt;
    private OnItemClick mOnItemClick;

    public WorkorderSpaceAdapter(@Nullable List<WorkorderService.WorkOrderLocationsBean> data, boolean opt) {
        super(R.layout.adapter_workorder_space_item, data);
        mOpt = opt;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WorkorderService.WorkOrderLocationsBean item) {
        helper.setText(R.id.tv_deal, StringUtils.formatString(item.repairDesc));
        helper.setText(R.id.tv_location, StringUtils.formatString(item.locationName));


        int currentPosition = helper.getLayoutPosition();
        helper.setGone(R.id.view_long_bottom, currentPosition == getData().size() - 1);
        helper.setGone(R.id.view_short_bottom, currentPosition != getData().size() - 1);
        helper.addOnClickListener(R.id.ll_content);

        if (!mOpt) {
            SwipeMenuLayout swipeMenuLayout = helper.getView(R.id.swipeMenuLayout);
            swipeMenuLayout.setSwipeEnable(false);
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
        void onBtnDelete(WorkorderService.WorkOrderLocationsBean space, int position);
    }
}
