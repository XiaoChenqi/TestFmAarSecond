package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderChargesAdapter;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderChargePresenter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单-收取明细
 * Date: 2018/9/25 4:06 PM
 */
public class WorkorderChargeFragment extends BaseFragment<WorkorderChargePresenter> implements BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private RelativeLayout mRlTotal;
    private RelativeLayout mRlChargeTool;
    private View mViewTotal;
    private TextView mTvTotal;
    private LinearLayout mLlCharge;
    private TextView mToolCostTv;
    private CheckBox mCheckBox;

    private static final String WORKORDER_ID = "workorder_id";
    private static final String WORKORDER_CHARGES = "workorder_charges";
    private static final String WORKORDER_TOOL = "workorder_tool";
    private static final String CAN_OPT = "can_opt";
    public static final String UPDATE_ADD = "update_add";
    private static final int ADD_UPDATE_CODE = 7000;

    private Long mWoId;
    private boolean mCanOpt;
    private boolean add;
    private List<WorkorderService.ChargesBean> mChargesBeanList;
    private WorkorderService.ChargesBean mChargesTool;
    private WorkorderChargesAdapter mAdapter;
    private int mPosition;
    private boolean chargeToolAdd;

    @Override
    public WorkorderChargePresenter createPresenter() {
        return new WorkorderChargePresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_charge_list;
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
            mChargesBeanList = arguments.getParcelableArrayList(WORKORDER_CHARGES);
            mChargesTool = arguments.getParcelable(WORKORDER_TOOL);
        }

        if (mChargesBeanList == null) {
            mChargesBeanList = new ArrayList<>();
        }

        if (mChargesTool == null) {
            mChargesTool = new WorkorderService.ChargesBean();
            mChargesTool.name = getString(R.string.workorder_tool_fee);
            mChargesTool.amount = 0D;
            mChargesTool.chargeId = -1L;
        }
    }

    private void initView() {
        setTitle(R.string.workorder_charge_title);
        if (mCanOpt) {
            setRightTextButton(R.string.workorder_add_menu, R.id.workorder_charges_add_menu_id);
        }
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRlTotal = findViewById(R.id.charge_cost_total_rl);
        mRlChargeTool = findViewById(R.id.charge_tool_rl);
        mViewTotal = findViewById(R.id.charge_cost_total_view);
        mTvTotal = findViewById(R.id.charge_total_cost_tv);
        mLlCharge = findViewById(R.id.ll_charge);
        mCheckBox = findViewById(R.id.charge_tool_cb);
        mToolCostTv = findViewById(R.id.tool_charge_cost_tv);

//        chargeToolAdd = true;
//        mCheckBox.setChecked(chargeToolAdd);
//        mToolCostTv.setVisibility(View.VISIBLE);
//        mToolCostTv.setText("¥" + StringUtils.double2String(mChargesTool.amount));

        mAdapter = new WorkorderChargesAdapter(mChargesBeanList, mCanOpt);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mRlChargeTool.setOnClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClick(new WorkorderChargesAdapter.OnItemClick() {
            @Override
            public void onBtnDelete(final WorkorderService.ChargesBean tool, final int position) {
                new FMWarnDialogBuilder(getContext()).setIconVisible(false)
                        .setSureBluBg(true)
                        .setTitle(R.string.workorder_tip_title)
                        .setSure(R.string.workorder_confirm)
                        .setTip(R.string.workorder_delete_item)
                        .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, View view) {
                                mPosition = position;
                                dialog.dismiss();
                                delCharge(tool.chargeId);
                            }
                        }).create(R.style.fmDefaultWarnDialog).show();
            }
        });

        onRefresh();
    }

    private void delCharge(Long chargeId) {
        WorkorderOptService.WorkOrderChargeReq req = new WorkorderOptService.WorkOrderChargeReq();
        req.woId = mWoId;
        req.operateType = WorkorderConstant.WORKORDER_CHARGE_DEL_OPT_TYPE;
        req.chargeId = chargeId;
        getPresenter().editorWorkorderCharge(req);
    }

    public void refreshList() {
        mAdapter.remove(mPosition);
        onRefresh();
    }

    private void onRefresh() {
        totalCost();
    }

    private void totalCost() {
        mAdapter.notifyDataSetChanged();
        if (mChargesBeanList.size() == 0 && !chargeToolAdd) {
            mRlTotal.setVisibility(View.GONE);
        } else {
            mRlTotal.setVisibility(View.VISIBLE);
        }
        double total = 0;
        for (WorkorderService.ChargesBean chargesBean : mChargesBeanList) {
            if (chargesBean.amount != null) {
                total += chargesBean.amount;
            }
        }

        if(chargeToolAdd) {
            total += mChargesTool.amount;
        }

        mViewTotal.setVisibility(View.VISIBLE);
        mTvTotal.setText("¥ " + StringUtils.double2String(total));
    }

    @Override
    public void onRightTextMenuClick(View view) {
        add = true;
        startForResult(WorkorderChargeAddFragment.getInstance(null, mWoId), ADD_UPDATE_CODE);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (mCanOpt && mChargesBeanList != null) {
            WorkorderService.ChargesBean chargesBean = mChargesBeanList.get(position);
            if (chargesBean.chargeId == null || chargesBean.chargeId != -1L) {
                add = false;
                startForResult(WorkorderChargeAddFragment.getInstance(chargesBean, mWoId), ADD_UPDATE_CODE);
            }
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            WorkorderService.ChargesBean bean = data.getParcelable(UPDATE_ADD);
            if (bean == null) {
                return;
            }
            if (add) {
//                boolean tool = false;
//                if (mChargesBeanList.size() > 0) {
//                    WorkorderService.ChargesBean chargesBean = mChargesBeanList.get(mChargesBeanList.size() - 1);
//                    if (chargesBean.chargeId != null && chargesBean.chargeId == -1L) {
//                        tool = true;
//                    }
//                }
//                if (tool) {
//                    mChargesBeanList.add(mChargesBeanList.size() - 1, bean);
//                } else {
//                    mChargesBeanList.add(bean);
//                }
                mChargesBeanList.add(bean);
            }
            totalCost();
        }
    }

    @Override
    public void onClick(View v) {
        chargeToolAdd = !chargeToolAdd;
        mCheckBox.setChecked(chargeToolAdd);
        if (chargeToolAdd) {
//            mChargesBeanList.add(mChargesTool);
            mToolCostTv.setVisibility(View.VISIBLE);
            mToolCostTv.setText("¥ " + StringUtils.double2String(mChargesTool.amount));
        } else {
//            mChargesBeanList.remove(mChargesTool);
            mToolCostTv.setVisibility(View.GONE);
        }

        totalCost();

    }

    @Override
    public void onDestroyView() {
        if (mChargesBeanList.contains(mChargesTool)) {
            mChargesBeanList.remove(mChargesTool);
        }
        super.onDestroyView();
    }

    public static WorkorderChargeFragment getInstance(ArrayList<WorkorderService.ChargesBean> chargesBeen
            , Long woId
            , WorkorderService.ChargesBean tool
            , boolean canOpt) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(WORKORDER_CHARGES, chargesBeen);
        bundle.putParcelable(WORKORDER_TOOL, tool);
        bundle.putLong(WORKORDER_ID, woId);
        bundle.putBoolean(CAN_OPT, canOpt);
        WorkorderChargeFragment fragment = new WorkorderChargeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
