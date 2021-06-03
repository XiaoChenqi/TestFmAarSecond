package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.EditNumberView;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderDispatchLaborerAdapter;
import com.facilityone.wireless.workorder.module.WorkorderLaborerService;
import com.facilityone.wireless.workorder.presenter.WorkorderApprovalPresenter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:审批申请页面
 * Date: 2018/7/18 下午12:15
 */
public class WorkorderApprovalFragment extends BaseFragment<WorkorderApprovalPresenter> implements View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private ImageView mIvAddLaborers;
    private RecyclerView mRvLaborers;
    private EditNumberView desc;
    private LinearLayout mApprovalLl;

    private static final int LABORER_LIST_REQUEST_CODE = 4010;
    private ArrayList<WorkorderLaborerService.WorkorderLaborerBean> mLaborers;
    private List<WorkorderLaborerService.WorkorderLaborerBean> mUploadLaborers;
    private Long mWoId;
    private WorkorderDispatchLaborerAdapter mDispatchLaborerAdapter;

    @Override
    public WorkorderApprovalPresenter createPresenter() {
        return new WorkorderApprovalPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_approval;
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
        getPresenter().getApproverList(mWoId);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mWoId = bundle.getLong(WorkorderInfoFragment.WORKORDER_ID);
        }
    }

    private void initView() {
        setTitle(R.string.workorder_approval_title);
        setRightTextButton(R.string.workorder_submit, R.id.workorder_approval_upload_menu_id);

        mRvLaborers = findViewById(R.id.recyclerView);
        mIvAddLaborers = findViewById(R.id.iv_add_menu);
        desc = findViewById(R.id.env_desc);
        mApprovalLl = findViewById(R.id.workorder_approval_ll);

        mIvAddLaborers.setOnClickListener(this);
        mUploadLaborers = new ArrayList<>();
        mDispatchLaborerAdapter = new WorkorderDispatchLaborerAdapter(mUploadLaborers, false);
        mRvLaborers.setLayoutManager(new LinearLayoutManager(getContext()));
        mDispatchLaborerAdapter.setOnItemChildClickListener(this);
        mRvLaborers.setAdapter(mDispatchLaborerAdapter);
    }

    @Override
    public void onRightTextMenuClick(View view) {
        if (mUploadLaborers.size() == 0) {
            ToastUtils.showShort(R.string.workoder_select_approver_tip);
            return;
        }
        showLoading();
        getPresenter().uploadApprovalData(mWoId, mUploadLaborers, desc.getDesc());
    }

    @Override
    public void onClick(View v) {
        if (mLaborers == null || mLaborers.size() == 0) {
            ToastUtils.showShort(R.string.work_order_no_person);
            return;
        }
        startForResult(WorkorderLaborerListFragment.getInstance(mLaborers, getString(R.string.workorder_approval_person),false), LABORER_LIST_REQUEST_CODE);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        if(view.getId() == R.id.btn_delete) {
            new FMWarnDialogBuilder(getContext())
                    .setSure(R.string.workorder_delete)
                    .setTitle(R.string.workorder_tip_title)
                    .setTip(R.string.workorder_delete_laborers)
                    .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, View view) {
                            dialog.dismiss();
                            WorkorderLaborerService.WorkorderLaborerBean workorderLaborerBean = mUploadLaborers.get(position);
                            if (workorderLaborerBean != null) {
                                workorderLaborerBean.checked = false;
                            }
                            mDispatchLaborerAdapter.remove(position);
                            if(mUploadLaborers.size() > 0) {
                                mApprovalLl.setVisibility(View.VISIBLE);
                            }else {
                                mApprovalLl.setVisibility(View.GONE);
                            }
                        }
                    }).create(R.style.fmDefaultWarnDialog).show();
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
        for (WorkorderLaborerService.WorkorderLaborerBean b : mLaborers) {
            if (b.checked) {
                temp.add(b);
            }
        }
        mUploadLaborers.clear();
        mUploadLaborers.addAll(temp);
        mDispatchLaborerAdapter.notifyDataSetChanged();
        if(mUploadLaborers.size() > 0) {
            mApprovalLl.setVisibility(View.VISIBLE);
        }else {
            mApprovalLl.setVisibility(View.GONE);
        }
    }

    public void setLaborers(ArrayList<WorkorderLaborerService.WorkorderLaborerBean> laborers) {
        mLaborers = laborers;
    }

    public static WorkorderApprovalFragment getInstance(Long woId) {
        Bundle bundle = new Bundle();
        bundle.putLong(WorkorderInfoFragment.WORKORDER_ID, woId);
        WorkorderApprovalFragment instance = new WorkorderApprovalFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
