package com.facilityone.wireless.patrol.adapter;

import android.support.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.offline.model.entity.DBPatrolConstant;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolTaskEntity;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.patrol.R;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:任务适配器
 * Date: 2018/11/6 10:20 AM
 */
public class PatrolTaskAdapter extends BaseQuickAdapter<PatrolTaskEntity, BaseViewHolder> {

    public PatrolTaskAdapter(@Nullable List<PatrolTaskEntity> data) {
        super(R.layout.item_patrol_task, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatrolTaskEntity item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.name_tv, StringUtils.formatString(item.getTaskName()));
        helper.setText(R.id.patrol_task_item_point_tv, item.getSpotNumber() + mContext.getString(R.string.patrol_task_diawei_ge));
        helper.setText(R.id.patrol_task_item_device_tv, item.getEqNumber() + mContext.getString(R.string.patrol_task_diawei_ge));

        if (item.getDueStartDateTime() != 0) {
            String startTime = TimeUtils.millis2String(item.getDueStartDateTime(), DateUtils.SIMPLE_DATE_FORMAT_ALL);
            helper.setText(R.id.patrol_task_item_start_time_tv, startTime);
        } else {
            helper.setText(R.id.patrol_task_item_start_time_tv, "");
        }

        if (item.getDueEndDateTime() != 0) {
            String endTime = TimeUtils.millis2String(item.getDueEndDateTime(), DateUtils.SIMPLE_DATE_FORMAT_ALL);
            helper.setText(R.id.patrol_task_item_end_time_tv, endTime);
        } else {
            helper.setText(R.id.patrol_task_item_end_time_tv, "");
        }

        if (item.getNeedSync() == DBPatrolConstant.DEFAULT_VALUE) {
            helper.setGone(R.id.no_sync_tv, false);
        } else if (item.getNeedSync() == DBPatrolConstant.TRUE_VALUE) {
            helper.setGone(R.id.no_sync_tv, true);
            helper.setText(R.id.no_sync_tv, R.string.patrol_not_sync);
        } else {
            helper.setGone(R.id.no_sync_tv, true);
            helper.setText(R.id.no_sync_tv, R.string.patrol_sync);
        }


        if (item.getCompleted() == DBPatrolConstant.TRUE_VALUE) {
            helper.setText(R.id.state_tv, R.string.patrol_finish);
            helper.setBackgroundRes(R.id.state_tv, R.drawable.fm_patrol_green_tag_bg);
        } else {
            helper.setText(R.id.state_tv, R.string.patrol_unfinish);
            helper.setBackgroundRes(R.id.state_tv, R.drawable.fm_patrol_red_tag_bg);
        }
        
        helper.addOnClickListener(R.id.item_rl);
        
//        helper.setGone(R.id.exception_tv,item.getException() == DBPatrolConstant.TRUE_VALUE);
    }
}
