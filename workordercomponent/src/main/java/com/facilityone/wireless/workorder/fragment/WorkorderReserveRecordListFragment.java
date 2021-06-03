package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.componentservice.inventory.InventoryService;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderReserveRecordListAdapter;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderReserveRecordListPresenter;
import com.luojilab.component.componentlib.router.Router;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * Created by peter.peng on 2018/12/14.
 * 工单预定物资列表
 */

public class WorkorderReserveRecordListFragment extends BaseFragment<WorkorderReserveRecordListPresenter> implements BaseQuickAdapter.OnItemClickListener, OnRefreshListener {

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private static final String WOID = "woid";
    private static final String WOCODE = "wocode";
    private static final String LABORER = "laborer";
    private static final String REFRESH_STATUS = "refresh_status";
    private static final int WORKORDER_MATERIAL_RESERVE_REQUEST_CODE = 1101;
    private static final int WORKORDER_MATERIAL_INFO_REQUEST_CODE = 1102;

    private WorkorderReserveRecordListAdapter mRecordListAdapter;

    private long mWoId = -1;//工单id
    private int mRefreshStatus;//工单状态
    private String mWoCode;//工单编号
    private boolean mLaborer;//当前工单是否属于登录用户

    @Override
    public WorkorderReserveRecordListPresenter createPresenter() {
        return new WorkorderReserveRecordListPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_reserve_record_list;
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
            mLaborer = bundle.getBoolean(LABORER, false);
            mWoId = bundle.getLong(WOID, -1);
            mRefreshStatus = bundle.getInt(REFRESH_STATUS, WorkorderConstant.WORK_STATUS_NONE);
            mWoCode = bundle.getString(WOCODE, "");
        }
    }

    private void initView() {
        setTitle(R.string.workorder_material);

        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setEnableLoadMore(false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecordListAdapter = new WorkorderReserveRecordListAdapter();
        mRecyclerView.setAdapter(mRecordListAdapter);
        mRecordListAdapter.setOnItemClickListener(this);

        if (mLaborer && mRefreshStatus == WorkorderConstant.WORK_STATUS_PROCESS) {
            setRightTextButton(R.string.workorder_book_tip, R.id.workorder_reserve_id);
        }

        //联网获取数据
        onRefresh();
    }

    private void onRefresh() {
        mRecordListAdapter.setEmptyView(getLoadingView(mRefreshLayout));
        getPresenter().getWorkorderReserveRecordList(mWoId);
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);
        Router router = Router.getInstance();
        InventoryService inventoryService = (InventoryService) router.getService(InventoryService.class.getSimpleName());
        if (inventoryService != null) {
            BaseFragment inventoryReserveFragment = inventoryService.getInventoryReserveFragment(InventoryService.TYPE_FROM_WORKORDER, mWoId, mWoCode);
            startForResult(inventoryReserveFragment, WORKORDER_MATERIAL_RESERVE_REQUEST_CODE);
        }
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefresh();
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Router router = Router.getInstance();
        InventoryService inventoryService = (InventoryService) router.getService(InventoryService.class.getSimpleName());
        WorkorderService.WorkorderReserveRocordBean workorderReserveRocordBean = ((WorkorderReserveRecordListAdapter) adapter).getData().get(position);
        if (inventoryService != null && workorderReserveRocordBean != null) {
            int status = workorderReserveRocordBean.status == null ? -1 : workorderReserveRocordBean.status;
            if (mRefreshStatus != WorkorderConstant.WORK_STATUS_CREATED && mRefreshStatus != WorkorderConstant.WORK_STATUS_PROCESS) {
                status = -1;
            }
            BaseFragment reserveRecordInfoFragment = inventoryService.getReserveRecordInfoFragment(InventoryService.TYPE_FROM_WORKORDER, workorderReserveRocordBean.activityId, status,mRefreshStatus);
            startForResult(reserveRecordInfoFragment, WORKORDER_MATERIAL_INFO_REQUEST_CODE);
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case WORKORDER_MATERIAL_INFO_REQUEST_CODE:
            case WORKORDER_MATERIAL_RESERVE_REQUEST_CODE:
                onRefresh();
                break;
        }
    }

    /**
     * 联网获取工单物资预定记录列表数据成功后回调
     *
     * @param data
     */
    public void getWorkorderReserveRecordListSuccess(List<WorkorderService.WorkorderReserveRocordBean> data) {
        mRecordListAdapter.setNewData(data);
        mRefreshLayout.finishRefresh();

        if (mRecordListAdapter.getData().size() == 0) {
            mRecordListAdapter.setEmptyView(getNoDataView(mRefreshLayout));
        }
    }

    /**
     * 联网获取工单物资预定记录列表数据失败后回调
     */
    public void getWorkorderReserveRecordListError() {
        mRecordListAdapter.setEmptyView(getNoDataView(mRefreshLayout));
        mRefreshLayout.finishRefresh(false);
    }

    public static WorkorderReserveRecordListFragment getInstance(int refreshStatus, boolean laborer, long woId, String woCode) {
        WorkorderReserveRecordListFragment fragment = new WorkorderReserveRecordListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(LABORER, laborer);
        bundle.putLong(WOID, woId);
        bundle.putInt(REFRESH_STATUS, refreshStatus);
        bundle.putString(WOCODE, woCode);
        fragment.setArguments(bundle);
        return fragment;
    }
}
