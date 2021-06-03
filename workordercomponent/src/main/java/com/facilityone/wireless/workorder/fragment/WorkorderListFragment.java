package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.module.Page;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderListAdapter;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderListPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;
import java.util.Map;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单列表页面
 * Date: 2018/7/4 下午3:49
 */
public class WorkorderListFragment extends BaseFragment<WorkorderListPresenter> implements BaseQuickAdapter.OnItemClickListener, OnRefreshLoadMoreListener {
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private DrawerLayout mDrawerLayout;

    private WorkorderListAdapter mAdapter;
    private Page mPage;

    private static final String LIST_TYPE = "list_type";
    private static final int WORKORDER_INFO = 4001;

    private Integer mType;
    private int clickPosition;


    @Override
    public WorkorderListPresenter createPresenter() {
        return new WorkorderListPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_list;
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
        getPresenter().queryPriority();
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt(LIST_TYPE, -1);
        }
    }

    private void initView() {
        String title = "";
        switch (mType) {
            case WorkorderConstant.WORKORER_PROCESS:
                title = getString(R.string.workorder_undo_home_title);
                break;
            case WorkorderConstant.WORKORER_DISPATCHING:
                title = getString(R.string.workorder_arrange_work_order_title);
                break;
            case WorkorderConstant.WORKORER_AUDIT:
                title = getString(R.string.workorder_approval_work_order_title);
                break;
            case WorkorderConstant.WORKORER_ARCHIVE:
                title = getString(R.string.workorder_archive_home_title);
                break;
        }
        setTitle(title);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new WorkorderListAdapter(mType);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        onRefresh();
    }

    private void onRefresh() {
        mAdapter.setEmptyView(getLoadingView(mRefreshLayout));
        if (mPage == null) {
            mPage = new Page();
        }
        mPage.reset();
        getPresenter().getWorkorderList(mType, mPage, true);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        clickPosition = position;
        WorkorderService.WorkorderItemBean workorderItemBean = ((WorkorderListAdapter) adapter).getData().get(position);
        Integer status = workorderItemBean.status;
        Long woId = workorderItemBean.woId;
        String code = workorderItemBean.code;
        startForResult(WorkorderInfoFragment.getInstance(status, code, woId), WORKORDER_INFO);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mPage == null || !mPage.haveNext()) {
            refreshLayout.finishLoadMore();
            ToastUtils.showShort(R.string.workorer_no_more_data);
            return;
        }
        getPresenter().getWorkorderList(mType, mPage.nextPage(), false);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefresh();
    }

    public void refreshSuccessUI(List<WorkorderService.WorkorderItemBean> ms, Page page, boolean refresh) {
        this.mPage = page;
        if (refresh) {
            mAdapter.setNewData(ms);
            mRefreshLayout.finishRefresh();
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
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
//        Integer originStatus = mAdapter.getData().get(clickPosition).status;
        int status = data.getInt(WorkorderInfoFragment.WORKORDER_STATUS);
        boolean needJump = data.getBoolean(WorkorderInfoFragment.WORKORDER_NEED_JUMP);
//        if (originStatus == status) {
//            return;
//        }
        boolean remove = needJump;
//        switch (originStatus) {
//            case WorkorderConstant.WORK_STATUS_PUBLISHED:// 已发布
//            case WorkorderConstant.WORK_STATUS_PROCESS:// 处理中
//            case WorkorderConstant.WORK_STATUS_SUSPENDED_GO:// 已暂停(继续工作)
//                if (status == WorkorderConstant.WORK_STATUS_PUBLISHED
//                        || status == WorkorderConstant.WORK_STATUS_PROCESS
//                        || status == WorkorderConstant.WORK_STATUS_SUSPENDED_GO) {
//                    remove = false;
//                }
//                break;
//            case WorkorderConstant.WORK_STATUS_TERMINATED:// 已终止
//            case WorkorderConstant.WORK_STATUS_COMPLETED:// 已完成
//            case WorkorderConstant.WORK_STATUS_VERIFIED:// 已验证
//                if (status == WorkorderConstant.WORK_STATUS_TERMINATED
//                        || status == WorkorderConstant.WORK_STATUS_COMPLETED
//                        || status == WorkorderConstant.WORK_STATUS_VERIFIED) {
//                    remove = false;
//                }
//                break;
//            case WorkorderConstant.WORK_STATUS_ARCHIVED:// 已存档
//                break;
//            case WorkorderConstant.WORK_STATUS_SUSPENDED_NO:// 已暂停(不继续工作)
//                break;
//        }
        if (remove) {
            mAdapter.remove(clickPosition);
            if (mAdapter.getData().size() == 0) {
                mAdapter.setEmptyView(getNoDataView(mRefreshLayout));
            }
        } else {
            if(status == WorkorderConstant.WORK_STATUS_VERIFIED) {
                onRefresh();
            }else {
                mAdapter.getData().get(clickPosition).status = status;
                mAdapter.notifyItemChanged(clickPosition);
            }
        }
    }

    public void setPriority(Map<Long, String> priority) {
        mAdapter.setPriority(priority);
    }

    public static WorkorderListFragment getInstance(Integer type) {
        Bundle bundle = new Bundle();
        bundle.putInt(LIST_TYPE, type);
        WorkorderListFragment instance = new WorkorderListFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
