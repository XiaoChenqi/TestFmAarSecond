package com.facilityone.wireless.workorder.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
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
 * description:工具
 * Date: 2018/9/21 上午11:05
 */
public class WorkorderToolsAdapter extends BaseQuickAdapter<WorkorderService.WorkOrderToolsBean, BaseViewHolder> {

    private boolean mOpt;
    private OnItemClick mOnItemClick;

    public WorkorderToolsAdapter(@Nullable List<WorkorderService.WorkOrderToolsBean> data, boolean opt) {
        super(R.layout.adapter_workorder_tool_item, data);
        mOpt = opt;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WorkorderService.WorkOrderToolsBean item) {
        helper.setText(R.id.tool_name_tv, item.name + (TextUtils.isEmpty(item.unit) ? "" : "(" + item.unit + ")"));
        helper.setText(R.id.tool_cost_tv, "¥ " + StringUtils.double2String(item.cost));
        helper.setText(R.id.tool_charge_model_tv, StringUtils.formatString(item.model));
        helper.setText(R.id.tool_count_tv, item.amount == null ? "" : "x" + item.amount);


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
        void onBtnDelete(WorkorderService.WorkOrderToolsBean tool, int position);
    }
}
