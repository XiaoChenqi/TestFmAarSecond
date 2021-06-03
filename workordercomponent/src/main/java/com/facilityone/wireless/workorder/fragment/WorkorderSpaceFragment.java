package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderSpaceAdapter;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderSpacePresenter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:空间位置
 * Date: 2018/9/20 下午5:02
 */
public class WorkorderSpaceFragment extends BaseFragment<WorkorderSpacePresenter> implements BaseQuickAdapter.OnItemChildClickListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;

    private static final String WORKORDER_ID = "workorder_id";
    private static final String WORKORDER_LOCATIONS = "workorder_locations";
    private static final String CAN_OPT = "can_opt";
    private static final int UPDATE_ADD = 8000;

    private Long mWoId;
    private List<WorkorderService.WorkOrderLocationsBean> mLocationsBeen;
    private boolean mCanOpt;
    private boolean mAdd;
    private WorkorderSpaceAdapter mAdapter;
    private int mPosition;

    @Override
    public WorkorderSpacePresenter createPresenter() {
        return new WorkorderSpacePresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_space;
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
            mWoId = arguments.getLong(WORKORDER_ID);
            mCanOpt = arguments.getBoolean(CAN_OPT, false);
            mLocationsBeen = arguments.getParcelableArrayList(WORKORDER_LOCATIONS);
        }
        if (mLocationsBeen == null) {
            mLocationsBeen = new ArrayList<>();
        }
    }

    private void initView() {
        setTitle(R.string.workorder_location);
        if (mCanOpt) {
            setRightTextButton(R.string.workorder_add_menu, R.id.workorder_space_add_menu_id);
        }
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);

        mAdapter = new WorkorderSpaceAdapter(mLocationsBeen, mCanOpt);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClick(new WorkorderSpaceAdapter.OnItemClick() {
            @Override
            public void onBtnDelete(final WorkorderService.WorkOrderLocationsBean space, final int position) {
                new FMWarnDialogBuilder(getContext()).setIconVisible(false)
                        .setSureBluBg(true)
                        .setTitle(R.string.workorder_tip_title)
                        .setSure(R.string.workorder_confirm)
                        .setTip(R.string.workorder_delete_position)
                        .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, View view) {
                                mPosition = position;
                                dialog.dismiss();
                                del(space.recordId);
                            }
                        }).create(R.style.fmDefaultWarnDialog).show();
            }
        });

        onRefresh();
    }

    private void del(Long recordId) {
        WorkorderOptService.WorkOrderSpaceReq req = new WorkorderOptService.WorkOrderSpaceReq();
        req.woId = mWoId;
        req.operateType = WorkorderConstant.WORKORDER_SPACE_DEL_OPT_TYPE;
        req.recordId = recordId;
        getPresenter().editorWorkorderSpace(req);
    }

    public void refreshList() {
        mAdapter.remove(mPosition);
        onRefresh();
    }

    @Override
    public void onRightTextMenuClick(View view) {
        mAdd = true;
        startForResult(WorkorderSpaceAddFragment.getInstance(null, mWoId, mLocationsBeen), UPDATE_ADD);
    }

    private void onRefresh() {
        if (mLocationsBeen == null || mLocationsBeen.size() == 0) {
            mAdapter.setEmptyView(getNoDataView(mRefreshLayout));
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            WorkorderService.WorkOrderLocationsBean workOrderLocationsBean = data.getParcelable(WorkorderSpaceAddFragment.WORKORDER_LOCATION);
            if (mAdd && workOrderLocationsBean != null) {
                mAdapter.addData(workOrderLocationsBean);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (mCanOpt) {
            mAdd = false;
            WorkorderService.WorkOrderLocationsBean workOrderLocationsBean = mLocationsBeen.get(position);
            startForResult(WorkorderSpaceAddFragment.getInstance(workOrderLocationsBean, mWoId, mLocationsBeen), UPDATE_ADD);
        }
    }

    public static WorkorderSpaceFragment getInstance(ArrayList<WorkorderService.WorkOrderLocationsBean> s
            , Long woId, boolean opt) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(WORKORDER_LOCATIONS, s);
        bundle.putLong(WORKORDER_ID, woId);
        bundle.putBoolean(CAN_OPT, opt);
        WorkorderSpaceFragment fragment = new WorkorderSpaceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
