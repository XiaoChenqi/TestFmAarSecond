package com.facilityone.wireless.demand.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.module.Page;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.DatePickUtils;
import com.facilityone.wireless.basiclib.utils.SystemDateUtils;
import com.facilityone.wireless.demand.R;
import com.facilityone.wireless.demand.adapter.DemandListAdapter;
import com.facilityone.wireless.demand.module.DemandConstant;
import com.facilityone.wireless.demand.module.DemandService;
import com.facilityone.wireless.demand.presenter.DemandListPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:需求列表通用页面
 * Date: 2018/6/21 下午4:07
 */
public class DemandListFragment extends BaseFragment<DemandListPresenter> implements OnRefreshLoadMoreListener, View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    private View mQueryHead;
    private TextView mPreTv;
    private TextView mNextTv;
    private TextView mShowMouthTv;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private DemandListAdapter mAdapter;
    private Page mPage;

    private static final int REQUEST_CODE_INFO_OPT = 20001;
    private static final String LIST_TYPE = "list_type";

    private Integer mType; //传入要请求的状态类型
    private Calendar mCalendarBeg = Calendar.getInstance();
    private Calendar mCalendarEnd = Calendar.getInstance();
    private Long startTime, endTime;
    private int mOptPosition;

    @Override
    public DemandListPresenter createPresenter() {
        return new DemandListPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_demand_list;
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt(LIST_TYPE, -1);
        }
    }

    private void initView() {
        mQueryHead = findViewById(R.id.query_head);
        mPreTv = findViewById(R.id.history_pre_iv);
        mNextTv = findViewById(R.id.history_next_iv);
        mShowMouthTv = findViewById(R.id.history_time_sep);
        mPreTv.setOnClickListener(this);
        mNextTv.setOnClickListener(this);
        mShowMouthTv.setOnClickListener(this);
        initDate();
        String title = "";
        switch (mType) {
            case DemandConstant.DEMAND_REQUEST_UNCHECK:
                title = getString(R.string.demand_uncheck);
                noDateTime();
                break;
            case DemandConstant.DEMAND_REQUES_UNFINISH:
                title = getString(R.string.demand_unfinish);
                noDateTime();
                break;
            case DemandConstant.DEMAND_REQUES_FINISH:
                title = getString(R.string.demand_unevaluate);
                noDateTime();
                break;
            case DemandConstant.DEMAND_REQUES_QUERY:
                title = getString(R.string.demand_query);
                mQueryHead.setVisibility(View.VISIBLE);
                break;
        }
        setTitle(title);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new DemandListAdapter(mType);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        onRefresh();
    }

    //不要时间范围
    private void noDateTime() {
        startTime = null;
        endTime = null;
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
        mShowMouthTv.setText(year + getString(R.string.demand_year) + String.format("%02d",month + 1) + getString(R.string.demand_month));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.history_pre_iv) {//上一月
            preMonth();
        } else if (i == R.id.history_next_iv) {//下一月
            nextMonth();
        } else if (i == R.id.history_time_sep) {//选时间
            pickDate();
        }
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
        onRefresh();
    }

    private void onRefresh() {
        mAdapter.setEmptyView(getLoadingView(mRefreshLayout));
        if (mPage == null) {
            mPage = new Page();
        }
        mPage.reset();
        getPresenter().getUnFinish(mType, mPage, startTime, endTime, true);
    }

    public void refreshSuccessUI(List<DemandService.DemandBean> ms, Page page, boolean refresh) {
        this.mPage = page;
        if (refresh) {
            mAdapter.setNewData(ms);
            mRefreshLayout.finishRefresh();
            mRecyclerView.scrollToPosition(0);
        } else {
            mAdapter.addData(ms);
            mRefreshLayout.finishLoadMore();
        }
        if (mAdapter.getData().size() == 0) {
            mAdapter.setEmptyView(getNoDataView(mRefreshLayout));
        }
    }

    public void refreshErrorUI() {
        mAdapter.setEmptyView(getErrorView(mRefreshLayout));
        mRefreshLayout.finishRefresh(false);
        mRefreshLayout.finishLoadMore(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mPage == null || !mPage.haveNext()) {
            refreshLayout.finishLoadMore();
            ToastUtils.showShort(R.string.demand_no_more_data);
            return;
        }
        getPresenter().getUnFinish(mType, mPage.nextPage(), startTime, endTime, false);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefresh();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DemandService.DemandBean demandBean = (DemandService.DemandBean) adapter.getData().get(position);
        mOptPosition = position;
        if (demandBean != null && demandBean.reqId != null) {
            startForResult(DemandInfoFragment.getInstance(mType, demandBean.reqId), REQUEST_CODE_INFO_OPT);
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK && data == null) {
//            return;
//        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_INFO_OPT) {
//            int opt = data.getInt(DemandConstant.DEMANDOPTTYPE, -1);
//            if (opt == DemandConstant.DEMAND_OPT_TYPE_FINISH || opt == DemandConstant.DEMAND_OPT_TYPE_PLEASED) {
//                mAdapter.remove(mOptPosition);
//            }
            onRefresh();
        }
    }

    public static DemandListFragment getInstance(Integer type) {
        Bundle bundle = new Bundle();
        bundle.putInt(LIST_TYPE, type);
        DemandListFragment instance = new DemandListFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
