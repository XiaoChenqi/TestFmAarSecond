package com.facilityone.wireless.maintenance.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.utils.RecyclerViewUtil;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.FMBottomGridSheetBuilder;
import com.facilityone.wireless.componentservice.workorder.WorkorderService;
import com.facilityone.wireless.maintenance.R;
import com.facilityone.wireless.maintenance.adapter.MaintenanceWorkorderAdapter;
import com.facilityone.wireless.maintenance.model.MaintenanceConstant;
import com.facilityone.wireless.maintenance.model.MaintenanceService;
import com.facilityone.wireless.maintenance.presenter.MaintenanceWorkorderPresenter;
import com.facilityone.wireless.maintenance.widget.MaintenanceBottomGridSheetBuilder;
import com.luojilab.component.componentlib.router.Router;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter.peng on 2018/11/21.
 * 计划性维护详情维护工单界面
 */

public class MaintenanceWorkorderFragment extends BaseFragment<MaintenanceWorkorderPresenter> implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    private static final String MAINTENANCE_INFO = "maintenance_info";

    private RecyclerView mRecyclerView;
    private ImageView mMoreMenuIv;//更多按鈕

    private MaintenanceWorkorderAdapter mMaintenanceWorkorderAdapter;
    private List<MaintenanceService.MaintenanceWorkOrder> mMaintenanceWorkOrderList;

    private MaintenanceService.MaintenanceInfoBean mMaintenanceInfo;

    @Override
    public MaintenanceWorkorderPresenter createPresenter() {
        return new MaintenanceWorkorderPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_maintenance_workorder;
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
        refreshView();
    }


    private void initData() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            mMaintenanceInfo = bundle.getParcelable(MAINTENANCE_INFO);
        }
    }

    private void initView() {
        setTitle(R.string.maintenance_detail_title);

        mRecyclerView = findViewById(R.id.recyclerView);
        mMoreMenuIv = findViewById(R.id.more_menu_iv);

        mMoreMenuIv.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMaintenanceWorkOrderList = new ArrayList<>();
        mMaintenanceWorkorderAdapter = new MaintenanceWorkorderAdapter(mMaintenanceWorkOrderList);
        mRecyclerView.setAdapter(mMaintenanceWorkorderAdapter);
        mMaintenanceWorkorderAdapter.setOnItemClickListener(this);
        RecyclerViewUtil.setHeightMatchParent(true,mRecyclerView);
    }

    private void refreshView() {
        if(mMaintenanceInfo == null || mMaintenanceInfo.workOrders == null || mMaintenanceInfo.workOrders.size() == 0) {
            mMaintenanceWorkorderAdapter.setEmptyView(getNoDataView((ViewGroup) mRecyclerView.getParent()));
        }else {
            mMaintenanceWorkOrderList.clear();
            mMaintenanceWorkOrderList.addAll(mMaintenanceInfo.workOrders);
            mMaintenanceWorkorderAdapter.notifyDataSetChanged();
            RecyclerViewUtil.setHeightMatchParent(false,mRecyclerView);
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Router router = Router.getInstance();
        WorkorderService workorderService = (WorkorderService) router.getService(WorkorderService.class.getSimpleName());
        if(workorderService != null) {
            MaintenanceService.MaintenanceWorkOrder maintenanceWorkOrder = ((MaintenanceWorkorderAdapter) adapter).getData().get(position);
            if(maintenanceWorkOrder != null) {
                int workorderStatus = maintenanceWorkOrder.status;
                String code = maintenanceWorkOrder.code;
                Long woId = maintenanceWorkOrder.woId;
                BaseFragment workorderInfoFragment = workorderService.getWorkorderInfoFragment(MaintenanceConstant.WORKORDER_STATUS_NONE, code, woId);
                start(workorderInfoFragment);
            }

        }
    }

    /**
     * 当点击右下方更多按钮时回调
     * @param v
     */
    @Override
    public void onClick(View v) {
        showBottomDialog();
    }


    /**
     * 显示底部dialog
     */
    private void showBottomDialog() {

        MaintenanceBottomGridSheetBuilder builder = new MaintenanceBottomGridSheetBuilder(getContext());
        builder.addItem(R.drawable.maintance_content,getString(R.string.maintenance_content), MaintenanceConstant.TAG_MENU_MAINTENANCE_CONTENT, FMBottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.maintance_object,getString(R.string.maintenance_object_tip),MaintenanceConstant.TAG_MENU_MAINTENANCE_OBJECT,FMBottomGridSheetBuilder.FIRST_LINE)
                .setIsShowButton(false)
                .setOnSheetItemClickListener(new FMBottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case MaintenanceConstant.TAG_MENU_MAINTENANCE_CONTENT ://维护内容
                                startWithPop(MaintenanceContentFragment.getInstance(mMaintenanceInfo));
                                break;
                            case MaintenanceConstant.TAG_MENU_MAINTENANCE_OBJECT ://对象
                                startWithPop(MaintenanceObjectFragment.getInstance(mMaintenanceInfo));
                                break;
                        }
                    }
                })
                .build()
                .show();
    }

    public static MaintenanceWorkorderFragment getInstance(MaintenanceService.MaintenanceInfoBean maintenanceInfoBean) {
        MaintenanceWorkorderFragment fragment = new MaintenanceWorkorderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MAINTENANCE_INFO,maintenanceInfoBean);
        fragment.setArguments(bundle);
        return fragment;
    }

}
