package com.facilityone.wireless.a.arch.ec.selectdata;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.ec.module.ISelectDataService;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.ec.module.WorkTeamBean;
import com.facilityone.wireless.a.arch.ec.utils.RecyclerViewUtil;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.SearchBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工作组
 * Date: 2018/11/19 2:31 PM
 */
public class WorkTeamSelectFragment extends BaseFragment<WorkTeamSelectPresenter> implements BaseQuickAdapter.OnItemChildClickListener {

    private RecyclerView mRecyclerView;
    private SearchBox mSearchBox;

    private WorkTeamAdapter mAdapter;
    private List<WorkTeamBean> mTotal;
    private List<WorkTeamBean> mShow;

    @Override
    public WorkTeamSelectPresenter createPresenter() {
        return new WorkTeamSelectPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_arch_select_data;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        requestData();
    }

    private void initView() {
        setTitle(R.string.arch_work_team);
        mRecyclerView = findViewById(R.id.recyclerView);
        mSearchBox = findViewById(R.id.search_box);
        mSearchBox.setOnSearchBox(new SearchBox.OnSearchBox() {
            @Override
            public void onSearchTextChanged(String curCharacter) {
                //搜索
                getPresenter().filter(curCharacter, mTotal);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mShow = new ArrayList<>();
        mTotal = new ArrayList<>();
        mAdapter = new WorkTeamAdapter(mShow);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setEmptyView(getLoadingView((ViewGroup) mRecyclerView.getParent()));
    }

    private void requestData() {
        showLoading();
        getPresenter().requestWorkTeam();
    }

    public void refreshHaveData(List<WorkTeamBean> workTeamBeanList) {
        mShow.clear();
        if (workTeamBeanList != null && workTeamBeanList.size() > 0) {
            RecyclerViewUtil.setHeightMatchParent(false, mRecyclerView);
            mShow.addAll(workTeamBeanList);
            mAdapter.notifyDataSetChanged();
        } else {
            RecyclerViewUtil.setHeightMatchParent(true, mRecyclerView);
            mAdapter.setEmptyView(getNoDataView((ViewGroup) mRecyclerView.getParent()));
        }
    }

    public void error() {
        mAdapter.setEmptyView(getErrorView((ViewGroup) mRecyclerView.getParent()));
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        WorkTeamBean workTeamBean = mShow.get(position);
        SelectDataBean backBean = new SelectDataBean();
        backBean.setId(workTeamBean.workTeamId);
        backBean.setName(workTeamBean.workTeamName);
        backBean.setFullName(workTeamBean.workTeamName);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ISelectDataService.SELECT_OFFLINE_DATA_BACK, backBean);
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }

    public void setTotal(List<WorkTeamBean> total) {
        mTotal.clear();
        if (total != null) {
            mTotal.addAll(total);
        }
    }

    public static WorkTeamSelectFragment getInstance() {
        return new WorkTeamSelectFragment();
    }
}
