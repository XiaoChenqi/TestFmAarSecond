package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderStepAdapter;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderStepPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:维护步骤列表
 * Date: 2018/9/28 2:18 PM
 */
public class WorkorderStepFragment extends BaseFragment<WorkorderStepPresenter> implements BaseQuickAdapter.OnItemChildClickListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;

    private WorkorderStepAdapter mAdapter;

    private static final String WORKORDER_STEPS = "workorder_steps";
    private static final String CAN_OPT = "can_opt";
    private static final String WORKORDER_ID = "workorder_id";
    private static final String WORK_TEAM_ID = "work_team_id";
    private static final int UPDATE = 7000;

    private ArrayList<WorkorderService.StepsBean> mSteps;
    private boolean mCanOpt;
    private long mWoId;
    private Long mWoTeamId;

    @Override
    public WorkorderStepPresenter createPresenter() {
        return new WorkorderStepPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_step_list;
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
            mWoId = arguments.getLong(WORKORDER_ID, -1L);
            mWoTeamId = arguments.getLong(WORK_TEAM_ID, -1L);
            mSteps = arguments.getParcelableArrayList(WORKORDER_STEPS);
            mCanOpt = arguments.getBoolean(CAN_OPT, false);
        }
    }

    private void initView() {
        setTitle(R.string.workorder_menu_step);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRefreshLayout = findViewById(R.id.refreshLayout);

        mAdapter = new WorkorderStepAdapter(mSteps, getActivity(), this);
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        if (mSteps == null || mSteps.size() == 0) {
            View noDataView = getNoDataView((ViewGroup) mRecyclerView.getParent());
            mAdapter.setEmptyView(noDataView);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        WorkorderService.StepsBean stepsBean = mSteps.get(position);
        if (mCanOpt
                && stepsBean != null
                && stepsBean.workTeamId != null
                && mWoTeamId.equals(stepsBean.workTeamId)) {
            if (stepsBean.finished != null && stepsBean.finished) {
//                ToastUtils.showShort(R.string.workorder_steps_completed_notice_tip);
                return;
            }
            startForResult(WorkorderStepUpdateFragment.getInstance(stepsBean, mWoId), UPDATE);
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public static WorkorderStepFragment getInstance(ArrayList<WorkorderService.StepsBean> s
            , Long woId
            , Long workTeamId
            , boolean canOpt) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(WORKORDER_STEPS, s);
        bundle.putLong(WORKORDER_ID, woId);
        bundle.putLong(WORK_TEAM_ID, workTeamId);
        bundle.putBoolean(CAN_OPT, canOpt);
        WorkorderStepFragment instance = new WorkorderStepFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
