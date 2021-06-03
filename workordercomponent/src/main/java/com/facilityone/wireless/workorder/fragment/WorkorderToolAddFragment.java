package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.ViewUtil;
import com.facilityone.wireless.a.arch.widget.CustomContentItemView;
import com.facilityone.wireless.a.arch.widget.EditNumberView;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderToolAddPresenter;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单-工具添加
 * Date: 2018/9/25 4:06 PM
 */
public class WorkorderToolAddFragment extends BaseFragment<WorkorderToolAddPresenter> {

    private CustomContentItemView mEtName;
    private CustomContentItemView mEtModel;
    private CustomContentItemView mEtCount;
    private CustomContentItemView mEtUnit;
    private CustomContentItemView mEtUnitPrice;
    private EditNumberView mEtMark;

    private static final String WORKORDER_ID = "workorder_id";
    private static final String WORKORDER_TOOL = "workorder_tool";

    private Long mWoId;
    private WorkorderService.WorkOrderToolsBean mToolsBean;

    @Override
    public WorkorderToolAddPresenter createPresenter() {
        return new WorkorderToolAddPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_tool_add;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initData();
        initView();
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mWoId = arguments.getLong(WORKORDER_ID);
            mToolsBean = arguments.getParcelable(WORKORDER_TOOL);
        }
    }

    private void initView() {
        if (mToolsBean != null) {
            setTitle(R.string.workorder_modify_tool);
        } else {
            setTitle(R.string.workorder_add_tool);
        }
        setRightTextButton(R.string.workorder_save, R.id.workorder_tools_save_menu_id);

        mEtName = findViewById(R.id.et_name_tool_add);
        mEtModel = findViewById(R.id.et_model_tool_add);
        mEtCount = findViewById(R.id.et_count_tool_add);
        mEtUnit = findViewById(R.id.et_unit_tool_add);
        mEtUnitPrice = findViewById(R.id.et_unit_price_tool_add);
        mEtMark = findViewById(R.id.et_desc_tool_add);

        mEtCount.getInputEt().setInputType(InputType.TYPE_CLASS_NUMBER);
        mEtUnitPrice.getInputEt().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ViewUtil.setNumberPoint(mEtUnitPrice.getInputEt(), 2);

        if (mToolsBean != null) {
            mEtName.setInputText(StringUtils.formatString(mToolsBean.name));
            mEtModel.setInputText(StringUtils.formatString(mToolsBean.model));
            mEtCount.setInputText(StringUtils.formatString(mToolsBean.amount + ""));
            mEtUnit.setInputText(StringUtils.formatString(mToolsBean.unit));
            mEtUnitPrice.setInputText(StringUtils.double2String(mToolsBean.cost));
            mEtMark.setDesc(StringUtils.formatString(mToolsBean.comment));
        }
    }

    @Override
    public void onRightTextMenuClick(View view) {
        WorkorderOptService.WorkOrderToolReq request = new WorkorderOptService.WorkOrderToolReq();
        request.operateType = WorkorderConstant.WORKORDER_TOOL_UPDATE_OPT_TYPE;
        request.woId = mWoId;
        if (mToolsBean == null) {
            mToolsBean = new WorkorderService.WorkOrderToolsBean();
            request.operateType = WorkorderConstant.WORKORDER_TOOL_ADD_OPT_TYPE;
        } else {
            request.toolId = mToolsBean.toolId;
        }
        
        String name = mEtName.getInputText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort(R.string.workorder_input_name_hint);
            return;
        }

        request.name = name;
        mToolsBean.name = name;

        String model = mEtModel.getInputText().toString();

        request.model = model;
        mToolsBean.model = model;

        String count = mEtCount.getInputText().toString();
        if (TextUtils.isEmpty(count)) {
            ToastUtils.showShort(R.string.workorder_number_hint);
            return;
        }
        Integer dCount = 0;
        try {
            dCount = Integer.valueOf(count);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        request.amount = dCount;
        mToolsBean.amount = dCount;

        String unit = mEtUnit.getInputText().toString();
        if (TextUtils.isEmpty(unit)) {
            ToastUtils.showShort(R.string.workorder_unit_hint);
            return;
        }

        request.unit = unit;
        mToolsBean.unit = unit;


        String unitPrice = mEtUnitPrice.getInputText().toString();
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

        request.cost = unitPriceD;
        mToolsBean.cost = unitPriceD;


        String mark = mEtMark.getDesc().toString();

        request.comment = mark;
        mToolsBean.comment = mark;

        getPresenter().editorWorkorderTool(request);
    }

    public void addUpdateSuccess() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WorkorderToolFragment.UPDATE_ADD, mToolsBean);
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }

    public static WorkorderToolAddFragment getInstance(WorkorderService.WorkOrderToolsBean workOrderTools
            , Long woId) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WORKORDER_TOOL, workOrderTools);
        bundle.putLong(WORKORDER_ID, woId);
        WorkorderToolAddFragment fragment = new WorkorderToolAddFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
