package com.facilityone.wireless.workorder.fragment;

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
import com.facilityone.wireless.a.arch.ec.selectdata.SelectDataFragment;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.DatePickUtils;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.basiclib.utils.SystemDateUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderListAdapter;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderQueryPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.facilityone.wireless.a.arch.utils.DatePickUtils.YEAR_MONTH;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单查询
 * Date: 2018/7/4 下午4:14
 */
public class WorkorderQueryFragment extends BaseFragment<WorkorderQueryPresenter> implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener, OnRefreshLoadMoreListener, DrawerLayout.DrawerListener {
    private View mQueryHead;
    private TextView mPreTv;
    private TextView mNextTv;
    private TextView mShowMouthTv;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private ImageView mQueryMenuIv;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mMenuLL;

    private EditText mCodeEt;
    private EditText mDescEt;
    private TextView mStartTimeTv;
    private TextView mEndTimeTv;
    private TextView mServiceTv;
    private TextView mTvClearType;
    private ImageView mTvClearWoIv;
    private ImageView mTvClearWoDescIv;

    private RecyclerView mPriorityRv;
    private RecyclerView mStatusRv;

    private static final int MAX_NUMBER = 3;//一行显示几个tag
    private static final int WORKORDER_INFO = 4002;
    private static final int REQUEST_SERVICE_TYPE = 4003;
    private static final String MY_REPAIR = "my_repair";

    private WorkorderListAdapter mAdapter;
    private GridTagAdapter mPTagAdapter;
    private GridTagAdapter mStatusTagAdapter;
    private Page mPage;

    private Calendar mCalendarBeg = Calendar.getInstance();
    private Calendar mCalendarEnd = Calendar.getInstance();

    private Calendar mConditionBeg;
    private Calendar mConditionEnd;

    private Long startTime, endTime;
    private Long conditionStartTime, conditionEndTime;
    private WorkorderService.WorkorderConditionBean mConditionBean;
    private List<AttachmentBean> mPriorityAs;
    private List<AttachmentBean> mStatusAs;

