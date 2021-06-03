package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderDeviceAdapter;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderDataHolder;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderDevicePresenter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单关联设备
 * Date: 2018/9/20 下午5:02
 */
public class WorkorderDeviceFragment extends BaseFragment<WorkorderDevicePresenter> implements BaseQuickAdapter.OnItemChildClickListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;

    private static final String WORKORDER_ID = "workorder_id";
    private static final String WORKORDER_DEVICES = "workorder_devices";
    private static final String OPT_DEVICE = "opt_device";
    private static final String NEED_SCAN = "need_scan";
    private static final int EDITOR_ADD_DEVICE_CODE = 5000;

    private Long mWoId;
    private List<WorkorderService.WorkOrderEquipmentsBean> mEquipmentsBeanList;
    private boolean mOptDevice;
    private boolean mNeedScan;
    private boolean addDevice;
    private WorkorderDeviceAdapter mDeviceAdapter;
    private int mPosition;
    private boolean opt;//本页面是否操作过

    @Override
    public WorkorderDevicePresenter createPresenter() {
        return new WorkorderDevicePresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_device;
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
            mOptDevice = arguments.getBoolean(OPT_DEVICE, false);
            mNeedScan = arguments.getBoolean(NEED_SCAN, false);
            if(WorkorderDataHolder.hasDeviceData()) {
                mEquipmentsBeanList = (List<WorkorderService.WorkOrderEquipmentsBean>) WorkorderDataHolder.getDeviceData();
            }
//            mEquipmentsBeanList = arguments.getParcelableArrayList(WORKORDER_DEVICES);
        }
        if (mEquipmentsBeanList == null) {
            mEquipmentsBeanList = new ArrayList<>();
        }
    }

    private void initView() {
        setTitle(R.string.workorder_associated_equipment);
        if (mOptDevice) {
            setRightTextButton(R.string.workorder_add_menu, R.id.workorder_device_add_menu_id);
        }
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);

        mDeviceAdapter = new WorkorderDeviceAdapter(mEquipmentsBeanList, mOptDevice, mNeedScan);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mDeviceAdapter);

        mDeviceAdapter.setOnItemChildClickListener(this);
        mDeviceAdapter.setOnItemClick(new WorkorderDeviceAdapter.OnItemClick() {
            @Override
            public void onBtnDelete(final WorkorderService.WorkOrderEquipmentsBean device, final int position) {
                new FMWarnDialogBuilder(getContext()).setIconVisible(false)
                        .setSureBluBg(true)
                        .setTitle(R.string.workorder_tip_title_t)
                        .setSure(R.string.workorder_confirm)
                        .setTip(R.string.workorder_delete_device)
                        .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, View view) {
                                mPosition = position;
                                dialog.dismiss();
                                delDevice(device.equipmentId);
                            }
                        }).create(R.style.fmDefaultWarnDialog).show();
            }
        });

        onRefresh();
    }

    private void delDevice(Long equipmentId) {
        WorkorderOptService.WorkOrderDeviceReq req = new WorkorderOptService.WorkOrderDeviceReq();
        req.woId = mWoId;
        req.operateType = WorkorderConstant.WORKORDER_DEVICE_DEL_OPT_TYPE;
        req.equipmentId = equipmentId;
        getPresenter().editorWorkorderDevice(req);
    }

    public void refreshList() {
        opt = true;
        mDeviceAdapter.remove(mPosition);
        onRefresh();
    }

    @Override
    public void onRightTextMenuClick(View view) {
        addDevice = true;
        startForResult(WorkorderDeviceEditorFragment.getInstance(null, mWoId, addDevice), EDITOR_ADD_DEVICE_CODE);
    }

    private void onRefresh() {
        if (mEquipmentsBeanList == null || mEquipmentsBeanList.size() == 0) {
            mDeviceAdapter.setEmptyView(getNoDataView(mRefreshLayout));
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            opt = true;
            if (addDevice) {
                getPresenter().getWorkorderInfo(mWoId);
            } else {
                mDeviceAdapter.notifyDataSetChanged();
            }
        }
    }

    public void refreshError() {
        ToastUtils.showShort(R.string.workorder_refresh_list_failed);
    }

    public void refreshEquipmentUI(WorkorderService.WorkorderInfoBean data) {
        List<WorkorderService.WorkOrderEquipmentsBean> workOrderEquipments = data.workOrderEquipments;
        mEquipmentsBeanList.clear();
        if(workOrderEquipments !=null){
            mEquipmentsBeanList.addAll(workOrderEquipments);
        }
        mDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        if (opt) {
            setFragmentResult(RESULT_OK, new Bundle());
        }
        super.onDestroyView();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (mOptDevice) {
            WorkorderService.WorkOrderEquipmentsBean workOrderEquipmentsBean = mEquipmentsBeanList.get(position);
            if (mNeedScan && workOrderEquipmentsBean.finished != null && workOrderEquipmentsBean.finished == WorkorderConstant.WO_EQU_STAT_UNFINISH) {
                getPresenter().scan(workOrderEquipmentsBean);
            } else {
                result(workOrderEquipmentsBean);
            }
        }
    }

    public void result(WorkorderService.WorkOrderEquipmentsBean workOrderEquipmentsBean) {
        addDevice = false;
        startForResult(WorkorderDeviceEditorFragment.getInstance(workOrderEquipmentsBean, mWoId, addDevice), EDITOR_ADD_DEVICE_CODE);
    }

    public static WorkorderDeviceFragment getInstance(Long woId, boolean optDevice, boolean needScan) {
        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(WORKORDER_DEVICES, devices);
        bundle.putLong(WORKORDER_ID, woId);
        bundle.putBoolean(OPT_DEVICE, optDevice);
        bundle.putBoolean(NEED_SCAN, needScan);
        WorkorderDeviceFragment fragment = new WorkorderDeviceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
