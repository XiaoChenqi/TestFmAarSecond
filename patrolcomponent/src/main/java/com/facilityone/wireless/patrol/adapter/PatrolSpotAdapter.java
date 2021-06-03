package com.facilityone.wireless.patrol.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.offline.model.entity.DBPatrolConstant;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolSpotEntity;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.patrol.R;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:点位适配器
 * Date: 2018/11/6 10:20 AM
 */
public class PatrolSpotAdapter extends BaseQuickAdapter<PatrolSpotEntity, BaseViewHolder> {

    public PatrolSpotAdapter(@Nullable List<PatrolSpotEntity> data) {
        super(R.layout.item_patrol_spot, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatrolSpotEntity item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.name_tv, StringUtils.formatString(item.getName()));
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


        if (item.getCompleted() == DBPatrolConstant.TRUE_VALUE || item.getRemoteCompleted() == DBPatrolConstant.TRUE_VALUE) {
            helper.setText(R.id.state_tv, R.string.patrol_finish);
            helper.setBackgroundRes(R.id.state_tv, R.drawable.fm_patrol_green_tag_bg);
        } else {
            helper.setText(R.id.state_tv, R.string.patrol_unfinish);
            helper.setBackgroundRes(R.id.state_tv, R.drawable.fm_patrol_red_tag_bg);
        }

        helper.addOnClickListener(R.id.root_ll);

//        helper.setGone(R.id.exception_tv, item.getException() == DBPatrolConstant.TRUE_VALUE);

        int layoutPosition = helper.getLayoutPosition();
        helper.setGone(R.id.long_view, layoutPosition == getData().size() - 1);
        helper.setGone(R.id.short_view, layoutPosition != getData().size() - 1);
    }
}
