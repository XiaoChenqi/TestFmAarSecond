package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.DatePickUtils;
import com.facilityone.wireless.a.arch.widget.CustomContentItemView;
import com.facilityone.wireless.a.arch.widget.EditNumberView;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderDispatchLaborerAdapter;
import com.facilityone.wireless.workorder.module.WorkorderLaborerService;
import com.facilityone.wireless.workorder.presenter.WorkorderDispatchPresenter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:派工页面
 * Date: 2018/7/17 上午10:19
 */
public class WorkorderDispatchFragment extends BaseFragment<WorkorderDispatchPresenter> implements View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private CustomContentItemView mCivStartTime;
    private CustomContentItemView mCivEndTime;
    private CustomContentItemView mCivTotalTime;
    private ImageView mIvAddLaborers;
    private RecyclerView mRvLaborers;
    private LinearLayout mLaborersLl;
    private EditNumberView desc;

    private static final int LABORER_LIST_REQUEST_CODE = 4004;
    private static final String WORKORDER_START = "workorder_start";
    private static final String WORKORDER_END = "workorder_end";
    private static final String WORKORDER_CONTENT = "workorder_content";

    private String mCode;
    private Long mWoId;
    private ArrayList<WorkorderLaborerService.WorkorderLaborerBean> mLaborers;
    private List<WorkorderLaborerService.WorkorderLaborerBean> mUploadLaborers;
    private Long startTime;
    private Long endTime;
    private WorkorderDispatchLaborerAdapter mDispatchLaborerAdapter;
    //派工预估时间和派发内容
    private String mSendContent;
    //派工负责人id
    private Long leaderId;

    @Override
    public WorkorderDispatchPresenter createPresenter() {
        return new WorkorderDispatchPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_dispatch;
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
        getPresenter().getLaborerList(mWoId);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCode = bundle.getString(WorkorderInfoFragment.WORKORDER_CODE, "");
            mSendContent = bundle.getString(WORKORDER_CONTENT, "");
            mWoId = bundle.getLong(WorkorderInfoFragment.WORKORDER_ID);
            startTime = bundle.getLong(WORKORDER_START, -1L);
            endTime = bundle.getLong(WORKORDER_END, -1L);
        }
    }

    private void initView() {
        setTitle(R.string.workorder_arrange_order);
        setRightTextButton(R.string.workorder_submit, R.id.workorder_dispatch_menu_id);

        mCivStartTime = findViewById(R.id.civ_start_time);
        mCivEndTime = findViewById(R.id.civ_end_time);
        mCivTotalTime = findViewById(R.id.civ_total_time);
        mRvLaborers = findViewById(R.id.recyclerView);
        mIvAddLaborers = findViewById(R.id.iv_add_menu);
        desc = findViewById(R.id.env_desc);
        mLaborersLl = findViewById(R.id.workorder_dispatch_laborer_ll);

        mIvAddLaborers.setOnClickListener(this);
        mCivStartTime.setOnClickListener(this);
        mCivEndTime.setOnClickListener(this);

        mCivStartTime.setTipColor(R.color.grey_6);
        mCivEndTime.setTipColor(R.color.grey_6);
        mCivTotalTime.setInputColor(R.color.grey_6);

        mUploadLaborers = new ArrayList<>();
        mDispatchLaborerAdapter = new WorkorderDispatchLaborerAdapter(mUploadLaborers);
        mDispatchLaborerAdapter.setOnItemChildClickListener(this);
        mRvLaborers.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvLaborers.setAdapter(mDispatchLaborerAdapter);

        if (startTime == -1L) {
            startTime = null;
        } else {
            mCivStartTime.setTipText(TimeUtils.millis2String(startTime, DateUtils.SIMPLE_DATE_FORMAT_ALL));
        }

        if (endTime == -1L) {
            endTime = null;
        } else {
            mCivEndTime.setTipText(TimeUtils.millis2String(endTime, DateUtils.SIMPLE_DATE_FORMAT_ALL));
        }
        desc.setDesc(mSendContent);
        setTakeTotalTime();
    }

    @Override
    public void onRightTextMenuClick(View view) {
        if (mUploadLaborers.size() == 0) {
            ToastUtils.showShort(R.string.workorder_select_laborer_tip);
            return;
        }
        showLoading();
        getPresenter().uploadDispatchData(mWoId, startTime, endTime, desc.getDesc(), mUploadLaborers);
    }


    public void setLaborers(ArrayList<WorkorderLaborerService.WorkorderLaborerBean> laborers) {
        mLaborers = laborers;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.civ_start_time) {
            long tempStartTime = startTime == null ? Calendar.getInstance().getTimeInMillis() : startTime;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(tempStartTime);
            DatePickUtils.pickDateDefaultYMDHM(getActivity(), calendar, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    if (endTime != null && date.getTime() > endTime) {
                        ToastUtils.showShort(R.string.workorder_time_start_error);
                        return;
                    }
                    startTime = date.getTime();
                    mCivStartTime.setTipText(TimeUtils.millis2String(startTime, DateUtils.SIMPLE_DATE_FORMAT_ALL));
                    setTakeTotalTime();
                }
            });
        } else if (v.getId() == R.id.civ_end_time) {
            long tempEndTime = endTime == null ? Calendar.getInstance().getTimeInMillis() : endTime;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(tempEndTime);
            DatePickUtils.pickDateDefaultYMDHM(getActivity(), calendar, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    if (startTime != null && date.getTime() < startTime) {
                        ToastUtils.showShort(R.string.workorder_time_end_error);
                        return;
                    }
                    endTime = date.getTime();
                    mCivEndTime.setTipText(TimeUtils.millis2String(endTime, DateUtils.SIMPLE_DATE_FORMAT_ALL));
                    setTakeTotalTime();
                }
            });
        } else if (v.getId() == R.id.iv_add_menu) {
            if (mLaborers == null || mLaborers.size() == 0) {
                ToastUtils.showShort(R.string.work_order_no_person);
                return;
            }
            startForResult(WorkorderLaborerListFragment.getInstance(mLaborers,getString(R.string.workorder_person)), LABORER_LIST_REQUEST_CODE);
        }
    }

    /**
     * 设置总耗时
     */
    private void setTakeTotalTime() {
        if (endTime == null || startTime == null) {
            return;
        }
        Long timeSpan = TimeUtils.getTimeSpan(endTime, startTime, TimeConstants.MIN);
        double p = timeSpan * 1.0 / 60;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//格式化设置
        String format = decimalFormat.format(p);
        mCivTotalTime.setInputText(format);
    }


    private void showLeader() {
        if (leaderId == null) {
            if (mUploadLaborers.size() > 0) {
                leaderId = mUploadLaborers.get(0).emId;
                mUploadLaborers.get(0).leader = true;
            }
        }
        //有没有负责人
        boolean have = false;
        for (WorkorderLaborerService.WorkorderLaborerBean uploadLaborer : mUploadLaborers) {
            if (uploadLaborer.emId.equals(leaderId)) {
                uploadLaborer.leader = true;
                have = true;
            } else {
                uploadLaborer.leader = false;
            }
        }

        if (!have) {
            if (mUploadLaborers.size() > 0) {
                leaderId = mUploadLaborers.get(0).emId;
                mUploadLaborers.get(0).leader = true;
            }
        }

        mDispatchLaborerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        if(view.getId() == R.id.btn_delete) {
            new FMWarnDialogBuilder(getContext())
                    .setSure(R.string.workorder_sure)
                    .setTitle(R.string.workorder_tip_title_t)
                    .setTip(R.string.workorder_delete_executor)
                    .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, View view) {
                            dialog.dismiss();
                            WorkorderLaborerService.WorkorderLaborerBean workorderLaborerBean = mUploadLaborers.get(position);
                            boolean leader = workorderLaborerBean.leader;
                            workorderLaborerBean.leader = false;
                            workorderLaborerBean.checked = false;
                            mUploadLaborers.remove(position);
                            if (leader && mUploadLaborers.size() > 0) {
                                leaderId = mUploadLaborers.get(0).emId;
                                mUploadLaborers.get(0).leader = true;
                            }
                            mDispatchLaborerAdapter.notifyDataSetChanged();
                            if(mUploadLaborers.size() > 0) {
                                mLaborersLl.setVisibility(View.VISIBLE);
                            }else {
                                mLaborersLl.setVisibility(View.GONE);
                            }
                        }
                    }).create(R.style.fmDefaultWarnDialog).show();
        }else if(view.getId() == R.id.dispatch_item_content) {
            leaderId = mUploadLaborers.get(position).emId;
            showLeader();
        }
    }




    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            for (WorkorderLaborerService.WorkorderLaborerBean laborer : mLaborers) {
                laborer.checked = false;
            }
            for (WorkorderLaborerService.WorkorderLaborerBean uploadLaborer : mUploadLaborers) {
                uploadLaborer.checked = true;
            }
            return;
        }

        List<WorkorderLaborerService.WorkorderLaborerBean> temp = new ArrayList<>();
        for (WorkorderLaborerService.WorkorderLaborerBean laborer : mLaborers) {
            if (laborer.checked) {
                temp.add(laborer);
            }
        }

        mUploadLaborers.clear();
        mUploadLaborers.addAll(temp);
        if(mUploadLaborers.size() > 0) {
            mLaborersLl.setVisibility(View.VISIBLE);
        }else {
            mLaborersLl.setVisibility(View.GONE);
        }
        showLeader();

    }

    public static WorkorderDispatchFragment getInstance(Long woId, String code, String sendWorkContent, Long estimateStartTime, Long estimateEndTime) {
        Bundle bundle = new Bundle();
        bundle.putLong(WorkorderInfoFragment.WORKORDER_ID, woId);
        bundle.putLong(WORKORDER_START, estimateStartTime);
        bundle.putLong(WORKORDER_END, estimateEndTime);
        bundle.putString(WorkorderInfoFragment.WORKORDER_CODE, code);
        bundle.putString(WORKORDER_CONTENT, sendWorkContent);
        WorkorderDispatchFragment instance = new WorkorderDispatchFragment();
        instance.setArguments(bundle);
        return instance;
    }


}