    private int clickPosition;
    private boolean mFromMyRepair;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initDrawerLayout();
        getPresenter().queryPriority(true);
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mFromMyRepair = arguments.getBoolean(MY_REPAIR, false);
        }
    }

    @Override
    public WorkorderQueryPresenter createPresenter() {
        return new WorkorderQueryPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_list;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    private void initView() {
        mQueryHead = findViewById(R.id.query_head);
        mPreTv = findViewById(R.id.history_pre_iv);
        mNextTv = findViewById(R.id.history_next_iv);
        mShowMouthTv = findViewById(R.id.history_time_sep);
        mQueryMenuIv = findViewById(R.id.iv_query_menu);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mMenuLL = findViewById(R.id.ll_menu);
        mTvClearType = findViewById(R.id.clear_type_tv);
        mTvClearWoIv = findViewById(R.id.clear_wo_code_iv);
        mTvClearWoDescIv = findViewById(R.id.clear_wo_desc_iv);
        mCodeEt = findViewById(R.id.work_order_code);
        mDescEt = findViewById(R.id.work_order_desc);
        mStartTimeTv = findViewById(R.id.work_order_create_time_start);
        mEndTimeTv = findViewById(R.id.work_order_create_time_end);
        mServiceTv = findViewById(R.id.work_type_tv);
        mPriorityRv = findViewById(R.id.work_order_query_menu_filter_priority_rv);
        mStatusRv = findViewById(R.id.work_order_query_menu_filter_status_fl);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new WorkorderListAdapter(WorkorderConstant.WORKORER_QUERY);
        mRecyclerView.setAdapter(mAdapter);

        if (mFromMyRepair) {
            setTitle(R.string.workorder_my_report_fault);
            mQueryHead.setVisibility(View.GONE);
            mQueryMenuIv.setVisibility(View.GONE);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            setTitle(R.string.workorder_query);
            mQueryHead.setVisibility(View.VISIBLE);
            mQueryMenuIv.setVisibility(View.VISIBLE);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        mPriorityAs = new ArrayList<>();
        mStatusAs = new ArrayList<>();
        AttachmentBean bb = new AttachmentBean();
        bb.value = -1L;
        bb.name = getString(R.string.workorder_unlimited);
        bb.check = true;
        mStatusAs.add(bb);
        mPriorityAs.add(bb);

        mStatusAs.addAll(getPresenter().getWorkorderStatus(getContext()));

        mPTagAdapter = new GridTagAdapter(getContext(), mPriorityAs);
        mPriorityRv.setLayoutManager(new GridLayoutManager(getContext(), MAX_NUMBER));
        mPriorityRv.setAdapter(mPTagAdapter);

        mStatusTagAdapter = new GridTagAdapter(getContext(), mStatusAs);
        mStatusRv.setLayoutManager(new GridLayoutManager(getContext(), MAX_NUMBER));
        mStatusRv.setAdapter(mStatusTagAdapter);


        mPreTv.setOnClickListener(this);
        mNextTv.setOnClickListener(this);
        mTvClearType.setOnClickListener(this);
        mTvClearWoIv.setOnClickListener(this);
        mTvClearWoDescIv.setOnClickListener(this);
        mShowMouthTv.setOnClickListener(this);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        mAdapter.setOnItemClickListener(this);
        mQueryMenuIv.setOnClickListener(this);
        mStartTimeTv.setOnClickListener(this);
        mEndTimeTv.setOnClickListener(this);
        mServiceTv.setOnClickListener(this);
        mDrawerLayout.addDrawerListener(this);
        findViewById(R.id.work_order_query_menu_filter_reset_btn).setOnClickListener(this);
        findViewById(R.id.work_order_query_menu_filter_sure_btn).setOnClickListener(this);
        findViewById(R.id.work_order_query_menu_operate_ll).setOnClickListener(this);

        int screenWidth = ScreenUtils.getScreenWidth();
        ViewGroup.LayoutParams lp = mMenuLL.getLayoutParams();
        lp.width = (screenWidth * 2 / 3);
        mMenuLL.setLayoutParams(lp);

        mConditionBean = new WorkorderService.WorkorderConditionBean();

        mCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    mTvClearWoIv.setVisibility(View.GONE);
                } else {
                    mTvClearWoIv.setVisibility(View.VISIBLE);
                }
            }
        });

        mDescEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    mTvClearWoDescIv.setVisibility(View.GONE);
                } else {
                    mTvClearWoDescIv.setVisibility(View.VISIBLE);
                }
            }
        });

        initDate();
        onRefresh();
    }

    private void resetCondition() {
        mCodeEt.setText("");
        mDescEt.setText("");
        mStartTimeTv.setText("");
        mEndTimeTv.setText("");
        mServiceTv.setText("");
        mTvClearType.setVisibility(View.GONE);
        mTvClearWoIv.setVisibility(View.GONE);

        mConditionBean.woCode = null;
        showConditionTime();
        mConditionBean.priority = null;
        mConditionBean.typeId = null;
        mConditionBean.status = null;
        for (AttachmentBean priorityA : mPriorityAs) {
            priorityA.check = false;
            if (priorityA.value == -1L) {
                priorityA.check = true;
            }
        }
        mPTagAdapter.notifyDataSetChanged();
        for (AttachmentBean statusA : mStatusAs) {
            statusA.check = false;
            if (statusA.value == -1L) {
                statusA.check = true;
            }
        }
        mStatusTagAdapter.notifyDataSetChanged();
    }

    private void initDrawerLayout() {
        mDrawerLayout.setScrimColor(ContextCompat.getColor(getContext(), R.color.drawer_menu_transparent));
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

        mShowMouthTv.setText(year + getString(R.string.workorder_year) + String.format("%02d", month + 1) + getString(R.string.workorder_month));
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
        } else if (viewId == R.id.work_order_create_time_start) {//开始时间
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
        } else if (viewId == R.id.work_order_create_time_end) {//结束时间
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
        } else if (viewId == R.id.work_type_tv) {//选择服务类型
            startForResult(SelectDataFragment.getInstance(ISelectDataService.DATA_TYPE_SERVICE_TYPE), REQUEST_SERVICE_TYPE);
        } else if (viewId == R.id.work_order_query_menu_filter_reset_btn) {//重置
            resetCondition();
        } else if (viewId == R.id.work_order_query_menu_filter_sure_btn) {//确定
            String code = mCodeEt.getText().toString();
            if (TextUtils.isEmpty(code)) {
                mConditionBean.woCode = null;
            } else {
                mConditionBean.woCode = code;
            }
            String desc = mDescEt.getText().toString();
            if (TextUtils.isEmpty(desc)) {
                mConditionBean.woDescription = null;
            } else {
                mConditionBean.woDescription = desc;
            }
            getPriority();
            getStatus();
            SystemDateUtils.setCalendarPreciseValue(mConditionBeg, mConditionEnd);
            conditionStartTime = mConditionBeg.getTimeInMillis();
            conditionEndTime = mConditionEnd.getTimeInMillis();
            mConditionBean.startDateTime = conditionStartTime;
            mConditionBean.endDateTime = conditionEndTime;
            mDrawerLayout.closeDrawers();
            showLoading();
            onRefresh();
        } else if (viewId == R.id.clear_type_tv) {//清除服务类型
            mServiceTv.setText("");
            mConditionBean.typeId = null;
            mTvClearType.setVisibility(View.GONE);
        } else if (viewId == R.id.clear_wo_code_iv) {//清除工单号
            mCodeEt.setText("");
            mConditionBean.woCode = null;
            mTvClearWoIv.setVisibility(View.GONE);
        } else if (viewId == R.id.clear_wo_desc_iv) {//清除工单描述
            mDescEt.setText("");
            mConditionBean.woDescription = null;
            mTvClearWoDescIv.setVisibility(View.GONE);
        } else if (viewId == R.id.work_order_query_menu_operate_ll) {

        }
    }

    public void getPriority() {
        List<Long> pId = new ArrayList<>();
        for (AttachmentBean a : mPriorityAs) {
            if (a.value != -1L && a.check) {
                pId.add(a.value);
            }
        }
        if (pId.size() > 0) {
            mConditionBean.priority = pId;
        } else {
            mConditionBean.priority = null;
        }
    }

    public void getStatus() {
        List<Long> statusId = new ArrayList<>();
        for (AttachmentBean a : mStatusAs) {
            if (a.value != -1L && a.check) {
                statusId.add(a.value);
                if (a.value == WorkorderConstant.WORK_STATUS_SUSPENDED_GO) {
                    statusId.add((long) WorkorderConstant.WORK_STATUS_SUSPENDED_NO);
                }
            }
        }
        if (statusId.size() > 0) {
            mConditionBean.status = statusId;
        } else {
            mConditionBean.status = null;
        }
    }

    private boolean detectionTime() {
        if (conditionEndTime < conditionStartTime) {
            ToastUtils.showShort(R.string.workorder_select_time_error);
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
        }, YEAR_MONTH);
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
        getPresenter().getConditionWorkorderList(mPage, mConditionBean, true, mFromMyRepair);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        clickPosition = position;
        WorkorderService.WorkorderItemBean workorderItemBean = ((WorkorderListAdapter) adapter).getData().get(position);
        Integer status = workorderItemBean.status;
        Long woId = workorderItemBean.woId;
        String code = workorderItemBean.code;
        startForResult(WorkorderInfoFragment.getInstance(WorkorderConstant.WORK_STATUS_NONE, code, woId), WORKORDER_INFO);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mPage == null || !mPage.haveNext()) {
            refreshLayout.finishLoadMore();
            ToastUtils.showShort(R.string.workorer_no_more_data);
            return;
        }
        getPresenter().getConditionWorkorderList(mPage.nextPage(), mConditionBean, false, mFromMyRepair);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefresh();
    }

    public void refreshSuccessUI(List<WorkorderService.WorkorderItemBean> ms, Page page, boolean refresh) {
        if (page != null) {
            this.mPage = page;
        }
        if (refresh) {
            mRecyclerView.scrollToPosition(0);
            mAdapter.setNewData(ms);
            mRefreshLayout.finishRefresh();
        } else {
            mAdapter.addData(ms);
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
            case REQUEST_SERVICE_TYPE:
                if (bean == null) {
                    mServiceTv.setText("");
                    mConditionBean.typeId = null;
                    mTvClearType.setVisibility(View.GONE);
                } else {
                    mServiceTv.setText(StringUtils.formatString(bean.getFullName()));
                    mConditionBean.typeId = bean.getId();
                    LogUtils.d("service type :" + mConditionBean.typeId);
                    mTvClearType.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    public void setPriorityAs(List<AttachmentBean> priorityAs) {
        mPriorityAs.clear();
        if (priorityAs != null) {
            mPriorityAs.addAll(priorityAs);
        }
        mPTagAdapter.notifyDataSetChanged();
    }

    public void setPriority(Map<Long, String> priority) {
        mAdapter.setPriority(priority);
    }

    public static WorkorderQueryFragment getInstance() {
        return new WorkorderQueryFragment();
    }

    public static WorkorderQueryFragment getInstance(boolean my) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(MY_REPAIR, my);
        WorkorderQueryFragment workorderQueryFragment = new WorkorderQueryFragment();
        workorderQueryFragment.setArguments(bundle);
        return workorderQueryFragment;
    }
}