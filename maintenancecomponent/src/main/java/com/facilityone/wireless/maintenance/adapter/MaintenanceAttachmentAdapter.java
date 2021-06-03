package com.facilityone.wireless.maintenance.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.ec.module.AttachmentBean;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.maintenance.R;

import java.util.List;

/**
 * Created by peter.peng on 2018/11/21.
 */

public class MaintenanceAttachmentAdapter extends BaseQuickAdapter<AttachmentBean,BaseViewHolder> {

    public MaintenanceAttachmentAdapter(@Nullable List<AttachmentBean> data) {
        super(R.layout.adapter_maintenance_attachment_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AttachmentBean item) {
        if(item != null) {
            helper.setText(R.id.maintenance_attachment_item_name_tv, StringUtils.formatString(item.name));

            helper.setGone(R.id.dash_line,helper.getLayoutPosition() != getData().size() - 1);
            helper.setGone(R.id.secant_line,helper.getLayoutPosition() == getData().size() - 1);
        }
    }
}
