package com.facilityone.wireless.workorder.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderService;

import java.util.List;

/**
 * Created by peter.peng on 2019/4/11.
 */

public class WorkorderApprovalContentAdapter extends BaseQuickAdapter<WorkorderService.ApprovalContentBean,BaseViewHolder> {
    public WorkorderApprovalContentAdapter(@Nullable List<WorkorderService.ApprovalContentBean> data) {
        super(R.layout.adapter_workorder_approval_content_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkorderService.ApprovalContentBean item) {
        if(item != null) {
            if (!TextUtils.isEmpty(item.name)) {
                helper.setText(R.id.approval_name_item_tv, item.name + " : ");
            }

            if (!TextUtils.isEmpty(item.value)) {
                helper.setText(R.id.approval_content_item_tv,item.value);
            }

            helper.setGone(R.id.approval_content_item_bottom_line,helper.getLayoutPosition() != getData().size() - 1);
        }
    }
}
