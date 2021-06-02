package com.facilityone.wireless.a.arch.ec.selectdata;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.ec.module.WorkTeamBean;
import com.facilityone.wireless.basiclib.utils.StringUtils;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工作组
 * Date: 2018/10/25 12:13 PM
 */
public class WorkTeamAdapter extends BaseQuickAdapter<WorkTeamBean, BaseViewHolder> {

    public WorkTeamAdapter(@Nullable List<WorkTeamBean> data) {
        super(R.layout.fragment_arch_select_data_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkTeamBean item) {
        if (item == null) {
            return;
        }

        helper.setText(R.id.title_tv, StringUtils.formatString(item.workTeamName));

        helper.addOnClickListener(R.id.root_ll);

        int layoutPosition = helper.getLayoutPosition();
        helper.setGone(R.id.view_dash, getData().size() - 1 != layoutPosition);
        helper.setGone(R.id.view_line, getData().size() - 1 == layoutPosition);
    }
}
