package com.facilityone.wireless.patrol.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.adapter.PatrolQuerySpotAdapter;
import com.facilityone.wireless.patrol.module.PatrolQueryService;
import com.facilityone.wireless.patrol.presenter.PatrolQuerySpotPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检查询点位页面
 * Date: 2018/11/21 9:38 AM
 */
public class PatrolQuerySpotFragment extends BaseFragment<PatrolQuerySpotPresenter> implements BaseQuickAdapter.OnItemChildClickListener {

    private TextView mTvRepair;
    private TextView mTvMiss;
    private TextView mTvException;
    private TextView mTvPeople;
    private TextView mTvPlan;
    private TextView mTvPlanTime;
    private TextView mTvRealTime;
    private RecyclerView mRecyclerView;
    private PatrolQuerySpotAdapter mAdapter;
    private List<MultiItemEntity> mList;

    private static final String PATROL_TASK_ID = "patrol_task_id";
    private static final String PATROL_TASK_NAME = "patrol_task_name";
    private Long mTaskId;
    private String mTitle;
    private Boolean mReadonly;

    @Override
    public PatrolQuerySpotPresenter createPresenter() {
        return new PatrolQuerySpotPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_patrol_query_spot;
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
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTaskId = arguments.getLong(PATROL_TASK_ID, -1L);
            mTitle = arguments.getString(PATROL_TASK_NAME);
        }

        if (mTaskId == null || mTaskId == -1L) {
            pop();
            return;
        }

        initView();
        getPresenter().requestData(mTaskId);
    }

    private void initView() {
        setTitle(StringUtils.formatString(mTitle, getString(R.string.patrol_task)));

        mTvRepair = findViewById(R.id.repair_tv);
        mTvMiss = findViewById(R.id.miss_tv);
        mTvException = findViewById(R.id.exception_tv);
        mTvPeople = findViewById(R.id.people_tv);
        mTvPlan = findViewById(R.id.plan_tv);
        mTvPlanTime = findViewById(R.id.plan_time_tv);
        mTvRealTime = findViewById(R.id.real_time_tv);
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mList = new ArrayList<>();
        mAdapter = new PatrolQuerySpotAdapter(mList);
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void refreshUI(PatrolQueryService.PatrolQuerySpotResp data) {
        mReadonly = data.readonly;
        mTvRepair.setText(data.repairNumber == null ? "0" : data.repairNumber + "");
        mTvMiss.setText(data.leakNumber == null ? "0" : data.leakNumber + "");
        mTvException.setText(data.exceptionNumber == null ? "0" : data.exceptionNumber + "");

        mTvPeople.setText(StringUtils.formatString(data.laborer));
        mTvPlan.setText(StringUtils.formatString(data.period));
        StringBuilder sb = new StringBuilder();
        if (data.dueStartDateTime != null) {
            String dst = TimeUtils.millis2String(data.dueStartDateTime, DateUtils.SIMPLE_DATE_FORMAT_ALL);
            sb.append(dst);
        }

        if (data.dueStartDateTime != null || data.dueEndDateTime != null) {
            sb.append("~");
        }

        if (data.dueEndDateTime != null) {
            String det = TimeUtils.millis2String(data.dueEndDateTime, DateUtils.SIMPLE_DATE_FORMAT_ALL);
            sb.append(det);
        }
        mTvPlanTime.setText(sb.toString());

        StringBuilder sb2 = new StringBuilder();
        if (data.actualStartDateTime != null) {
            String ast = TimeUtils.millis2String(data.actualStartDateTime, DateUtils.SIMPLE_DATE_FORMAT_ALL);
            sb2.append(ast);
        }

        if (data.actualStartDateTime != null || data.actualEndDateTime != null) {
            sb2.append("~");
        }

        if (data.actualEndDateTime != null) {
            String aet = TimeUtils.millis2String(data.actualEndDateTime, DateUtils.SIMPLE_DATE_FORMAT_ALL);
            sb2.append(aet);
        }

        mTvRealTime.setText(sb2.toString());

        //数据库查询

        getPresenter().delData(data.spots);
    }

    public void refAdapter(List<MultiItemEntity> list) {
        mList.clear();
        if (list != null) {
            mList.addAll(list);
        }
        mAdapter.notifyDataSetChanged();
        dismissLoading();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Object o = adapter.getData().get(position);
        if (o instanceof PatrolQueryService.EquipmentBean) {
            PatrolQueryService.EquipmentBean bean = (PatrolQueryService.EquipmentBean) o;
            LogUtils.d("equ name :" + bean.name);
            start(PatrolQueryEquFragment.getInstance(mTaskId, bean.spotId, bean.eqId, mReadonly, bean.locationBean, bean.spotLocationName));
        }
    }

    public static PatrolQuerySpotFragment getInstance(Long taskId, String title) {
        Bundle bundle = new Bundle();
        bundle.putLong(PATROL_TASK_ID, taskId);
        bundle.putString(PATROL_TASK_NAME, title);
        PatrolQuerySpotFragment instance = new PatrolQuerySpotFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
