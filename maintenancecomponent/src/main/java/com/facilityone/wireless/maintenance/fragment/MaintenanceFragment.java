package com.facilityone.wireless.maintenance.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.SystemDateUtils;
import com.facilityone.wireless.maintenance.R;
import com.facilityone.wireless.maintenance.adapter.MaintenanceAdapter;
import com.facilityone.wireless.maintenance.model.MaintenanceConstant;
import com.facilityone.wireless.maintenance.model.MaintenanceService;
import com.facilityone.wireless.maintenance.presenter.MaintenancePresenter;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by peter.peng on 2018/11/15.
 * 计划性维护首界面
 */

public class MaintenanceFragment extends BaseFragment<MaintenancePresenter> implements CalendarView.OnCalendarSelectListener, CalendarView.OnMonthChangeListener, CalendarView.OnCalendarInterceptListener, BaseQuickAdapter.OnItemClickListener {

    private CalendarView mCalendarView;
    private TextView mTaskTitleTv;
    private TextView mFinishedTv;
    private TextView mMissTv;
    private TextView mUndoTv;
    private TextView mDoingTv;

    private RecyclerView mRecyclerView;
    private MaintenanceAdapter mAdapter;
    private List<MaintenanceService.MaintenanceCalendarBean> mListData;

    private int mFinished;
    private int mMiss;
    private int mUndo;
    private int mDoing;


    private boolean isFirstIntercept = true;
    private boolean isRefreshCalendar = true;
    //查询维护日历的开始时间
    private java.util.Calendar mCalendarStart;
    //查询维护日历的结束时间
    private java.util.Calendar mCalendarEnd;
    private List<MaintenanceService.MaintenanceCalendarBean> mLastSelectDataList;
    private Map<String, Calendar> mSchemeCalendarMap;
    private int currentMonth;
    private Map<Integer, List<MaintenanceService.MaintenanceCalendarBean>> currentMonthData;

    @Override
    public MaintenancePresenter createPresenter() {
        return new MaintenancePresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_maintenance;
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
        getMaintenanceCalendarData();
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            boolean runAlone = bundle.getBoolean(IService.COMPONENT_RUNALONE, false);
            if (runAlone) {
                setSwipeBackEnable(false);
            }
        }

