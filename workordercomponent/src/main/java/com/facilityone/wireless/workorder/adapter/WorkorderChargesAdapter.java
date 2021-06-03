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
 * description:收取明细
 * Date: 2018/9/21 上午11:05
 */
public class WorkorderChargesAdapter extends BaseQuickAdapter<WorkorderService.ChargesBean, BaseViewHolder> {

    private boolean mOpt;
    private OnItemClick mOnItemClick;

    public WorkorderChargesAdapter(@Nullable List<WorkorderService.ChargesBean> data, boolean opt) {
        super(R.layout.adapter_workorder_charge_item, data);
        mOpt = opt;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WorkorderService.ChargesBean item) {
        helper.setText(R.id.charge_name_tv, item.name);
        helper.setText(R.id.charge_cost_tv, "¥ " + StringUtils.double2String(item.amount));


        int currentPosition = helper.getLayoutPosition();
        helper.setGone(R.id.view_long_bottom, currentPosition == getData().size() - 1);
        helper.setGone(R.id.view_short_bottom, currentPosition != getData().size() - 1);
        helper.addOnClickListener(R.id.ll_content);

        if (!mOpt || (item.chargeId != null && item.chargeId == -1)) {
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
        void onBtnDelete(WorkorderService.ChargesBean tool, int position);
    }
}
