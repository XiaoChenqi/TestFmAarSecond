package com.facilityone.wireless.workorder.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderLaborerService;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:派工页面的执行人适配器
 * Date: 2018/7/17 下午2:43
 */
public class WorkorderDispatchLaborerAdapter extends BaseQuickAdapter<WorkorderLaborerService.WorkorderLaborerBean, BaseViewHolder> {

    private boolean leaderShow;

    public WorkorderDispatchLaborerAdapter(@Nullable List<WorkorderLaborerService.WorkorderLaborerBean> data) {
        this(data, true);
    }

    public WorkorderDispatchLaborerAdapter(@Nullable List<WorkorderLaborerService.WorkorderLaborerBean> data, boolean leaderShow) {
        super(R.layout.adapter_workorder_diapatch_laborer_item, data);
        this.leaderShow = leaderShow;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WorkorderLaborerService.WorkorderLaborerBean item) {
        helper.setText(R.id.tv_name, StringUtils.formatString(item.name));
        if (leaderShow) {
            helper.setGone(R.id.itv_check, item.leader);
        } else {
            helper.setGone(R.id.itv_check, false);
        }


        helper.addOnClickListener(R.id.dispatch_item_content);
        helper.addOnClickListener(R.id.btn_delete);

        helper.setGone(R.id.dispatch_secant_line, helper.getLayoutPosition() == getData().size() -1);
        helper.setGone(R.id.dispatch_dash_line, helper.getLayoutPosition() != getData().size() -1);

    }

}
