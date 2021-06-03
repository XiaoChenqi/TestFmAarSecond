package com.facilityone.wireless.patrol.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolSpotEntity;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.adapter.PatrolScanAdapter;
import com.facilityone.wireless.patrol.presenter.PatrolScanPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检扫码或者nfc页面
 * Date: 2018/11/16 10:18 AM
 */
public class PatrolScanFragment extends BaseFragment<PatrolScanPresenter> implements BaseQuickAdapter.OnItemChildClickListener, OnRefreshLoadMoreListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;

    private static final int REQUEST_SPOT = 70001;
    private static final int REQUEST_DEVICE = 30001;
    private static final String QRCODE = "qrcode";

    private String mCode;
    private List<PatrolSpotEntity> mSpotEntityList;
    private PatrolScanAdapter mAdapter;
    private Long mTaskId;

    @Override
    public PatrolScanPresenter createPresenter() {
        return new PatrolScanPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_patrol_scan;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        String title = getString(R.string.patrol_spot);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mCode = arguments.getString(QRCODE);
        }
        setTitle(title);
        if (TextUtils.isEmpty(mCode)) {
            pop();
            return;
        }
        initView();
    }

    private void initView() {
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSpotEntityList = new ArrayList<>();
        mAdapter = new PatrolScanAdapter(mSpotEntityList);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        onRefresh();

    }

    /**
     * 请求网络删除巡检任务或更新巡检任务
     */
    private void onRefresh() {
        mAdapter.setEmptyView(getLoadingView((ViewGroup) mRecyclerView.getParent()));
        if (NetworkUtils.isConnected()) {
            showLoading();
            getPresenter().getServicePatrolTask();
        } else {
            getSpotList();
        }

    }

    public void getSpotList() {
        getPresenter().getSpotList(mCode);
    }

    public void refreshUI(List<PatrolSpotEntity> spotEntities) {
        mSpotEntityList.clear();
        if (spotEntities != null && spotEntities.size() > 0) {
            mSpotEntityList.addAll(spotEntities);
            setTitle(spotEntities.get(0).getName());
        } else {
            mAdapter.setEmptyView(getNoDataView((ViewGroup) mRecyclerView.getParent()));
        }
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh();
    }

    public void error() {
        mRefreshLayout.finishRefresh(false);
        mAdapter.setEmptyView(getErrorView((ViewGroup) mRecyclerView.getParent(), R.string.patrol_get_data_error));
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        PatrolSpotEntity patrolSpotEntity = mSpotEntityList.get(position);
        if (patrolSpotEntity == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.item_rl) {
            mTaskId = patrolSpotEntity.getTaskId();
            boolean canGo = getPresenter().canGo(patrolSpotEntity, mSpotEntityList);
            if (canGo) {
                startForResult(PatrolDeviceFragment.getInstance(patrolSpotEntity.getName(), patrolSpotEntity.getPatrolSpotId()), REQUEST_DEVICE);
            } else {
                ToastUtils.showShort(R.string.patrol_overdue_task_tip);
            }
        } else if (id == R.id.ll_look_all) {
            mTaskId = null;
            Long taskId = patrolSpotEntity.getTaskId();
            String taskName = patrolSpotEntity.getTaskName();
            String code = patrolSpotEntity.getCode();
            startForResult(PatrolSpotFragment.getInstance(taskId, taskName, code), REQUEST_SPOT);
        }


    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getPresenter().setTaskSpotDb(mTaskId);
            getSpotList();
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefresh();
    }

    @Override
    public void onNoDataOrErrorClick(View view) {
        onRefresh();
    }

    public static PatrolScanFragment getInstance(String code) {
        Bundle bundle = new Bundle();
        bundle.putString(QRCODE, code);
        PatrolScanFragment instance = new PatrolScanFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
