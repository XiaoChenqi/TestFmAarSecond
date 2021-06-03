package com.facilityone.wireless.workorder.adapter;

import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderHelper;
import com.facilityone.wireless.workorder.module.WorkorderService;

import java.util.Date;
import java.util.Map;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/4 下午4:00
 */
public class WorkorderListAdapter extends BaseQuickAdapter<WorkorderService.WorkorderItemBean, BaseViewHolder> {

    private final int type;
    private Map<Long, String> mPriority;

    public WorkorderListAdapter(int type) {
        super(R.layout.fragment_workorder_list_item);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkorderService.WorkorderItemBean item) {
        helper.setGone(R.id.ll_quick_show, type != WorkorderConstant.WORKORER_QUERY);
        helper.setGone(R.id.bottom_line_view, type != WorkorderConstant.WORKORER_QUERY);
        helper.setGone(R.id.view_placeholder, type != WorkorderConstant.WORKORER_QUERY);
        helper.setGone(R.id.ll_desc, !TextUtils.isEmpty(item.woDescription));

        int layoutPosition = helper.getLayoutPosition();
        if (type == WorkorderConstant.WORKORER_QUERY && layoutPosition == getData().size() - 1) {
            helper.setGone(R.id.bottom_line_view, true);
            helper.setGone(R.id.center_line_view, false);
        } else {
            helper.setGone(R.id.center_line_view, true);
        }

        helper.setText(R.id.code_tv, StringUtils.formatString(item.code));
        helper.setText(R.id.describe_tv, StringUtils.formatString(item.woDescription));
        if (item.createDateTime != null) {
            String date2String = TimeUtils.date2String(new Date(item.createDateTime), DateUtils.SIMPLE_DATE_FORMAT_ALL);
            helper.setText(R.id.date_tv, date2String);
        }

        if (TextUtils.isEmpty(item.priorityName)) {
            if (mPriority != null) {
                item.priority = mPriority.get(item.priorityId);
            }
        } else {
            item.priority = item.priorityName;
        }


        if (item.priority != null) {
            helper.setText(R.id.priority_tv, item.priority);
            helper.setVisible(R.id.priority_tv, true);
        } else {
            helper.setText(R.id.priority_tv, "");
            helper.setVisible(R.id.priority_tv, false);
        }


        if (item.status != null) {
            helper.setVisible(R.id.status_tv, true);
            int resId = R.drawable.workorder_fill_grey_background;
            helper.setText(R.id.status_tv, WorkorderHelper.getWorkStatusMap(mContext).get(item.status));
            switch (item.status) {
                case WorkorderConstant.WORK_STATUS_CREATED:
                    resId = R.drawable.fm_workorder_tag_fill_created_bg;
                    break;
                case WorkorderConstant.WORK_STATUS_PUBLISHED:
                    resId = R.drawable.fm_workorder_tag_fill_published_bg;
                    break;
                case WorkorderConstant.WORK_STATUS_PROCESS:
                    resId = R.drawable.fm_workorder_tag_fill_process_bg;
                    break;
                case WorkorderConstant.WORK_STATUS_SUSPENDED_GO:
                    resId = R.drawable.fm_workorder_tag_fill_suspended_go_bg;
                    break;
                case WorkorderConstant.WORK_STATUS_TERMINATED:
                    resId = R.drawable.fm_workorder_tag_fill_terminated_bg;
                    break;
                case WorkorderConstant.WORK_STATUS_COMPLETED:
                    resId = R.drawable.fm_workorder_tag_fill_completed_bg;
                    break;
                case WorkorderConstant.WORK_STATUS_VERIFIED:
                    resId = R.drawable.fm_workorder_tag_fill_verified_bg;
                    break;
                case WorkorderConstant.WORK_STATUS_ARCHIVED:
                    resId = R.drawable.fm_workorder_tag_fill_archived_bg;
                    break;
                case WorkorderConstant.WORK_STATUS_APPROVAL:
                    resId = R.drawable.fm_workorder_tag_fill_approval_bg;
                    break;
                case WorkorderConstant.WORK_STATUS_SUSPENDED_NO:
                    resId = R.drawable.fm_workorder_tag_fill_suspended_no_bg;
                    break;
            }
            helper.setBackgroundRes(R.id.status_tv, resId);
        } else {
            helper.setVisible(R.id.status_tv, false);
        }
    }

    public void setPriority(Map<Long, String> priority) {
        mPriority = priority;
        notifyDataSetChanged();
    }
}
