package com.facilityone.wireless.patrol.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.offline.model.entity.DBPatrolConstant;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolSpotEntity;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.adapter.PatrolSpotAdapter;
import com.facilityone.wireless.patrol.module.PatrolConstant;
import com.facilityone.wireless.patrol.module.PatrolSaveReq;
import com.facilityone.wireless.patrol.presenter.PatrolSpotPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SwipeBackLayout;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检点位页面
 * Date: 2018/11/6 9:38 AM
 */
public class PatrolSpotFragment extends BaseFragment<PatrolSpotPresenter> implements
        BaseQuickAdapter.OnItemChildClickListener
        , View.OnClickListener
        , OnRefreshLoadMoreListener
        , SwipeBackLayout.OnSwipeListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private LinearLayout mLlTopMenu;
    private TextView mTvTotal;
    private TextView mTvNotSync;
    private TextView mTvUnfinish;
    private TextView mTvSync;

    private static final String PATROL_TASK_ID = "patrol_task_id";
    private static final String PATROL_TASK_NAME = "patrol_task_name";
    private static final String PATROL_SCAN = "patrol_scan";
    private static final int REQUEST_DEVICE = 30001;

    private List<PatrolSpotEntity> mShowEntities;
    private List<PatrolSpotEntity> mTotalEntities;
    private List<PatrolSpotEntity> mCompletedEntities;
    private List<PatrolSpotEntity> mUncompletedEntities;
    private PatrolSpotAdapter mAdapter;
    private int mMenuId;
    private Long mTaskId;
    private PatrolSaveReq mPatrolSaveReq;
    private String mFromScan;
    private int mNeedNfc;

    @Override
    public PatrolSpotPresenter createPresenter() {
        return new PatrolSpotPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_patrol_spot;
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
        String title = getString(R.string.patrol_spot);
        Bundle arguments = getArguments();
        if (arguments != null) {
            title = arguments.getString(PATROL_TASK_NAME, getString(R.string.patrol_spot));
            mTaskId = arguments.getLong(PATROL_TASK_ID);
            mFromScan = arguments.getString(PATROL_SCAN, "");
        }
        setTitle(title);
        if (mTaskId == 0L) {
            pop();
            return;
        }
        initView();
        getSpotList();
        mNeedNfc = SPUtils.getInstance(SPKey.SP_MODEL_PATROL).getInt(SPKey.PATROL_NEED_NFC, 0);
    }

    private void initView() {

        setRightTextButton(R.string.patrol_submit, R.id.patrol_spot_upload_id);

        mRefreshLayout = findViewById(R.id.refreshLayout);
        mLlTopMenu = findViewById(R.id.ll_top);
        mTvTotal = findViewById(R.id.all_tag_tv);
        mMenuId = R.id.all_tag_tv;
        mTvNotSync = findViewById(R.id.not_sync_tv);
        mTvUnfinish = findViewById(R.id.unfinish_tv);
        mTvSync = findViewById(R.id.spot_sync_tv);
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mShowEntities = new ArrayList<>();
        mTotalEntities = new ArrayList<>();
        mCompletedEntities = new ArrayList<>();
        mUncompletedEntities = new ArrayList<>();
        mAdapter = new PatrolSpotAdapter(mShowEntities);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(this);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        mTvTotal.setOnClickListener(this);
        mTvNotSync.setOnClickListener(this);
        mTvUnfinish.setOnClickListener(this);
        mTvSync.setOnClickListener(this);
        getSwipeBackLayout().addSwipeListener(this);
    }


    private void getSpotList() {
        showLoading();
        getPresenter().getSpotList(mTaskId);
    }

    @Override
    public void onRightTextMenuClick(View view) {
        syncDta(PatrolConstant.PATROL_OPT_TYPE_UPLOAD);
    }

    public void error() {
        mLlTopMenu.setVisibility(View.GONE);
        mRefreshLayout.finishRefresh(false);
        mAdapter.setEmptyView(getErrorView((ViewGroup) mRecyclerView.getParent(), R.string.patrol_get_data_error));
        dismissLoading();
    }

    @SuppressLint("DefaultLocale")
    public void refreshUI(List<PatrolSpotEntity> spotEntities) {
        mTotalEntities.clear();
        mShowEntities.clear();
        mCompletedEntities.clear();
        mUncompletedEntities.clear();
        if (spotEntities != null && spotEntities.size() > 0) {
            mLlTopMenu.setVisibility(View.VISIBLE);
            mTotalEntities.addAll(spotEntities);

            for (PatrolSpotEntity totalEntity : mTotalEntities) {
                if (totalEntity.getRemoteCompleted() == DBPatrolConstant.TRUE_VALUE || totalEntity.getCompleted() == DBPatrolConstant.TRUE_VALUE) {
                    mCompletedEntities.add(totalEntity);
                } else {
                    mUncompletedEntities.add(totalEntity);
                }
            }
        } else {
            mLlTopMenu.setVisibility(View.VISIBLE);
            mAdapter.setEmptyView(getNoDataView((ViewGroup) mRecyclerView.getParent()));
        }
        mTvTotal.setText(String.format(getString(R.string.patrol_task_spot_all) + "(%d)", mTotalEntities.size()));
        mTvNotSync.setText(String.format(getString(R.string.patrol_task_spot_finish) + "(%d)", mCompletedEntities.size()));
        mTvUnfinish.setText(String.format(getString(R.string.patrol_task_spot_unfinish) + "(%d)", mUncompletedEntities.size()));

        refreshAll(mMenuId, true);

        mAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh();
        dismissLoading();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        PatrolSpotEntity patrolSpotEntity = mShowEntities.get(position);
        //如果远程完成了也不需要扫描二维码 加上  || patrolSpotEntity.getRemoteCompleted() == DBPatrolConstant.TRUE_VALUE

        if (mNeedNfc == PatrolConstant.PATROL_NEED_NFC && patrolSpotEntity.getCompleted() != DBPatrolConstant.TRUE_VALUE) {
            ToastUtils.showShort(R.string.patrol_spot_operate_tip);
        } else {
            String code = patrolSpotEntity.getCode();
            if ((!TextUtils.isEmpty(mFromScan) && !TextUtils.isEmpty(code) && mFromScan.equals(code))
                    || patrolSpotEntity.getCompleted() == DBPatrolConstant.TRUE_VALUE
                    || patrolSpotEntity.getRemoteCompleted() == DBPatrolConstant.TRUE_VALUE) {
                startForResult(PatrolDeviceFragment.getInstance(patrolSpotEntity.getName(), patrolSpotEntity.getPatrolSpotId()), REQUEST_DEVICE);
            } else {
                //需要扫描二维码
                getPresenter().scan(patrolSpotEntity);
            }
        }
    }

    /**
     * 扫描结果匹配
     *
     * @param patrolSpotEntity
     */
    public void scanResult(PatrolSpotEntity patrolSpotEntity) {
        startForResult(PatrolDeviceFragment.getInstance(patrolSpotEntity.getName(), patrolSpotEntity.getPatrolSpotId()), REQUEST_DEVICE);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getSpotList();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.all_tag_tv) {
            refreshAll(id, false);
        } else if (id == R.id.not_sync_tv) {
            refreshAll(id, false);
        } else if (id == R.id.unfinish_tv) {
            refreshAll(id, false);
        } else if (id == R.id.spot_sync_tv) {
            syncDta(PatrolConstant.PATROL_OPT_TYPE_SYNC);
        }
    }

    private void refreshAll(int id, boolean canClick) {
        if (!canClick) {
            mTvTotal.setTextColor(getResources().getColor(R.color.green_1ab394));
            mTvNotSync.setTextColor(getResources().getColor(R.color.green_1ab394));
            mTvUnfinish.setTextColor(getResources().getColor(R.color.green_1ab394));
            mTvTotal.setBackgroundResource(R.drawable.fm_patrol_left_empty_bg);
            mTvNotSync.setBackgroundResource(R.drawable.fm_patrol_center_empty_bg);
            mTvUnfinish.setBackgroundResource(R.drawable.fm_patrol_right_empty_bg);
        }

        if (id == R.id.all_tag_tv && (mMenuId != R.id.all_tag_tv || canClick)) {
            mTvTotal.setTextColor(getResources().getColor(R.color.white));
            mTvTotal.setBackgroundResource(R.drawable.fm_patrol_left_fill_bg);
            mMenuId = id;
            mShowEntities.clear();
            mShowEntities.addAll(mTotalEntities);
        } else if (id == R.id.not_sync_tv && (mMenuId != R.id.not_sync_tv || canClick)) {
            mTvNotSync.setTextColor(getResources().getColor(R.color.white));
            mTvNotSync.setBackgroundResource(R.drawable.fm_patrol_center_fill_bg);
            mMenuId = id;
            mShowEntities.clear();
            mShowEntities.addAll(mCompletedEntities);
        } else if (id == R.id.unfinish_tv && (mMenuId != R.id.unfinish_tv || canClick)) {
            mTvUnfinish.setTextColor(getResources().getColor(R.color.white));
            mTvUnfinish.setBackgroundResource(R.drawable.fm_patrol_right_fill_bg);
            mMenuId = id;
            mShowEntities.clear();
            mShowEntities.addAll(mUncompletedEntities);
        }

        mRecyclerView.scrollToPosition(0);
        mAdapter.setEmptyView(getNoDataView((ViewGroup) mRecyclerView.getParent()));
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 同步数据
     */
    private void syncDta(int operateType) {
        mPatrolSaveReq = new PatrolSaveReq();
        mPatrolSaveReq.operateType = operateType;
        mPatrolSaveReq.userId = FM.getEmId();
        getPresenter().syncData(mPatrolSaveReq, mTotalEntities, operateType, mTaskId);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getSpotList();
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

    public void popResult() {
        setFragmentResult(RESULT_OK, null);
        pop();
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

    public void refresh() {
        mAdapter.notifyDataSetChanged();
    }

    public Long getTaskId() {
        return mTaskId;
    }

    public PatrolSaveReq getPatrolSaveReq() {
        return mPatrolSaveReq;
    }

    public static PatrolSpotFragment getInstance(Long taskId, String name) {
        return getInstance(taskId, name, null);
    }

    public static PatrolSpotFragment getInstance(Long taskId, String name, String scan) {
        Bundle bundle = new Bundle();
        bundle.putLong(PATROL_TASK_ID, taskId);
        bundle.putString(PATROL_TASK_NAME, name);
        bundle.putString(PATROL_SCAN, scan);
        PatrolSpotFragment instance = new PatrolSpotFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
