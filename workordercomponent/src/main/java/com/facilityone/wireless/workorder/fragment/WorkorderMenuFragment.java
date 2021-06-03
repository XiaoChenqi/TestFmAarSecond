package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.FunctionAdapter;
import com.facilityone.wireless.a.arch.ec.collect.CollectUtils;
import com.facilityone.wireless.a.arch.ec.module.FunctionService;
import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.presenter.WorkorderMenuPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单菜单页面
 * Date: 2018/7/3 下午4:18
 */
public class WorkorderMenuFragment extends BaseFragment<WorkorderMenuPresenter> implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private FunctionAdapter mFunctionAdapter;
    private List<FunctionService.FunctionBean> mFunctionBeanList;

    @Override
    public WorkorderMenuPresenter createPresenter() {
        return new WorkorderMenuPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_menu;
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
        mFunctionBeanList = new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            ArrayList<FunctionService.FunctionBean> bean = (ArrayList<FunctionService.FunctionBean>) arguments.getSerializable(IService.FRAGMENT_CHILD_KEY);
            if (bean != null) {
                mFunctionBeanList.addAll(bean);
            } else {
                ToastUtils.showShort(R.string.workorder_no_function);
            }

            boolean runAlone = arguments.getBoolean(IService.COMPONENT_RUNALONE, false);
            if (runAlone) {
                setSwipeBackEnable(false);
            }
        }
    }

    private void initView() {
        setTitle(R.string.workorder_name_title);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), FunctionService.COUNT));
//        mRecyclerView.addItemDecoration(new GridItemDecoration(getResources().getColor(R.color.grey_d6)));

        mFunctionAdapter = new FunctionAdapter(mFunctionBeanList);
        mRecyclerView.setAdapter(mFunctionAdapter);
        mFunctionAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //获取角标
        getUndoNumber();
    }

    private void getUndoNumber() {
        getPresenter().getUndoNumber(FunctionService.UNDO_TYPE_WORK_ORDER);
    }

    public List<FunctionService.FunctionBean> getFunctionBeanList() {
        if (mFunctionBeanList == null) {
            return new ArrayList<>();
        }
        return mFunctionBeanList;
    }

    public void updateFunction(List<FunctionService.FunctionBean> functionBeanList) {
        mFunctionAdapter.replaceData(functionBeanList);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        FunctionService.FunctionBean functionBean = mFunctionBeanList.get(position);
        BaseFragment baseFragment = null;
        switch (functionBean.index) {
            case WorkorderConstant.WORKORER_CREATE:
                //创建工单
                baseFragment = WorkorderCreateFragment.getInstance();
                break;
            case WorkorderConstant.WORKORER_PROCESS:
                //待处理工单
            case WorkorderConstant.WORKORER_DISPATCHING:
                //待派工工单
            case WorkorderConstant.WORKORER_AUDIT:
                //待审批工单
            case WorkorderConstant.WORKORER_VALIDATION:
                //待验证工单
            case WorkorderConstant.WORKORER_ARCHIVE:
                //待存档工单
                baseFragment = WorkorderListFragment.getInstance(functionBean.index);
                break;
            case WorkorderConstant.WORKORER_QUERY:
                //工单查询
                baseFragment = WorkorderQueryFragment.getInstance();
                break;
        }
        if (baseFragment != null) {
            start(baseFragment);
        }
    }

    @Override
    public boolean isUseDialog() {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        CollectUtils.targetPageStart(this,"workorder");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CollectUtils.targetPageEnd(this,"workorder");
    }

    public static WorkorderMenuFragment getInstance(Bundle bundle) {
        WorkorderMenuFragment menuFragment = new WorkorderMenuFragment();
        menuFragment.setArguments(bundle);
        return menuFragment;
    }
}
