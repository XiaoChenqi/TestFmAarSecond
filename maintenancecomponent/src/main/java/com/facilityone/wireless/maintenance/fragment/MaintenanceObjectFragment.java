package com.facilityone.wireless.maintenance.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.FMBottomGridSheetBuilder;
import com.facilityone.wireless.maintenance.R;
import com.facilityone.wireless.maintenance.adapter.MaintenanceObjectAdapter;
import com.facilityone.wireless.maintenance.model.MaintenanceConstant;
import com.facilityone.wireless.maintenance.model.MaintenanceService;
import com.facilityone.wireless.maintenance.presenter.MaintenanceObjectPresenter;
import com.facilityone.wireless.maintenance.widget.MaintenanceBottomGridSheetBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter.peng on 2018/11/21.
 * 计划性维护详情对象页面
 */

public class MaintenanceObjectFragment extends BaseFragment<MaintenanceObjectPresenter> implements View.OnClickListener {
    private static final String MAINTENANCE_INFO = "maintenance_info";

    private RecyclerView mRecyclerView;
    private ImageView mMoreMenuIv;//更多按钮

    private MaintenanceObjectAdapter mAdapter;
    private List<MaintenanceService.MaintenanceObject> maintenanceObjectList;

    private MaintenanceService.MaintenanceInfoBean mMaintenanceInfo;

    @Override
    public MaintenanceObjectPresenter createPresenter() {
        return new MaintenanceObjectPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_maintenance_object;
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
        if(bundle != null) {
            mMaintenanceInfo = bundle.getParcelable(MAINTENANCE_INFO);
        }
    }

    private void initView() {
        setTitle(R.string.maintenance_detail_title);
        mMoreMenuIv = findViewById(R.id.more_menu_iv);
        mRecyclerView = findViewById(R.id.maintenance_object_rv);
        mRecyclerView.setNestedScrollingEnabled(false);

        maintenanceObjectList = new ArrayList<>();
        if(mMaintenanceInfo != null) {
            if(mMaintenanceInfo.equipments != null && mMaintenanceInfo.equipments.size() > 0) {
                MaintenanceService.MaintenanceObject maintenanceObject = new MaintenanceService.MaintenanceObject();
                maintenanceObject.type = MaintenanceConstant.TYPE_TITLE;
                maintenanceObject.title = getString(R.string.maintenance_device);
                maintenanceObjectList.add(maintenanceObject);
                for (MaintenanceService.MaintenanceEquipment equipment  : mMaintenanceInfo.equipments) {
                    MaintenanceService.MaintenanceObject equipmentObject = new MaintenanceService.MaintenanceObject();
                    equipmentObject.type = MaintenanceConstant.TYPE_EQUIPEMNT;
                    equipmentObject.code = equipment.code;
                    equipmentObject.name = equipment.name;
                    equipmentObject.eqSystemName = equipment.eqSystemName;
                    equipmentObject.location = equipment.location;
                    maintenanceObjectList.add(equipmentObject);
                }
            }

            if(mMaintenanceInfo.spaces != null && mMaintenanceInfo.spaces.size() > 0) {
                MaintenanceService.MaintenanceObject maintenanceObject = new MaintenanceService.MaintenanceObject();
                maintenanceObject.type = MaintenanceConstant.TYPE_TITLE;
                maintenanceObject.title = getString(R.string.maintenance_location);
                maintenanceObjectList.add(maintenanceObject);
                for (MaintenanceService.Space space : mMaintenanceInfo.spaces) {
                    MaintenanceService.MaintenanceObject spaceObject = new MaintenanceService.MaintenanceObject();
                    spaceObject.type = MaintenanceConstant.TYPE_SPACE;
                    spaceObject.location = space.location;
                    maintenanceObjectList.add(spaceObject);
                }
            }
        }
        mAdapter = new MaintenanceObjectAdapter(maintenanceObjectList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        if(mAdapter.getData().size() == 0) {
            mAdapter.setEmptyView(getNoDataView((ViewGroup)mRecyclerView.getParent()));
        }

        //设置更多按钮的点击事件
        mMoreMenuIv.setOnClickListener(this);


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
                .addItem(R.drawable.maintance_work_order,getString(R.string.maintenance_work_order_tip),MaintenanceConstant.TAG_MENU_MAINTENANCE_WORK_ORDER,FMBottomGridSheetBuilder.FIRST_LINE)
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
                            case MaintenanceConstant.TAG_MENU_MAINTENANCE_WORK_ORDER ://维护工单
                                startWithPop(MaintenanceWorkorderFragment.getInstance(mMaintenanceInfo));
                                break;
                        }
                    }
                })
                .build()
                .show();
    }

    public static MaintenanceObjectFragment getInstance(MaintenanceService.MaintenanceInfoBean maintenanceInfoBean) {
        MaintenanceObjectFragment fragment = new MaintenanceObjectFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MAINTENANCE_INFO,maintenanceInfoBean);
        fragment.setArguments(bundle);
        return fragment;
    }


}
