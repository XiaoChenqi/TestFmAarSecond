package com.facilityone.wireless.demand.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.EditNumberView;
import com.facilityone.wireless.demand.R;
import com.facilityone.wireless.demand.module.DemandService;
import com.facilityone.wireless.demand.presenter.DemandEvaluatePresenter;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:需求评价
 * Date: 2018/7/3 下午2:39
 */
public class DemandEvaluateFragment extends BaseFragment<DemandEvaluatePresenter> implements SimpleRatingBar.OnRatingBarChangeListener {

    private EditNumberView mDescEt;
    private SimpleRatingBar mBarService;
    private SimpleRatingBar mBarServiceSpeed;
    private SimpleRatingBar mBarServiceAttitude;

    private static final String DEMAND_ID = "demand_id";

    private Long mDemandId;

    @Override
    public DemandEvaluatePresenter createPresenter() {
        return new DemandEvaluatePresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_demand_evaluate;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setTitle(R.string.demand_evaluate_title);
        setRightTextButton(R.string.demand_evaluate, R.id.demand_evaluate_upload_id);
        mDescEt = findViewById(R.id.env_desc);

        mBarService = findViewById(R.id.srb_service);
        mBarServiceSpeed = findViewById(R.id.srb_speed);
        mBarServiceAttitude = findViewById(R.id.srb_attitude);

        mBarService.setOnRatingBarChangeListener(this);
        mBarServiceSpeed.setOnRatingBarChangeListener(this);
        mBarServiceAttitude.setOnRatingBarChangeListener(this);
    }

    @Override
    public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
        if(rating == 0F){
            simpleRatingBar.setRating(0.5F);
        }
    }

    private void initData() {
        if (getArguments() != null) {
            mDemandId = getArguments().getLong(DEMAND_ID, -1);
        }
    }

    @Override
    public void onRightTextMenuClick(View view) {
        showLoading();
        int service = (int) (mBarService.getRating() * 2);
        int speed = (int) (mBarServiceSpeed.getRating() * 2);
        int attitude = (int) (mBarServiceAttitude.getRating() * 2);
        DemandService.DemandEvaluateReq req = new DemandService.DemandEvaluateReq();
        req.reqId = mDemandId;
        req.quality = service;
        req.speed = speed;
        req.attitude = attitude;
        if(!TextUtils.isEmpty(mDescEt.getDesc())) {
            req.desc = mDescEt.getDesc().trim();
        }
        getPresenter().uploadEvaluate(req);
    }

    public void uploadSuccess() {
        setFragmentResult(RESULT_OK, null);
        pop();
    }

    public static DemandEvaluateFragment getInstance(Long reqId) {
        Bundle bundle = new Bundle();
        bundle.putLong(DEMAND_ID, reqId);
        DemandEvaluateFragment instance = new DemandEvaluateFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
