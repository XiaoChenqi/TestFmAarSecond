package com.facilityone.wireless.patrol.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolEquEntity;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.adapter.PatrolDeviceAdapter;
import com.facilityone.wireless.patrol.presenter.PatrolDevicePresenter;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SwipeBackLayout;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检设备
 * Date: 2018/11/8 9:37 AM
 */
public class PatrolDeviceFragment extends BaseFragment<PatrolDevicePresenter> implements
        BaseQuickAdapter.OnItemChildClickListener
        , SwipeBackLayout.OnSwipeListener {

    private RecyclerView mRecyclerView;

    private static final String PATROL_SPOT_NAME = "patrol_spot_name";
    private static final String PATROL_SPOT_ID = "patrol_spot_id";
    private static final int REQUEST_ITEM = 40001;

    private Long mSpotId;
    private String mSpotName;
    private PatrolDeviceAdapter mAdapter;
    private List<PatrolEquEntity> mEntities;
    private boolean mChange;

    @Override
    public PatrolDevicePresenter createPresenter() {
        return new PatrolDevicePresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_patrol_device;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        String title = getString(R.string.patrol_device);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mSpotName = arguments.getString(PATROL_SPOT_NAME, getString(R.string.patrol_device));
            title = mSpotName;
            mSpotId = arguments.getLong(PATROL_SPOT_ID);
        }
        setTitle(title);
        if (mSpotId == 0L) {
            pop();
            return;
        }

        mEntities = new ArrayList<>();
        mAdapter = new PatrolDeviceAdapter(mEntities);

        initView();
        getDeviceList();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
        getSwipeBackLayout().addSwipeListener(this);
    }

    public void getDeviceList() {
        showLoading();
        getPresenter().getDeviceList(mSpotId);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        startForResult(PatrolItemFragment.getInstance(mSpotId, (ArrayList<PatrolEquEntity>) mEntities, position, mSpotName), REQUEST_ITEM);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getDeviceList();
            if (data != null) {
                mChange = data.getBoolean("change");
            }
        }
    }

    public void refreshUI(List<PatrolEquEntity> entities) {
        mEntities.clear();
        if (entities != null && entities.size() > 0) {
            mEntities.addAll(entities);
        } else {
            mAdapter.setEmptyView(getNoDataView((ViewGroup) mRecyclerView.getParent()));
        }
        mAdapter.notifyDataSetChanged();
        dismissLoading();
    }

    public void error() {
        dismissLoading();
        mAdapter.setEmptyView(getErrorView((ViewGroup) mRecyclerView.getParent(), getString(R.string.patrol_get_data_error)));
    }

    @Override
    public boolean onBackPressedSupport() {
        popResult();
        return true;
    }

    @Override
    public void leftBackListener() {
        popResult();
    }

    @Override
    public void onDragStateChange(int state) {
        if (state == SwipeBackLayout.STATE_FINISHED) {
            setFragmentResult(RESULT_OK, null);
        }
    }

    @Override
    public void onEdgeTouch(int oritentationEdgeFlag) {

    }

    @Override
    public void onDragScrolled(float scrollPercent) {

    }

    public void popResult() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("change", mChange);
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }

    public static PatrolDeviceFragment getInstance(String spotName, Long spotId) {
        Bundle bundle = new Bundle();
        bundle.putLong(PATROL_SPOT_ID, spotId);
        bundle.putString(PATROL_SPOT_NAME, spotName);
        PatrolDeviceFragment instance = new PatrolDeviceFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
