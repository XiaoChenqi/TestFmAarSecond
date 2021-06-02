package com.facilityone.wireless.a.arch.ec.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.ec.module.AttachmentBean;
import com.facilityone.wireless.basiclib.utils.StringUtils;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:附件adapter
 * Date: 2018/7/2 下午12:01
 */
public class AttachmentAdapter extends BaseQuickAdapter<AttachmentBean, BaseViewHolder> {

    private boolean showDash;
    private boolean showRightIcon;

    public AttachmentAdapter(@Nullable List<AttachmentBean> data) {
        this(data, true,true);
    }

    public AttachmentAdapter(@Nullable List<AttachmentBean> data, boolean showDash) {
        this(data,showDash,true);
    }

    public AttachmentAdapter(@Nullable List<AttachmentBean> data, boolean showDash,boolean showRightIcon) {
        super(R.layout.item_order, data);
        this.showDash = showDash;
        this.showRightIcon = showRightIcon;
    }

    @Override
    protected void convert(BaseViewHolder helper, AttachmentBean item) {
        helper.setText(R.id.tv_order_id, StringUtils.formatString(item.name));
        helper.addOnClickListener(R.id.tv_order_id);
        if (showDash) {
            helper.setGone(R.id.dash_line, helper.getLayoutPosition() != getData().size() - 1);
        } else {
            helper.setGone(R.id.dash_line, false);
        }
        if(showRightIcon) {
            helper.setVisible(R.id.icon_right,true);
        }else {
            helper.setGone(R.id.icon_right,false);
        }
    }
}
