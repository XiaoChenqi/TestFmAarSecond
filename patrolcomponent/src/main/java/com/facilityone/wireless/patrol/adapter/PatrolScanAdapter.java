package com.facilityone.wireless.patrol.adapter;

import android.support.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.offline.model.entity.DBPatrolConstant;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolSpotEntity;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.patrol.R;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:扫描适配器
 * Date: 2018/11/6 10:20 AM
 */
public class PatrolScanAdapter extends BaseQuickAdapter<PatrolSpotEntity, BaseViewHolder> {

    public PatrolScanAdapter(@Nullable List<PatrolSpotEntity> data) {
        super(R.layout.item_patrol_scan, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatrolSpotEntity item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.name_tv, StringUtils.formatString(item.getTaskName()));
        helper.setText(R.id.location_tv, StringUtils.formatString(item.getLocationName()));
        helper.setText(R.id.people_tv, StringUtils.formatString(item.getHandler()));
        helper.setText(R.id.patrol_task_item_point_tv, item.getCompNumber() + mContext.getString(R.string.patrol_task_zonghe_xiang));
        helper.setText(R.id.patrol_task_item_device_tv, item.getEquNumber() + mContext.getString(R.string.patrol_task_diawei_ge));

        if (item.getNeedSync() == DBPatrolConstant.DEFAULT_VALUE) {
            helper.setGone(R.id.no_sync_tv, false);
        } else if (item.getNeedSync() == DBPatrolConstant.TRUE_VALUE) {
            helper.setGone(R.id.no_sync_tv, true);
            helper.setText(R.id.no_sync_tv, R.string.patrol_not_sync);
        } else {
            helper.setGone(R.id.no_sync_tv, true);
            helper.setText(R.id.no_sync_tv, R.string.patrol_sync);
        }

        if (item.getRemoteCompleted() == DBPatrolConstant.TRUE_VALUE) {
            item.setCompleted(DBPatrolConstant.TRUE_VALUE);
        }

        if (item.getCompleted() == DBPatrolConstant.TRUE_VALUE) {
            helper.setText(R.id.state_tv, R.string.patrol_finish);
            helper.setBackgroundRes(R.id.state_tv, R.drawable.fm_patrol_green_tag_bg);
        } else {
            helper.setText(R.id.state_tv, R.string.patrol_unfinish);
            helper.setBackgroundRes(R.id.state_tv, R.drawable.fm_patrol_red_tag_bg);
        }

        if (item.getTaskDueStartDateTime() != 0) {
            String startTime = TimeUtils.millis2String(item.getTaskDueStartDateTime(), DateUtils.SIMPLE_DATE_FORMAT_ALL);
            helper.setText(R.id.patrol_task_item_start_time_tv, startTime);
        } else {
            helper.setText(R.id.patrol_task_item_start_time_tv, "");
        }

        if (item.getTaskDueEndDateTime() != 0) {
            String endTime = TimeUtils.millis2String(item.getTaskDueEndDateTime(), DateUtils.SIMPLE_DATE_FORMAT_ALL);
            helper.setText(R.id.patrol_task_item_end_time_tv, endTime);
        } else {
            helper.setText(R.id.patrol_task_item_end_time_tv, "");
        }

        helper.addOnClickListener(R.id.item_rl);
        helper.addOnClickListener(R.id.ll_look_all);

//        helper.setGone(R.id.exception_tv, item.getException() == DBPatrolConstant.TRUE_VALUE);
    }
}
