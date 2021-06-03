package com.facilityone.wireless.patrol.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.offline.model.entity.DBPatrolConstant;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolTaskEntity;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.adapter.PatrolTaskAdapter;
import com.facilityone.wireless.patrol.presenter.PatrolTaskPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检任务页面
 * Date: 2018/11/6 9:38 AM
 */
public class PatrolTaskFragment extends BaseFragment<PatrolTaskPresenter> implements OnRefreshLoadMoreListener,
        BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private LinearLayout mLlTopMenu;
    private TextView mTvTotal;
    private TextView mTvNotSync;
    private TextView mTvUnfinish;

    private static final int REQUEST_SPOT = 20001;

    private List<PatrolTaskEntity> mShowEntities;
    private List<PatrolTaskEntity> mTotalEntities;
    private List<PatrolTaskEntity> mNeedSyncEntities;
    private List<PatrolTaskEntity> mUnfinishEntities;
    private PatrolTaskAdapter mAdapter;
    private int mMenuId;

    @Override
    public PatrolTaskPresenter createPresenter() {
        return new PatrolTaskPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_patrol_task;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        setTitle(R.string.patrol_task);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mLlTopMenu = findViewById(R.id.ll_top);
        mTvTotal = findViewById(R.id.all_tag_tv);
        mMenuId = R.id.all_tag_tv;
        mTvNotSync = findViewById(R.id.not_sync_tv);
        mTvUnfinish = findViewById(R.id.unfinish_tv);
        mRecyclerView = findViewById(R.id.recyclerView);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mShowEntities = new ArrayList<>();
        mTotalEntities = new ArrayList<>();
        mNeedSyncEntities = new ArrayList<>();
        mUnfinishEntities = new ArrayList<>();
        mAdapter = new PatrolTaskAdapter(mShowEntities);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(this);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        mTvTotal.setOnClickListener(this);
        mTvNotSync.setOnClickListener(this);
        mTvUnfinish.setOnClickListener(this);
        onRefresh();
    }

    private void onRefresh() {
        mAdapter.setEmptyView(getLoadingView((ViewGroup) mRecyclerView.getParent()));
        if (NetworkUtils.isConnected()) {
            showLoading();
            getPresenter().getServicePatrolTask();
        } else {
            getDBPatrolTask(true);
        }

    }

    public void getDBPatrolTask(boolean needTime) {//需要当前时间戳去获取离线预期结束比当前时间戳晚的任务
        if (needTime) {
            getPresenter().getDBPatrolTask(System.currentTimeMillis());
        } else {
            getPresenter().getDBPatrolTask(null);
        }

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefresh();
    }

    public void error() {
        mLlTopMenu.setVisibility(View.GONE);
        mRefreshLayout.finishRefresh(false);
        mAdapter.setEmptyView(getErrorView((ViewGroup) mRecyclerView.getParent(), R.string.patrol_get_data_error));
        dismissLoading();
    }

    @SuppressLint("DefaultLocale")
    public void refreshUI(List<PatrolTaskEntity> patrolTaskEntities) {
        mTotalEntities.clear();
        mShowEntities.clear();
        mNeedSyncEntities.clear();
        mUnfinishEntities.clear();
        if (patrolTaskEntities != null && patrolTaskEntities.size() > 0) {
            mLlTopMenu.setVisibility(View.VISIBLE);
            mTotalEntities.addAll(patrolTaskEntities);

            for (PatrolTaskEntity totalEntity : mTotalEntities) {
                if (totalEntity.getNeedSync() == DBPatrolConstant.TRUE_VALUE) {
                    mNeedSyncEntities.add(totalEntity);
                }
                if (totalEntity.getCompleted() != DBPatrolConstant.TRUE_VALUE) {
                    mUnfinishEntities.add(totalEntity);
                }
            }
        } else {
            mLlTopMenu.setVisibility(View.VISIBLE);
            mAdapter.setEmptyView(getNoDataView((ViewGroup) mRecyclerView.getParent()));
        }

        mTvTotal.setText(String.format(getString(R.string.patrol_task_spot_all) + "(%d)", mTotalEntities.size()));
        mTvNotSync.setText(String.format(getString(R.string.patrol_not_sync) + "(%d)", mNeedSyncEntities.size()));
        mTvUnfinish.setText(String.format(getString(R.string.patrol_task_spot_unfinish) + "(%d)", mUnfinishEntities.size()));

        refreshAll(mMenuId, true);

        mAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh();
        dismissLoading();
    }

    @Override
    public void onNoDataOrErrorClick(View view) {
        onRefresh();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        PatrolTaskEntity patrolTaskEntity = mShowEntities.get(position);
        boolean can = getPresenter().canGo(patrolTaskEntity, mShowEntities);
        if (can) {
            startForResult(PatrolSpotFragment.getInstance(patrolTaskEntity.getTaskId(), patrolTaskEntity.getTaskName()), REQUEST_SPOT);
        } else {
            ToastUtils.showShort(R.string.patrol_overdue_task_tip);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        refreshAll(id, false);
    }

    private void refreshAll(int id, boolean canClick) {
        if (!canClick) {
            mTvTotal.setTextColor(getResources().getColor(R.color.green_1ab394));
            mTvNotSync.setTextColor(getResources().getColor(R.color.green_1ab394));
            mTvUnfinish.setTextColor(getResources().getColor(R.color.green_1ab394));
            mTvTotal.setBackgroundResource(R.drawable.fm_patrol_left_empty_bg);
            mTvNotSync.setBackgroundResource(R.drawable.fm_patrol_center_empty_bg);
            mTvUnfinish.setBackgroundResource(R.drawable.fm_patrol_right_empty_bg);
        }

        if (id == R.id.all_tag_tv && (mMenuId != R.id.all_tag_tv || canClick)) {
            mTvTotal.setTextColor(getResources().getColor(R.color.white));
            mTvTotal.setBackgroundResource(R.drawable.fm_patrol_left_fill_bg);
            mMenuId = id;
            mShowEntities.clear();
            mShowEntities.addAll(mTotalEntities);
        } else if (id == R.id.not_sync_tv && (mMenuId != R.id.not_sync_tv || canClick)) {
            mTvNotSync.setTextColor(getResources().getColor(R.color.white));
            mTvNotSync.setBackgroundResource(R.drawable.fm_patrol_center_fill_bg);
            mMenuId = id;
            mShowEntities.clear();
            mShowEntities.addAll(mNeedSyncEntities);
        } else if (id == R.id.unfinish_tv && (mMenuId != R.id.unfinish_tv || canClick)) {
            mTvUnfinish.setTextColor(getResources().getColor(R.color.white));
            mTvUnfinish.setBackgroundResource(R.drawable.fm_patrol_right_fill_bg);
            mMenuId = id;
            mShowEntities.clear();
            mShowEntities.addAll(mUnfinishEntities);
        }

        mRecyclerView.scrollToPosition(0);
        mAdapter.setEmptyView(getNoDataView((ViewGroup) mRecyclerView.getParent()));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getDBPatrolTask(false);
        }
    }

    public static PatrolTaskFragment getInstance() {
        return new PatrolTaskFragment();
    }
}
