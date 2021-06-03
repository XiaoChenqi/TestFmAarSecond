package com.facilityone.wireless.patrol.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.module.PatrolConstant;
import com.facilityone.wireless.patrol.module.PatrolQueryService;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检查询适配器
 * Date: 2018/11/6 10:20 AM
 */
public class PatrolQueryAdapter extends BaseQuickAdapter<PatrolQueryService.PatrolQueryBodyBean, BaseViewHolder> {

    private final String[] mTaskStatus;

    public PatrolQueryAdapter(Context context) {
        super(R.layout.item_patrol_query);
        mTaskStatus = context.getResources().getStringArray(R.array.patrol_task_label_stat);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void convert(BaseViewHolder helper, PatrolQueryService.PatrolQueryBodyBean item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.name_tv, StringUtils.formatString(item.patrolName));
        helper.setText(R.id.people_tv, StringUtils.formatString(item.laborer));
        helper.setText(R.id.spot_number_tv, String.format(mContext.getString(R.string.patrol_task_diawei_ge_spot), item.spotNumber == null ? 0 : item.spotNumber));

        if (item.dueStartDateTime != 0) {
            String startTime = TimeUtils.millis2String(item.dueStartDateTime, DateUtils.SIMPLE_DATE_FORMAT_ALL);
            helper.setText(R.id.patrol_task_item_start_time_tv, startTime);
        } else {
            helper.setText(R.id.patrol_task_item_start_time_tv, "");
        }

        if (item.dueEndDateTime != 0) {
            String endTime = TimeUtils.millis2String(item.dueEndDateTime, DateUtils.SIMPLE_DATE_FORMAT_ALL);
            helper.setText(R.id.patrol_task_item_end_time_tv, endTime);
        } else {
            helper.setText(R.id.patrol_task_item_end_time_tv, "");
        }

        helper.addOnClickListener(R.id.item_rl);

        helper.setGone(R.id.repair_tv, item.repairNumber != null && item.repairNumber > 0);
        helper.setGone(R.id.miss_tv, item.leakNumber != null && item.leakNumber > 0);
        helper.setGone(R.id.exception_tv, item.exceptionNumber != null && item.exceptionNumber > 0);

        if (item.status != null) {
            helper.setGone(R.id.state_tv, true);
            String status = null;
            if (item.status < mTaskStatus.length) {
                status = mTaskStatus[item.status];
            }
            if (TextUtils.isEmpty(status)) {
                helper.setGone(R.id.state_tv, false);
            } else {
                helper.setText(R.id.state_tv, status);
            }

            int drawableId = R.drawable.fm_patrol_un_start_tag_bg;
            String people = mContext.getString(R.string.patrol_task_query_laborer_tip);
            switch (item.status) {
                case PatrolConstant.PATROL_STATUS_NOT_START://未开始
                    drawableId = R.drawable.fm_patrol_un_start_tag_bg;
                    people = mContext.getString(R.string.patrol_task_query_laborer_plan_tip);
                    break;
                case PatrolConstant.PATROL_STATUS_INSPECTION://补检
                    drawableId = R.drawable.fm_patrol_supplementary_examination_tag_bg;
                    break;
                case PatrolConstant.PATROL_STATUS_ING://进行中
                    drawableId = R.drawable.fm_patrol_ing_tag_bg;
                    break;
                case PatrolConstant.PATROL_STATUS_COMPLETED://按时完成
                    drawableId = R.drawable.fm_patrol_completed_tag_bg;
                    break;
                case PatrolConstant.PATROL_STATUS_DELAY://延期完成
                    drawableId = R.drawable.fm_patrol_delay_tag_bg;
                    break;
                case PatrolConstant.PATROL_STATUS_UNCOMPLETED://未巡检
                    drawableId = R.drawable.fm_patrol_un_check_tag_bg;
                    people = mContext.getString(R.string.patrol_task_query_laborer_plan_tip);
                    break;
            }
            helper.setBackgroundRes(R.id.state_tv, drawableId);
            helper.setText(R.id.patrol_peoples_tv, people);

        } else {
            helper.setGone(R.id.state_tv, false);
        }

        int layoutPosition = helper.getLayoutPosition();
        helper.setGone(R.id.short_view, layoutPosition != getData().size() - 1);
        helper.setGone(R.id.long_view, layoutPosition == getData().size() - 1);

    }
}
