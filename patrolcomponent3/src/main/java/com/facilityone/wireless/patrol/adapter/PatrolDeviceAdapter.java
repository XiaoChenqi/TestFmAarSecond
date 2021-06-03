package com.facilityone.wireless.patrol.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.offline.model.entity.DBPatrolConstant;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolEquEntity;
import com.facilityone.wireless.a.arch.offline.model.service.PatrolDbService;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.patrol.R;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/11/8 10:20 AM
 */
public class PatrolDeviceAdapter extends BaseQuickAdapter<PatrolEquEntity, BaseViewHolder> {

    public PatrolDeviceAdapter(@Nullable List<PatrolEquEntity> data) {
        super(R.layout.item_patrol_device, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatrolEquEntity item) {
        if (item == null) {
            return;
        }

        if (PatrolDbService.COMPREHENSIVE_EQU_ID != item.getEqId()) {
            helper.setText(R.id.name_tv, StringUtils.formatString(item.getName()) + (TextUtils.isEmpty(item.getCode()) ? "" : "(" + item.getCode() + ")"));
            if (item.isDeviceStatus()) {
                helper.setText(R.id.item_tv, item.getItemUseNumber() + mContext.getString(R.string.patrol_task_diawei_ge));
            } else {
                helper.setText(R.id.item_tv, item.getItemStopNumber() + mContext.getString(R.string.patrol_task_diawei_ge));
            }

        } else {
            helper.setText(R.id.name_tv, mContext.getString(R.string.patrol_task_spot_content));
            helper.setText(R.id.item_tv, item.getItemUseNumber() + mContext.getString(R.string.patrol_task_diawei_ge));
        }


        if (item.getCompleted() == DBPatrolConstant.TRUE_VALUE || item.getRemoteCompleted() == DBPatrolConstant.TRUE_VALUE) {
            helper.setText(R.id.state_tv, R.string.patrol_finish);
            helper.setBackgroundRes(R.id.state_tv, R.drawable.fm_patrol_green_tag_bg);
        } else {
            helper.setText(R.id.state_tv, R.string.patrol_unfinish);
            helper.setBackgroundRes(R.id.state_tv, R.drawable.fm_patrol_red_tag_bg);
        }

        helper.setGone(R.id.device_status_tv, !item.isDeviceStatus());
        helper.setGone(R.id.miss_tv, item.isMiss());

        helper.addOnClickListener(R.id.root_rl);

        helper.setGone(R.id.exception_tv, item.getException() == DBPatrolConstant.TRUE_VALUE);

        int layoutPosition = helper.getLayoutPosition();
        helper.setGone(R.id.long_view, layoutPosition == getData().size() - 1);
        helper.setGone(R.id.short_view, layoutPosition != getData().size() - 1);
    }
}
