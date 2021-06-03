package com.facilityone.wireless.patrol.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.GridTagAdapter;
import com.facilityone.wireless.a.arch.ec.module.AttachmentBean;
import com.facilityone.wireless.a.arch.ec.module.ISelectDataService;
import com.facilityone.wireless.a.arch.ec.module.Page;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.ec.selectdata.WorkTeamSelectFragment;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.DatePickUtils;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.basiclib.utils.SystemDateUtils;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.adapter.PatrolQueryAdapter;
import com.facilityone.wireless.patrol.module.PatrolQueryService;
import com.facilityone.wireless.patrol.presenter.PatrolQueryPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检查询
 * Date: 2018/11/19 9:39 AM
 */
public class PatrolQueryFragment extends BaseFragment<PatrolQueryPresenter>
        implements View.OnClickListener,
        OnRefreshLoadMoreListener, DrawerLayout.DrawerListener, TextWatcher, BaseQuickAdapter.OnItemChildClickListener {

    private View mQueryHead;
    private TextView mPreTv;
    private TextView mNextTv;
    private TextView mShowMouthTv;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private ImageView mQueryMenuIv;
    private ImageView mIvClearName;
    private TextView mClearTeamTv;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mMenuLL;

    private EditText mNameEt;
    private TextView mStartTimeTv;
    private TextView mEndTimeTv;
    private TextView mDepTv;

    private RecyclerView mTaskStatusRv;
    private RecyclerView mSpotStatusRv;
    private RecyclerView mSortOrderRv;

    private static final int REQUEST_DEP_TYPE = 80001;
    private static final int MAX_NUMBER = 3;//一行显示几个tag

    private PatrolQueryAdapter mAdapter;
    private GridTagAdapter mTaskAdapter;
    private GridTagAdapter mSpotAdapter;
    private GridTagAdapter mSortAdapter;
    private Page mPage;

    private Calendar mCalendarBeg = Calendar.getInstance();
    private Calendar mCalendarEnd = Calendar.getInstance();

    private Calendar mConditionBeg;
    private Calendar mConditionEnd;

    private Long startTime, endTime;
    private Long conditionStartTime, conditionEndTime;
    private PatrolQueryService.PatrolQueryConditionBean mConditionBean;
    private List<AttachmentBean> mTaskStatusList;
    private List<AttachmentBean> mSpotStatusList;
    private List<AttachmentBean> mSortOrderList;

    @Override
    public PatrolQueryPresenter createPresenter() {
        return new PatrolQueryPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_patrol_query;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initDrawerLayout();
    }

    private void initView() {
        setTitle(R.string.patrol_query);
        mQueryHead = findViewById(R.id.query_head);
        mPreTv = findViewById(R.id.history_pre_iv);
        mNextTv = findViewById(R.id.history_next_iv);
        mShowMouthTv = findViewById(R.id.history_time_sep);
        mQueryHead.setVisibility(View.VISIBLE);
        mQueryMenuIv = findViewById(R.id.iv_query_menu);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mMenuLL = findViewById(R.id.ll_menu);
        mNameEt = findViewById(R.id.name_et);
        mStartTimeTv = findViewById(R.id.time_start);
        mEndTimeTv = findViewById(R.id.time_end);
        mDepTv = findViewById(R.id.dep_tv);
        mTaskStatusRv = findViewById(R.id.task_rv);
        mSpotStatusRv = findViewById(R.id.spot_status_rv);
        mSortOrderRv = findViewById(R.id.sort_order_rv);
        mClearTeamTv = findViewById(R.id.clear_team_tv);
        mIvClearName = findViewById(R.id.clear_name_iv);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PatrolQueryAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        mTaskStatusList = new ArrayList<>();
        mSpotStatusList = new ArrayList<>();
        mSortOrderList = new ArrayList<>();

        AttachmentBean tb2 = new AttachmentBean();
        tb2.value = -1;
        tb2.check = true;
        tb2.name = getString(R.string.patrol_unlimited);
        mTaskStatusList.add(tb2);
        mTaskStatusList.addAll(getPresenter().getStatus(getContext(), R.array.patrol_task_label_stat));
        AttachmentBean tb = new AttachmentBean();
        tb.value = -1;
        tb.check = true;
        tb.name = getString(R.string.patrol_unlimited);
        mSpotStatusList.add(tb);
        mSpotStatusList.addAll(getPresenter().getStatus(getContext(), R.array.patrol_spot_label_stat));
        mSortOrderList.addAll(getPresenter().getSortOrder(getContext()));


        mTaskAdapter = new GridTagAdapter(getContext(), mTaskStatusList);
        mTaskStatusRv.setLayoutManager(new GridLayoutManager(getContext(), MAX_NUMBER));
        mTaskStatusRv.setAdapter(mTaskAdapter);

        mSpotAdapter = new GridTagAdapter(getContext(), mSpotStatusList);
        mSpotStatusRv.setLayoutManager(new GridLayoutManager(getContext(), MAX_NUMBER));
        mSpotStatusRv.setAdapter(mSpotAdapter);

        mSortAdapter = new GridTagAdapter(getContext(), mSortOrderList, true);
        mSortOrderRv.setLayoutManager(new GridLayoutManager(getContext(), MAX_NUMBER -1));
        mSortOrderRv.setAdapter(mSortAdapter);

        mQueryMenuIv.setVisibility(View.VISIBLE);

        mClearTeamTv.setOnClickListener(this);
        mIvClearName.setOnClickListener(this);
        mPreTv.setOnClickListener(this);
        mNextTv.setOnClickListener(this);
        mShowMouthTv.setOnClickListener(this);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mQueryMenuIv.setOnClickListener(this);
        mStartTimeTv.setOnClickListener(this);
        mEndTimeTv.setOnClickListener(this);
        mDepTv.setOnClickListener(this);
        mDrawerLayout.addDrawerListener(this);
        mNameEt.addTextChangedListener(this);
        findViewById(R.id.reset_btn).setOnClickListener(this);
        findViewById(R.id.sure_btn).setOnClickListener(this);
        findViewById(R.id.menu_btn_ll).setOnClickListener(this);

        int screenWidth = ScreenUtils.getScreenWidth();
        ViewGroup.LayoutParams lp = mMenuLL.getLayoutParams();
        lp.width = (screenWidth * 2 / 3);
        mMenuLL.setLayoutParams(lp);

        mConditionBean = new PatrolQueryService.PatrolQueryConditionBean();

        initDate();
        onRefresh();
        mDepTv.setSelected(true);
    }

    private void initDrawerLayout() {
        mDrawerLayout.setScrimColor(ContextCompat.getColor(getContext(), R.color.drawer_menu_transparent));
    }

    private void resetCondition() {
        mNameEt.setText("");
        mStartTimeTv.setText("");
        mEndTimeTv.setText("");
        mDepTv.setText("");

        mConditionBean.reset();
        showConditionTime();
        for (AttachmentBean priorityA : mTaskStatusList) {
            priorityA.check = false;
            if (priorityA.value == -1) {
                priorityA.check = true;
            }
        }
        mTaskAdapter.notifyDataSetChanged();
        for (AttachmentBean statusA : mSpotStatusList) {
            statusA.check = false;
            if (statusA.value == -1) {
                statusA.check = true;
            }
        }
        mSpotAdapter.notifyDataSetChanged();
        for (int i = 0; i < mSortOrderList.size(); i++) {
            if (i == 0){
                mSortOrderList.get(i).check = true;
            }else{
                mSortOrderList.get(i).check = false;
            }
        }
        mSortAdapter.notifyDataSetChanged();
    }

    //设置初始时间 时间范围从0:00 到23:59
    private void initDate() {
        SystemDateUtils.showMonth(null, mCalendarBeg, mCalendarEnd);
        showDate(mCalendarBeg.get(Calendar.YEAR), mCalendarBeg.get(Calendar.MONTH));
    }

    //展示当前所选时间
    private void showDate(int year, int month) {
        startTime = mCalendarBeg.getTimeInMillis();
        endTime = mCalendarEnd.getTimeInMillis();
        mConditionBean.startDateTime = startTime;
        mConditionBean.endDateTime = endTime;
        mShowMouthTv.setText(year + getString(R.string.patrol_year) + String.format("%02d",month + 1) + getString(R.string.patrol_month));
        showConditionTime();
    }

    private void showConditionTime() {
        //设置过滤
        mConditionBeg = Calendar.getInstance();
        mConditionBeg.set(mCalendarBeg.get(Calendar.YEAR),
                mCalendarBeg.get(Calendar.MONTH), 1);
        mConditionEnd = Calendar.getInstance();
        mConditionEnd.set(mCalendarBeg.get(Calendar.YEAR),
                mCalendarBeg.get(Calendar.MONTH),
                mCalendarBeg.getActualMaximum(Calendar.DAY_OF_MONTH));

        Date startTime = mConditionBeg.getTime();
        Date endTime = mConditionEnd.getTime();

        conditionStartTime = mCalendarBeg.getTimeInMillis();
        conditionEndTime = mCalendarEnd.getTimeInMillis();

        String startTimes = TimeUtils.date2String(startTime, DateUtils.SIMPLE_DATE_FORMAT_YMD);
        mStartTimeTv.setText(startTimes);
        String endTimes = TimeUtils.date2String(endTime, DateUtils.SIMPLE_DATE_FORMAT_YMD);
        mEndTimeTv.setText(endTimes);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.history_pre_iv) {//上一月
            preMonth();
        } else if (viewId == R.id.history_next_iv) {//下一月
            nextMonth();
        } else if (viewId == R.id.history_time_sep) {//选时间
            pickDate();
        } else if (viewId == R.id.iv_query_menu) {//菜单
            mDrawerLayout.openDrawer(Gravity.RIGHT);
        } else if (viewId == R.id.time_start) {//开始时间
            DatePickUtils.pickDateOnTime(getActivity(), mCalendarBeg, mConditionBeg, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    Calendar start = Calendar.getInstance();
                    start.setTime(date);

                    Calendar end = Calendar.getInstance();
                    end.setTimeInMillis(conditionEndTime);
                    SystemDateUtils.setCalendarPreciseValue(start, end);
                    conditionStartTime = start.getTimeInMillis();
                    if (!detectionTime()) {
                        return;
                    }
                    mConditionBeg.setTime(date);
                    String date2String = TimeUtils.date2String(date, DateUtils.SIMPLE_DATE_FORMAT_YMD);
                    mStartTimeTv.setText(date2String);
                }
            }, DatePickUtils.YEAR_MONTH_DAY);
        } else if (viewId == R.id.time_end) {//结束时间
            DatePickUtils.pickDateOnTime(getActivity(), mCalendarBeg, mConditionEnd, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    Calendar start = Calendar.getInstance();
                    start.setTimeInMillis(conditionStartTime);

                    Calendar end = Calendar.getInstance();
                    end.setTime(date);
                    SystemDateUtils.setCalendarPreciseValue(start, end);
                    conditionEndTime = end.getTimeInMillis();
                    if (!detectionTime()) {
                        return;
                    }
                    mConditionEnd.setTime(date);
                    String date2String = TimeUtils.date2String(date, DateUtils.SIMPLE_DATE_FORMAT_YMD);
                    mEndTimeTv.setText(date2String);
                }
            }, DatePickUtils.YEAR_MONTH_DAY);
        } else if (viewId == R.id.dep_tv) {//选择部门
            startForResult(WorkTeamSelectFragment.getInstance(), REQUEST_DEP_TYPE);
        } else if (viewId == R.id.reset_btn) {//重置
            resetCondition();
        } else if (viewId == R.id.sure_btn) {//确定
            String code = mNameEt.getText().toString();
            if (TextUtils.isEmpty(code)) {
                mConditionBean.patrolName = null;
            } else {
                mConditionBean.patrolName = code;
            }
            getPriority();
            getStatus();
            getSort();
            SystemDateUtils.setCalendarPreciseValue(mConditionBeg, mConditionEnd);
            conditionStartTime = mConditionBeg.getTimeInMillis();
            conditionEndTime = mConditionEnd.getTimeInMillis();
            mConditionBean.startDateTime = conditionStartTime;
            mConditionBean.endDateTime = conditionEndTime;
            mDrawerLayout.closeDrawers();
            showLoading();
            onRefresh();
        } else if (viewId == R.id.clear_team_tv) {
            mDepTv.setText("");
            mClearTeamTv.setVisibility(View.GONE);
            mConditionBean.workTeamId = null;
        } else if (viewId == R.id.clear_name_iv) {
            mNameEt.setText("");
            mConditionBean.patrolName = null;
            mIvClearName.setVisibility(View.GONE);
        }
    }

    private boolean detectionTime() {
        if (conditionEndTime < conditionStartTime) {
            ToastUtils.showShort(R.string.patrol_select_time_error);
            return false;
        }
        return true;
    }

    private void pickDate() {
        DatePickUtils.pickDateDefault(getActivity(), mCalendarBeg, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                showLoading();
                mCalendarBeg.setTime(date);
                resetDateTime(null);
            }
        });
    }

    //下一月
    private void nextMonth() {
        showLoading();
        resetDateTime(false);
    }

    //上一月
    private void preMonth() {
        showLoading();
        resetDateTime(true);
    }

    //重置开始时间和结束时间
    private void resetDateTime(Boolean isPre) {
        SystemDateUtils.showMonth(isPre, mCalendarBeg, mCalendarEnd);
        showDate(mCalendarBeg.get(Calendar.YEAR), mCalendarBeg.get(Calendar.MONTH));
        mConditionBeg = mCalendarBeg;
        mConditionEnd = mCalendarEnd;
        onRefresh();
    }

    private void onRefresh() {
        mAdapter.setEmptyView(getLoadingView(mRefreshLayout));
        if (mPage == null) {
            mPage = new Page();
        }
        mPage.reset();
        getPresenter().getConditionList(mPage, mConditionBean, true);
    }

    public void getPriority() {
        List<Long> pId = new ArrayList<>();
        for (AttachmentBean a : mTaskStatusList) {
            if (a.value != -1L && a.check) {
                pId.add(a.value);
            }
        }
        if (pId.size() > 0) {
            mConditionBean.taskStatus = pId;
        } else {
            mConditionBean.taskStatus = null;
        }
    }

    public void getStatus() {
        List<Long> statusId = new ArrayList<>();
        for (AttachmentBean a : mSpotStatusList) {
            if (a.value != -1L && a.check) {
                statusId.add(a.value);
            }
        }
        if (statusId.size() > 0) {
            mConditionBean.spotStatus = statusId;
        } else {
            mConditionBean.spotStatus = null;
        }
    }

    private void getSort() {
        for (int i = 0; i < mSortOrderList.size(); i++) {
            if (mSortOrderList.get(i).check){
                mConditionBean.sort = i;
            }
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mPage == null || !mPage.haveNext()) {
            refreshLayout.finishLoadMore();
            ToastUtils.showShort(R.string.patrol_no_more_data);
            return;
        }
        getPresenter().getConditionList(mPage.nextPage(), mConditionBean, false);

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefresh();
    }

    public void refreshSuccessUI(List<PatrolQueryService.PatrolQueryBodyBean> been, Page page, boolean refresh) {
        this.mPage = page;
        if (refresh) {
            mRecyclerView.scrollToPosition(0);
            mAdapter.setNewData(been);
            mRefreshLayout.finishRefresh();
        } else {
            mAdapter.addData(been);
            mRefreshLayout.finishLoadMore();
        }
        if (mAdapter.getData().size() == 0) {
            mAdapter.setEmptyView(getNoDataView(mRefreshLayout));
        }
        dismissLoading();
    }

    public void refreshErrorUI() {
        mAdapter.setEmptyView(getErrorView(mRefreshLayout));
        mRefreshLayout.finishRefresh(false);
        mRefreshLayout.finishLoadMore(false);
        dismissLoading();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {
        KeyboardUtils.hideSoftInput(getActivity());
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (data == null || resultCode != RESULT_OK) {
            return;
        }
        SelectDataBean bean = data.getParcelable(ISelectDataService.SELECT_OFFLINE_DATA_BACK);
        switch (requestCode) {
            case REQUEST_DEP_TYPE:
                if (bean == null) {
                    mClearTeamTv.setVisibility(View.GONE);
                    mDepTv.setText("");
                    mConditionBean.workTeamId = null;
                } else {
                    mDepTv.setText(StringUtils.formatString(bean.getFullName()));
                    mConditionBean.workTeamId = bean.getId();
                    mClearTeamTv.setVisibility(View.VISIBLE);
                    LogUtils.d("workTeamId type :" + mConditionBean.workTeamId);
                }
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(mNameEt.getText().toString())) {
            mIvClearName.setVisibility(View.GONE);
        } else {
            mIvClearName.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        PatrolQueryService.PatrolQueryBodyBean patrolQueryBodyBean = mAdapter.getData().get(position);
        start(PatrolQuerySpotFragment.getInstance(patrolQueryBodyBean.patrolTaskId, patrolQueryBodyBean.patrolName));
    }

    public static PatrolQueryFragment getInstance() {
        return new PatrolQueryFragment();
    }

}
