package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.utils.RecyclerViewUtil;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderToolsAdapter;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderToolPresenter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单-工具
 * Date: 2018/9/25 4:06 PM
 */
public class WorkorderToolFragment extends BaseFragment<WorkorderToolPresenter> implements BaseQuickAdapter.OnItemChildClickListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private RelativeLayout mRlTotal;
    private View mViewTotal;
    private TextView mTvTotal;
    private LinearLayout mLlTool;

    private static final String WORKORDER_ID = "workorder_id";
    private static final String WORKORDER_TOOLS = "workorder_tools";
    private static final String CAN_OPT = "can_opt";
    public static final String UPDATE_ADD = "update_add";
    private static final int ADD_UPDATE_CODE = 6000;

    private Long mWoId;
    private boolean mCanOpt;
    private boolean add;
    private List<WorkorderService.WorkOrderToolsBean> mToolsBeanList;
    private WorkorderToolsAdapter mAdapter;
    private int mPosition;

    @Override
    public WorkorderToolPresenter createPresenter() {
        return new WorkorderToolPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_tool_list;
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
            mToolsBeanList = arguments.getParcelableArrayList(WORKORDER_TOOLS);
        }

        if (mToolsBeanList == null) {
            mToolsBeanList = new ArrayList<>();
        }
    }

    private void initView() {
        setTitle(R.string.workorder_tool);
        if (mCanOpt) {
            setRightTextButton(R.string.workorder_add_menu, R.id.workorder_tools_add_menu_id);
        }
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRlTotal = findViewById(R.id.tool_cost_total_rl);
        mViewTotal = findViewById(R.id.tool_cost_total_view);
        mTvTotal = findViewById(R.id.tool_total_cost_tv);
        mLlTool = findViewById(R.id.ll_tool);

        mAdapter = new WorkorderToolsAdapter(mToolsBeanList, mCanOpt);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClick(new WorkorderToolsAdapter.OnItemClick() {
            @Override
            public void onBtnDelete(final WorkorderService.WorkOrderToolsBean tool, final int position) {
                new FMWarnDialogBuilder(getContext()).setIconVisible(false)
                        .setSureBluBg(true)
                        .setTitle(R.string.workorder_tip_title)
                        .setSure(R.string.workorder_confirm)
                        .setTip(R.string.workorder_delete_tool_tip_content)
                        .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, View view) {
                                mPosition = position;
                                dialog.dismiss();
                                delTool(tool.toolId);
                            }
                        }).create(R.style.fmDefaultWarnDialog).show();
            }
        });

        onRefresh();
    }

    private void delTool(Long toolId) {
        WorkorderOptService.WorkOrderToolReq req = new WorkorderOptService.WorkOrderToolReq();
        req.woId = mWoId;
        req.operateType = WorkorderConstant.WORKORDER_TOOL_DEL_OPT_TYPE;
        req.toolId = toolId;
        getPresenter().editorWorkorderTool(req);
    }

    public void refreshList() {
        mAdapter.remove(mPosition);
        onRefresh();
    }

    private void onRefresh() {
        mRlTotal.setVisibility(View.GONE);
        mViewTotal.setVisibility(View.GONE);
        mTvTotal.setText("");
        if (mToolsBeanList == null || mToolsBeanList.size() == 0) {
            RecyclerViewUtil.setHeightMatchParent(true,mRecyclerView);
            mAdapter.setEmptyView(getNoDataView(mRefreshLayout));
        } else {
            totalCost();
        }
    }

    private void totalCost() {
        mAdapter.notifyDataSetChanged();
        double total = 0;
        for (WorkorderService.WorkOrderToolsBean workOrderToolsBean : mToolsBeanList) {
            if (workOrderToolsBean.amount != null && workOrderToolsBean.cost != null) {
                total += (workOrderToolsBean.amount * workOrderToolsBean.cost);
            }
        }

        mRlTotal.setVisibility(View.VISIBLE);
        mViewTotal.setVisibility(View.VISIBLE);
        mTvTotal.setText("¥ " + StringUtils.double2String(total));
    }

    @Override
    public void onRightTextMenuClick(View view) {
        add = true;
        startForResult(WorkorderToolAddFragment.getInstance(null, mWoId), ADD_UPDATE_CODE);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (mCanOpt && mToolsBeanList != null) {
            add = false;
            startForResult(WorkorderToolAddFragment.getInstance(mToolsBeanList.get(position), mWoId), ADD_UPDATE_CODE);
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (mToolsBeanList == null || mToolsBeanList.size() == 0) {
                RecyclerViewUtil.setHeightMatchParent(false,mRecyclerView);
            }
            WorkorderService.WorkOrderToolsBean bean = data.getParcelable(UPDATE_ADD);
            if (bean == null) {
                return;
            }
            if (add) {
                mToolsBeanList.add(bean);
            }
            totalCost();
        }
    }

    public static WorkorderToolFragment getInstance(ArrayList<WorkorderService.WorkOrderToolsBean> workOrderTools
            , Long woId
            , boolean canOpt) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(WORKORDER_TOOLS, workOrderTools);
        bundle.putLong(WORKORDER_ID, woId);
        bundle.putBoolean(CAN_OPT, canOpt);
        WorkorderToolFragment fragment = new WorkorderToolFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
