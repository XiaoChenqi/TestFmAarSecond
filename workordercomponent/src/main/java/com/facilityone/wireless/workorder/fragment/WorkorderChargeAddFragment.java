package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.ViewUtil;
import com.facilityone.wireless.a.arch.widget.CustomContentItemView;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderChargeAddPresenter;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单-检查项添加
 * Date: 2018/9/25 4:06 PM
 */
public class WorkorderChargeAddFragment extends BaseFragment<WorkorderChargeAddPresenter> {

    private CustomContentItemView mEtName;
    private CustomContentItemView mEtCost;

    private static final String WORKORDER_ID = "workorder_id";
    private static final String WORKORDER_CHARGE = "workorder_charge";

    private Long mWoId;
    private WorkorderService.ChargesBean mChargesBean;

    @Override
    public WorkorderChargeAddPresenter createPresenter() {
        return new WorkorderChargeAddPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_charge_add;
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
            mChargesBean = arguments.getParcelable(WORKORDER_CHARGE);
        }
    }

    private void initView() {
        if (mChargesBean != null) {
            setTitle(R.string.workorder_edit_cost);
        } else {
            setTitle(R.string.workorder_add_cost);
        }
        setRightTextButton(R.string.workorder_save, R.id.workorder_charges_save_menu_id);

        mEtName = findViewById(R.id.et_name_charge_add);
        mEtCost = findViewById(R.id.et_cost_charge_add);

        mEtCost.getInputEt().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ViewUtil.setNumberPoint(mEtCost.getInputEt(), 2);

        if (mChargesBean != null) {
            mEtName.setInputText(StringUtils.formatString(mChargesBean.name));
            mEtCost.setInputText(StringUtils.double2String(mChargesBean.amount));
        }
    }

    @Override
    public void onRightTextMenuClick(View view) {
        WorkorderOptService.WorkOrderChargeReq request = new WorkorderOptService.WorkOrderChargeReq();
        request.operateType = WorkorderConstant.WORKORDER_CHARGE_UPDATE_OPT_TYPE;
        request.woId = mWoId;
        if (mChargesBean == null) {
            mChargesBean = new WorkorderService.ChargesBean();
            request.operateType = WorkorderConstant.WORKORDER_CHARGE_ADD_OPT_TYPE;
        } else {
            request.chargeId = mChargesBean.chargeId;
        }

        String name = mEtName.getInputText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort(R.string.workorder_input_name_hint);
            return;
        }

        request.name = name;
        mChargesBean.name = name;


        String unitPrice = mEtCost.getInputText().toString();
        if (TextUtils.isEmpty(unitPrice)) {
            ToastUtils.showShort(R.string.workorder_input_amount_hint);
            return;
        }
        Double unitPriceD = 0D;
        try {
            unitPriceD = Double.valueOf(unitPrice);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        request.amount = unitPriceD;
        mChargesBean.amount = unitPriceD;


        getPresenter().editorWorkorderCharge(request);
    }

    public void addUpdateSuccess() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WorkorderToolFragment.UPDATE_ADD, mChargesBean);
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }

    public static WorkorderChargeAddFragment getInstance(WorkorderService.ChargesBean chargesBean
            , Long woId) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WORKORDER_CHARGE, chargesBean);
        bundle.putLong(WORKORDER_ID, woId);
        WorkorderChargeAddFragment fragment = new WorkorderChargeAddFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