        mCalendarStart = java.util.Calendar.getInstance();
        mCalendarEnd = java.util.Calendar.getInstance();
        mLastSelectDataList = new ArrayList<>();
    }

    private void initView() {
        mCalendarView = findViewById(R.id.calendarView);
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        String title = year + "-" + DateUtils.addZero(month);
        currentMonth = month;
        setTitle(title);

        mRecyclerView = findViewById(R.id.recyclerView);
        mCalendarView.setOnCalendarInterceptListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_header, null);
        mFinishedTv = (TextView) view.findViewById(R.id.maintenance_finished_tv);
        mTaskTitleTv = (TextView) view.findViewById(R.id.task_title_tv);
        mMissTv = (TextView) view.findViewById(R.id.maintenance_miss_tv);
        mUndoTv = (TextView) view.findViewById(R.id.maintenance_undo_tv);
        mDoingTv = (TextView) view.findViewById(R.id.maintenance_doing_tv);
        mListData = new ArrayList<>();
        mAdapter = new MaintenanceAdapter(mListData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.addHeaderView(view);
    }

    /**
     * 联网获取维护日历的数据
     */
    private void getMaintenanceCalendarData() {
        int curYear = mCalendarView.getCurYear();
        int curMonth = mCalendarView.getCurMonth();
        SystemDateUtils.setCalendarPreciseValue(mCalendarStart, mCalendarEnd, curYear, curMonth - 1);
        getPresenter().getMaintenanceCalendarList(mCalendarStart.getTimeInMillis(), mCalendarEnd.getTimeInMillis(), MaintenanceConstant.CALENDAR_STATUS_SWITCH_MONTH);
    }


    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        if (isFirstIntercept) {
            isFirstIntercept = false;
            return calendar.isCurrentDay();
        } else {
            return currentMonth != calendar.getMonth();
        }
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {

    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    /**
     * 当选择日期时回调
     *
     * @param calendar
     * @param isClick
     */
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();
        if (year > 0 && month > 0 && day > 0) {
            mCalendarStart.clear();
            mCalendarEnd.clear();
            SystemDateUtils.setCalendarPreciseValue(mCalendarStart, mCalendarEnd, year, month - 1, day);
            int calendarDay = mCalendarStart.get(java.util.Calendar.DAY_OF_MONTH);
            List<MaintenanceService.MaintenanceCalendarBean> maintenanceCalendarBeans = getCurrentMonthData().get(calendarDay);
            getPresenter().getSchemeCalendarMap(mCalendarStart.getTimeInMillis(), maintenanceCalendarBeans, MaintenanceConstant.CALENDAR_STATUS_SELECT_DAY, true);
        }
        String title = year + "-" + DateUtils.addZero(month) + "-" + DateUtils.addZero(day);
        setTitle(title);
    }

    @Override
    public void onMonthChange(int year, int month) {
        if (year > 0 && month > 0) {
            mCalendarView.clearSingleSelect();
            isRefreshCalendar = true;
            String title = year + "-" + DateUtils.addZero(month);
            currentMonth = month;
            setTitle(title);
            mCalendarStart.clear();
            mCalendarEnd.clear();
            Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
            int selectedYear = selectedCalendar.getYear();
            int selectedMonth = selectedCalendar.getMonth();
            int selectedDay = selectedCalendar.getDay();
            SystemDateUtils.setCalendarPreciseValue(mCalendarStart, mCalendarEnd, year, month - 1);
            if (selectedYear == year && selectedMonth == month && selectedDay > 0) {
                getPresenter().getMaintenanceCalendarList(mCalendarStart.getTimeInMillis(), mCalendarEnd.getTimeInMillis(),
                        MaintenanceConstant.CALENDAR_STATUS_SWITCH_LAST_SELECT_DAY);
            } else {
                getPresenter().getMaintenanceCalendarList(mCalendarStart.getTimeInMillis(), mCalendarEnd.getTimeInMillis(),
                        MaintenanceConstant.CALENDAR_STATUS_SWITCH_MONTH);
            }

        }

    }

    /**
     * 联网获取维护数据失败时回调
     */
    public void getMaintenanceCalendarListError(boolean day, int calendarSwitchStatus) {
        dismissLoading();
        day = calendarSwitchStatus != MaintenanceConstant.CALENDAR_STATUS_SWITCH_MONTH;
        mTaskTitleTv.setText(day ? getString(R.string.maintenance_cur_day_task_title) : getString(R.string.maintenance_cur_month_task_title));
        mLastSelectDataList = null;
        mFinishedTv.setText(getString(R.string.maintenance_finish) + "(0)");
        mMissTv.setText(getString(R.string.maintenance_leak) + "(0)");
        mUndoTv.setText(getString(R.string.maintenance_undo) + "(0)");
        mDoingTv.setText(getString(R.string.maintenance_doing) + "(0)");
    }

    /**
     * 联网获取维护日历数据后刷新日历
     */
    public void refreshCalendar() {
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.clearSchemeDate();
        mCalendarView.setSchemeDate(mSchemeCalendarMap);
    }

    /**
     * 联网获取维护日历数据后刷新界面
     */
    public void refreshView(boolean day, int calendarSwitchStatus) {
        day = calendarSwitchStatus != MaintenanceConstant.CALENDAR_STATUS_SWITCH_MONTH;
        mTaskTitleTv.setText(day ? getString(R.string.maintenance_cur_day_task_title) : getString(R.string.maintenance_cur_month_task_title));
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
        mFinishedTv.setText(getString(R.string.maintenance_finish) + "(" + mFinished + ")");
        mMissTv.setText(getString(R.string.maintenance_leak) + "(" + mMiss + ")");
        mUndoTv.setText(getString(R.string.maintenance_undo) + "(" + mUndo + ")");
        mDoingTv.setText(getString(R.string.maintenance_doing) + "(" + mDoing + ")");
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MaintenanceService.MaintenanceCalendarBean maintenanceCalendarBean = ((MaintenanceAdapter) adapter).getData().get(position);
        if (maintenanceCalendarBean != null && maintenanceCalendarBean.type == MaintenanceConstant.TYPE_CONTENT) {
            maintenanceCalendarBean.pmtodoId = maintenanceCalendarBean.pmtodoId == null ? -1L : maintenanceCalendarBean.pmtodoId;
            //联网获取计划性维护详情数据
            start(MaintenanceContentFragment.getInstance(true, maintenanceCalendarBean.pmId, maintenanceCalendarBean.pmtodoId));
        }

    }

    public void setmSchemeCalendarMap(Map<String, Calendar> mSchemeCalendarMap) {
        this.mSchemeCalendarMap = mSchemeCalendarMap;
    }

    public void setmFinished(int mFinished) {
        this.mFinished = mFinished;
    }

    public void setmMiss(int mMiss) {
        this.mMiss = mMiss;
    }

    public void setmUndo(int mUndo) {
        this.mUndo = mUndo;
    }

    public void setmDoing(int mDoing) {
        this.mDoing = mDoing;
    }

    public boolean isRefreshCalendar() {
        return isRefreshCalendar;
    }

    public void setRefreshCalendar(boolean refreshCalendar) {
        isRefreshCalendar = refreshCalendar;
    }

    public List<MaintenanceService.MaintenanceCalendarBean> getListData() {
        if (mListData == null) {
            mListData = new ArrayList<>();
        }
        return mListData;
    }

    public List<MaintenanceService.MaintenanceCalendarBean> getLastSelectDataList() {
        if (mLastSelectDataList == null) {
            mLastSelectDataList = new ArrayList<>();
        }
        return mLastSelectDataList;
    }

    public Map<Integer, List<MaintenanceService.MaintenanceCalendarBean>> getCurrentMonthData() {
        if (currentMonthData == null) {
            currentMonthData = new HashMap<>();
        }
        return currentMonthData;
    }

    public static MaintenanceFragment getInstance(Bundle bundle) {
        MaintenanceFragment fragment = new MaintenanceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MaintenanceFragment getInstance() {
        MaintenanceFragment fragment = new MaintenanceFragment();
        return fragment;
    }


}
