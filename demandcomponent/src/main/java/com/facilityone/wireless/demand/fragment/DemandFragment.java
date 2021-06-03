package com.facilityone.wireless.demand.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.FunctionAdapter;
import com.facilityone.wireless.a.arch.ec.module.FunctionService;
import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.demand.R;
import com.facilityone.wireless.demand.module.DemandConstant;
import com.facilityone.wireless.demand.presenter.DemandPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/3/29 上午10:13
 */
public class DemandFragment extends BaseFragment<DemandPresenter> implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private FunctionAdapter mFunctionAdapter;
    private List<FunctionService.FunctionBean> mFunctionBeanList;

    @Override
    public DemandPresenter createPresenter() {
        return new DemandPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_demand;
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
                ToastUtils.showShort(R.string.demand_no_function);
            }

            boolean runAlone = arguments.getBoolean(IService.COMPONENT_RUNALONE, false);
            if (runAlone) {
                setSwipeBackEnable(false);
            }

        }
    }

    private void initView() {
        setTitle(R.string.demand_demand_title);
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
        getPresenter().getUndoNumber(FunctionService.UNDO_TYPE_DEMAND);
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
            case DemandConstant.DEMAND_CREATE:
                //创建需求
                baseFragment = DemandCreateFragment.getInstance();
                break;
            case DemandConstant.DEMAND_UNCHECK:
                //待审批
                baseFragment = DemandListFragment.getInstance(DemandConstant.DEMAND_REQUEST_UNCHECK);
                break;
            case DemandConstant.DEMAND_UNFINISH:
                //待处理
                baseFragment = DemandListFragment.getInstance(DemandConstant.DEMAND_REQUES_UNFINISH);
                break;
            case DemandConstant.DEMAND_FINISH:
                //待评价
                baseFragment = DemandListFragment.getInstance(DemandConstant.DEMAND_REQUES_FINISH);
                break;
            case DemandConstant.DEMAND_QUERY:
                //查询
                baseFragment = DemandListFragment.getInstance(DemandConstant.DEMAND_REQUES_QUERY);
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

    public static DemandFragment getInstance(Bundle bundle) {
        DemandFragment demandFragment = new DemandFragment();
        demandFragment.setArguments(bundle);
        return demandFragment;
    }
}
