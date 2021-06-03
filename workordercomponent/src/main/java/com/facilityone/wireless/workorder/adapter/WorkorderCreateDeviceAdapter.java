package com.facilityone.wireless.workorder.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.utils.NoDoubleClickListener;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:报错设备列表
 * Date: 2018/9/21 上午11:05
 */
public class WorkorderCreateDeviceAdapter extends BaseQuickAdapter<SelectDataBean, BaseViewHolder> {

    private OnItemClick mOnItemClick;

    public WorkorderCreateDeviceAdapter(@Nullable List<SelectDataBean> data) {
        super(R.layout.adapter_workorder_create_device_item, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SelectDataBean item) {
        helper.setText(R.id.tv_name, StringUtils.formatString(item.getName()));
        helper.setText(R.id.tv_code, StringUtils.formatString(item.getFullName()));
        helper.setText(R.id.tv_location, StringUtils.formatString(item.getDesc()));


        int currentPosition = helper.getLayoutPosition();
        helper.setGone(R.id.view_long_bottom, currentPosition == getData().size() - 1);
        helper.setGone(R.id.view_short_bottom, currentPosition != getData().size() - 1);
        helper.addOnClickListener(R.id.ll_content);


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
        void onBtnDelete(SelectDataBean device, int position);
    }
}
