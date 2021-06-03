package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderHistoryAdapter;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderHistoryPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单操作历史记录
 * Date: 2018/7/12 下午4:38
 */
public class WorkorderHistoryFragment extends BaseFragment<WorkorderHistoryPresenter> {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;

    private WorkorderHistoryAdapter mHistoryAdapter;

    private static final String WORKORDER_HISTORY = "workorder_history";
    private ArrayList<WorkorderService.HistoriesBean> mHistories;

    @Override
    public WorkorderHistoryPresenter createPresenter() {
        return new WorkorderHistoryPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_history_list;
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
            mHistories = arguments.getParcelableArrayList(WORKORDER_HISTORY);
        }
    }

    private void initView() {
        setTitle(R.string.workorder_history_recorder);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRefreshLayout = findViewById(R.id.refreshLayout);

        mHistoryAdapter = new WorkorderHistoryAdapter(mHistories, getContext(), this,getPresenter());
        View noDataView = getNoDataView((ViewGroup) mRecyclerView.getParent());
        mHistoryAdapter.setEmptyView(noDataView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mHistoryAdapter);
    }

    public static WorkorderHistoryFragment getInstance(ArrayList<WorkorderService.HistoriesBean> hs) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(WORKORDER_HISTORY, hs);
        WorkorderHistoryFragment instance = new WorkorderHistoryFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
