package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.ec.module.ISelectDataService;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.ec.selectdata.SelectDataFragment;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.CustomContentItemView;
import com.facilityone.wireless.a.arch.widget.EditNumberView;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderDeviceEditorPresenter;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:故障设备添加或修改
 * Date: 2018/9/21 下午5:31
 */
public class WorkorderDeviceEditorFragment extends BaseFragment<WorkorderDeviceEditorPresenter> implements View.OnClickListener {

    private LinearLayout mLlDeviceBaseInfo;
    private CustomContentItemView mEtDeviceName;
    private CustomContentItemView mEtDeviceCode;
    private EditNumberView mEtDesc;
    private EditNumberView mEtDealWay;

    public static final String WORKORDER_DEVICE = "workorder_device";
    private static final String WORKORDER_ID = "workorder_id";
    private static final String ADD_DEVICE = "add_device";
    private static final int REQUEST_EQU = 80012;


    private Long mWoId;
    private Boolean mAddDevice;
    private WorkorderService.WorkOrderEquipmentsBean mEquipmentsBean;
    private WorkorderService.WorkOrderEquipmentsBean mAddUpdateEquipmentsBean;
    private SelectDataBean mBean;

    @Override
    public WorkorderDeviceEditorPresenter createPresenter() {
        return new WorkorderDeviceEditorPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_device_editor;
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
            mAddDevice = arguments.getBoolean(ADD_DEVICE, true);
            mEquipmentsBean = arguments.getParcelable(WORKORDER_DEVICE);
        }
    }

    private void initView() {
        mLlDeviceBaseInfo = findViewById(R.id.ll_device_base_info);
        mEtDeviceName = findViewById(R.id.device_name_civ);
        mEtDeviceCode = findViewById(R.id.device_code_civ);
        mEtDesc = findViewById(R.id.workorder_device_desc_env);
        mEtDealWay = findViewById(R.id.workorder_device_deal_way_env);

        mEtDeviceName.setOnClickListener(this);
        mEtDeviceCode.setOnClickListener(this);

        if (!mAddDevice && mEquipmentsBean != null) {
            mLlDeviceBaseInfo.setVisibility(View.GONE);
            setTitle(StringUtils.formatString(mEquipmentsBean.equipmentName));
            mEtDesc.setDesc(StringUtils.formatString(mEquipmentsBean.failureDesc));
            mEtDealWay.setDesc(StringUtils.formatString(mEquipmentsBean.repairDesc));
        } else {
            mLlDeviceBaseInfo.setVisibility(View.VISIBLE);
            setTitle(R.string.workorder_add_device);
        }
        setRightTextButton(R.string.workorder_save, R.id.workorder_device_save_menu_id);

    }

    @Override
    public void onClick(View v) {
        startForResult(SelectDataFragment.getInstance(ISelectDataService.DATA_TYPE_EQU_ALL), REQUEST_EQU);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            mBean = data.getParcelable(ISelectDataService.SELECT_OFFLINE_DATA_BACK);
            if (mBean == null) {
                return;
            }
            mEtDeviceName.setTipText(StringUtils.formatString(mBean.getName()));
            mEtDeviceCode.setTipText(StringUtils.formatString(mBean.getFullName()));
        }
    }

    @Override
    public void onRightTextMenuClick(View view) {
        mAddUpdateEquipmentsBean = new WorkorderService.WorkOrderEquipmentsBean();
        WorkorderOptService.WorkOrderDeviceReq request = new WorkorderOptService.WorkOrderDeviceReq();
        request.woId = mWoId;
        request.failureDesc = mEtDesc.getDesc();
        request.repairDesc = mEtDealWay.getDesc();
        if (mAddDevice) {
            request.operateType = WorkorderConstant.WORKORDER_DEVICE_ADD_OPT_TYPE;
            if (TextUtils.isEmpty(mEtDeviceName.getTipText().toString()) || mBean == null) {
                ToastUtils.showShort(R.string.workorder_select_device_hint);
                return;
            }
            request.equipmentId = mBean.getId();
            mAddUpdateEquipmentsBean.equipmentName = mBean.getName();
            mAddUpdateEquipmentsBean.equipmentCode = mBean.getFullName();
            mAddUpdateEquipmentsBean.equipmentId = mBean.getId();
            mAddUpdateEquipmentsBean.failureDesc = request.failureDesc;
            mAddUpdateEquipmentsBean.repairDesc = request.repairDesc;
        } else {
            request.operateType = WorkorderConstant.WORKORDER_DEVICE_UPDATE_OPT_TYPE;
            if (mEquipmentsBean != null) {
                request.equipmentId = mEquipmentsBean.equipmentId;
                mEquipmentsBean.failureDesc = request.failureDesc;
                mEquipmentsBean.repairDesc = request.repairDesc;
            }
            mAddUpdateEquipmentsBean = mEquipmentsBean;
        }
        getPresenter().editorWorkorderDevice(request);
    }

    public void saveResult() {
        Bundle bundle = new Bundle();
        mAddUpdateEquipmentsBean.finished = WorkorderConstant.WO_EQU_STAT_FINISHED;
        bundle.putParcelable(WORKORDER_DEVICE, mAddUpdateEquipmentsBean);
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }

    public static WorkorderDeviceEditorFragment getInstance(WorkorderService.WorkOrderEquipmentsBean device
            , Long woId, boolean add) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WORKORDER_DEVICE, device);
        bundle.putLong(WORKORDER_ID, woId);
        bundle.putBoolean(ADD_DEVICE, add);
        WorkorderDeviceEditorFragment fragment = new WorkorderDeviceEditorFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
