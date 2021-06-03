package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.DatePickUtils;
import com.facilityone.wireless.a.arch.widget.CustomContentItemView;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.presenter.WorkorderExecutorPresenter;

import java.util.Calendar;
import java.util.Date;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:执行人
 * Date: 2018/7/16 下午3:37
 */
public class WorkorderExecutorFragment extends BaseFragment<WorkorderExecutorPresenter> implements View.OnClickListener {

    private CustomContentItemView mCivStartTime;
    private CustomContentItemView mCivEndTime;

    private static final String LABORER_ID = "laborer_id";
    private static final String LABORER_STR = "laborer_str";

    private Long mWoId;
    private Long laborerId;
    private String laborerName;
    private Long startTime, endTime;

    @Override
    public WorkorderExecutorPresenter createPresenter() {
        return new WorkorderExecutorPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_executor;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            laborerName = arguments.getString(LABORER_STR, "");
            mWoId = arguments.getLong(WorkorderInfoFragment.WORKORDER_ID);
            startTime = arguments.getLong(WorkorderInfoFragment.ARRIVAL_DATE_TIME);
            endTime = arguments.getLong(WorkorderInfoFragment.ARRIVAL_DATE_END_TIME);
            laborerId = arguments.getLong(LABORER_ID);
        }
    }

    private void initView() {
        setTitle(R.string.workorder_work_date);
//        setTitle(String.format(getString(R.string.workorder_work_date), laborerName));
        setRightTextButton(R.string.workorder_save, R.id.workorder_laborer_menu_id);
        mCivStartTime = findViewById(R.id.civ_start_time);
        mCivEndTime = findViewById(R.id.civ_end_time);

        mCivStartTime.setOnClickListener(this);
        mCivEndTime.setOnClickListener(this);

        mCivStartTime.setTipColor(R.color.grey_6);
        mCivEndTime.setTipColor(R.color.grey_6);

        if (startTime != 0L) {
            mCivStartTime.setTipText(TimeUtils.millis2String(startTime, DateUtils.SIMPLE_DATE_FORMAT_ALL));
        }

        if (endTime != 0L) {
            mCivEndTime.setTipText(TimeUtils.millis2String(endTime, DateUtils.SIMPLE_DATE_FORMAT_ALL));
        }
    }

    @Override
    public void onRightTextMenuClick(View view) {
        if (startTime == 0L || endTime == 0L) {
            ToastUtils.showShort(R.string.workorder_illegal_time);
            return;
        }
        getPresenter().saveWorkTime(mWoId, laborerId, startTime, endTime);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.civ_start_time) {
            long tempStartTime = startTime == 0L ? Calendar.getInstance().getTimeInMillis() : startTime;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(tempStartTime);
            DatePickUtils.pickDateDefaultYMDHM(getActivity(), calendar, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    if (date.getTime() > endTime) {
                        ToastUtils.showShort(R.string.workorder_laborer_attendance_time_alert);
                        return;
                    }
                    startTime = date.getTime();
                    mCivStartTime.setTipText(TimeUtils.millis2String(startTime, DateUtils.SIMPLE_DATE_FORMAT_ALL));
                }
            });
        } else if (v.getId() == R.id.civ_end_time) {
            long tempEndTime = endTime == 0L ? Calendar.getInstance().getTimeInMillis() : endTime;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(tempEndTime);
            DatePickUtils.pickDateDefaultYMDHM(getActivity(), calendar, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    if (date.getTime() < startTime) {
                        ToastUtils.showShort(R.string.workorder_laborer_complete_time_alert);
                        return;
                    }
                    endTime = date.getTime();
                    mCivEndTime.setTipText(TimeUtils.millis2String(endTime, DateUtils.SIMPLE_DATE_FORMAT_ALL));
                }
            });
        }
    }

    public static WorkorderExecutorFragment getInstance(Long woId,
                                                        String laborer,
                                                        Long laborerId,
                                                        Long starTime,
                                                        Long endTime) {
        if (starTime == null) {
            starTime = 0L;
        }

        if (endTime == null) {
            endTime = 0L;
        }
        Bundle bundle = new Bundle();
        bundle.putLong(WorkorderInfoFragment.WORKORDER_ID, woId);
        bundle.putLong(LABORER_ID, laborerId);
        bundle.putString(LABORER_STR, laborer);
        bundle.putLong(WorkorderInfoFragment.ARRIVAL_DATE_TIME, starTime);
        bundle.putLong(WorkorderInfoFragment.ARRIVAL_DATE_END_TIME, endTime);
        WorkorderExecutorFragment instance = new WorkorderExecutorFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
