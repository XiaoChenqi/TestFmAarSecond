package com.facilityone.wireless.patrol.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.module.PatrolConstant;
import com.facilityone.wireless.patrol.module.PatrolQueryService;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/11/22 3:00 PM
 */
public class PatrolQueryEquAdapter extends BaseQuickAdapter<PatrolQueryService.PatrolQueryItemBean, BaseViewHolder> {

    private static final int ITEM_ORDER = 1;//报障
    public boolean mReadOnly;

    public PatrolQueryEquAdapter(@Nullable List<PatrolQueryService.PatrolQueryItemBean> data, boolean readOnly) {
        super(R.layout.item_patrol_query_equ_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatrolQueryService.PatrolQueryItemBean item) {
        if (item == null) {
            return;
        }

        helper.setText(R.id.title_tv, StringUtils.formatString(item.content));
        helper.setText(R.id.result_tv, StringUtils.formatString(item.result));
        helper.setText(R.id.desc_tv, StringUtils.formatString(item.comment));

        TextView tvException = helper.getView(R.id.exception_tv);
        TextView tvSolve = helper.getView(R.id.solved_tv);
        LinearLayout llDesc = helper.getView(R.id.desc_ll);
        TextView tvDel = helper.getView(R.id.del_tv);

        tvException.setVisibility(View.GONE);
        tvSolve.setVisibility(View.GONE);
        tvDel.setVisibility(View.GONE);
        llDesc.setVisibility(View.GONE);
        helper.setGone(R.id.miss_tv, false);

        if (item.status != null) {
            switch (item.status) {
                case PatrolConstant.ITEM_STATUS_EXCEPTION:
                    tvException.setVisibility(View.VISIBLE);
                    llDesc.setVisibility(View.VISIBLE);
                    tvDel.setVisibility(View.VISIBLE);
                    if (item.processed != null && item.processed) {
//                        tvException.setText(R.string.patrol_task_query_exception_handle);
                        tvException.setVisibility(View.GONE);
                        tvSolve.setVisibility(View.VISIBLE);
                    } else {
//                        tvException.setText(R.string.patrol_query_exception);
                        tvException.setVisibility(View.VISIBLE);
                        tvSolve.setVisibility(View.GONE);
                    }

                    if (!mReadOnly) {
                        tvDel.setVisibility(View.VISIBLE);
                    } else {
                        tvDel.setVisibility(View.GONE);
                    }
                    break;
                case PatrolConstant.ITEM_STATUS_EXCEPTION_DEL:
                    tvException.setVisibility(View.GONE);
                    tvSolve.setVisibility(View.VISIBLE);
                    llDesc.setVisibility(View.VISIBLE);
                    tvDel.setVisibility(View.VISIBLE);
//                    tvException.setText(R.string.patrol_task_query_exception_handle);

                    if (!mReadOnly) {
                        tvDel.setVisibility(View.VISIBLE);
                    } else {
                        tvDel.setVisibility(View.GONE);
                    }
                    break;
                case PatrolConstant.ITEM_STATUS_MISS:
                    helper.setGone(R.id.miss_tv, true);
                    break;
            }
        }

        helper.setGone(R.id.people_ll, item.operator != null);
        helper.setGone(R.id.type_ll, item.operationType != null);
        helper.setGone(R.id.time_ll, item.operationTime != null);

        helper.setText(R.id.people_tv, StringUtils.formatString(item.operator));
        String type = item.operationType == null ? "" : item.operationType == ITEM_ORDER ? mContext.getString(R.string.patrol_task_query_repair) : mContext.getString(R.string.patrol_operate_type_mark);
        helper.setText(R.id.type_tv, type);
        String time = item.operationTime == null ? "" : TimeUtils.millis2String(item.operationTime, DateUtils.SIMPLE_DATE_FORMAT_ALL);
        helper.setText(R.id.time_tv, time);

        helper.addOnClickListener(R.id.pic_tv);
        helper.addOnClickListener(R.id.del_tv);
        if (item.imageIds != null && item.imageIds.size() > 0) {
            helper.setGone(R.id.pic_tv, true);
        } else {
            helper.setGone(R.id.pic_tv, false);
        }

        int layoutPosition = helper.getLayoutPosition();
        helper.setGone(R.id.short_line, layoutPosition != getData().size() - 1);
        helper.setGone(R.id.long_line, layoutPosition == getData().size() - 1);
    }
}
