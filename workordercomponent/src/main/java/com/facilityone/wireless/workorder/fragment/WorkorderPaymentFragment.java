package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.componentservice.payment.PaymentService;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderPaymentAdapter;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderPaymentPresenter;
import com.luojilab.component.componentlib.router.Router;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: owen.
 * Date: on 2018/12/11 下午4:08.
 * Description:
 * email:
 */

public class WorkorderPaymentFragment extends BaseFragment<WorkorderPaymentPresenter> implements BaseQuickAdapter.OnItemChildClickListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private WorkorderPaymentAdapter mAdapter;

    private List<WorkorderService.PaymentsBean> mPayments;
    private boolean canEditAble = false;
    private int mWoStatus;
    private Long mWoId = null;
    private List<WorkorderService.WorkOrderLocationsBean> mWoLocationses;
    private LocationBean location;

    private static final int CREATE_PAYMENT = 1000;

    public static final String RELATE_EPAYMENT = "relate_epayment";
    public final static String WORK_ORDER_STATUS = "work_order_status";
    public final static String WORK_ORDER_ID = "work_order_id";
    public final static String WORK_LOCATION = "work_location";
    public final static String WORK_LOCATION_ID = "work_location_id";
    public final static String CAN_EDITABLE = "can_editable";

    @Override
    public WorkorderPaymentPresenter createPresenter() {
        return new WorkorderPaymentPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_payment;
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPayments = bundle.getParcelableArrayList(RELATE_EPAYMENT);
            mWoId = bundle.getLong(WORK_ORDER_ID);
            canEditAble = bundle.getBoolean(CAN_EDITABLE, false);
            mWoStatus = bundle.getInt(WORK_ORDER_STATUS, -1);
            mWoLocationses = bundle.getParcelableArrayList(WORK_LOCATION);
            location = bundle.getParcelable(WORK_LOCATION_ID);
        }
    }

    private void initView() {
        setTitle(R.string.workorder_related_payment);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new WorkorderPaymentAdapter(mPayments);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setEmptyView(getNoDataView(mRecyclerView));

        if (mWoStatus != -1 && canEditAble) {
            switch (mWoStatus) {
                case WorkorderConstant.WORK_STATUS_PROCESS:
                case WorkorderConstant.WORK_STATUS_TERMINATED:
                case WorkorderConstant.WORK_STATUS_COMPLETED:
                case WorkorderConstant.WORK_STATUS_VERIFIED:
                    setRightImageButton(R.drawable.menu_add, R.id.topbar_menu_add_id);
                    break;
                default:
                    break;

            }
        }
    }

    @Override
    public void onRightImageMenuClick(View view) {
        super.onRightImageMenuClick(view);
        if (view.getId() == R.id.topbar_menu_add_id){
            PaymentService service = (PaymentService) Router.getInstance().getService(PaymentService.class.getSimpleName());
            if (service != null) {
                BaseFragment infoFragment;
                if (mWoLocationses == null || mWoLocationses.size() == 0) {
                    infoFragment = service.getPaymentCreateFragment(mWoId, location);
                } else {
                    LocationBean locationId = new LocationBean();
                    WorkorderService.WorkOrderLocationsBean locationsBean = mWoLocationses.get(0);
                    if (locationsBean != null) {
                        locationId = locationsBean.location;
                    }
                    infoFragment = service.getPaymentCreateFragment(mWoId, locationId);
                }
                startForResult(infoFragment, CREATE_PAYMENT);
            }
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == mAdapter) {
            WorkorderService.PaymentsBean paymentsBean = mAdapter.getData().get(position);
            PaymentService service = (PaymentService) Router.getInstance().getService(PaymentService.class.getSimpleName());
            if (service != null) {
                BaseFragment infoFragment = service.getPaymentInfoFragment(paymentsBean.paymentId, paymentsBean.code, false);
                start(infoFragment);
            }
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
//            Bundle bundle = new Bundle();
//            setFragmentResult(RESULT_OK,bundle);
            getPresenter().getWorkorderInfo(mWoId);
        }
//        pop();
    }

    /**
     * 刷新缴费单列表
     * @param data
     */
    public void refreshPayment(WorkorderService.WorkorderInfoBean data) {
        mPayments.clear();
        mPayments.addAll(data.payments);
        mAdapter.notifyDataSetChanged();
    }

    public static WorkorderPaymentFragment getInstance(ArrayList<WorkorderService.PaymentsBean> payments,
                                                       LocationBean locationId,
                                                       int status,
                                                       Long woId,
                                                       boolean canOpt) {
        WorkorderPaymentFragment fragment = new WorkorderPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(RELATE_EPAYMENT, payments);
        bundle.putParcelable(WORK_LOCATION_ID, locationId);
        bundle.putLong(WORK_ORDER_ID, woId);
        bundle.putInt(WORK_ORDER_STATUS, status);
        bundle.putBoolean(CAN_EDITABLE, true);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static WorkorderPaymentFragment getInstance(ArrayList<WorkorderService.PaymentsBean> payments,
                                                       List<WorkorderService.WorkOrderLocationsBean> spaceLocation,
                                                       int status,
                                                       Long woId,
                                                       boolean canOpt) {
        WorkorderPaymentFragment fragment = new WorkorderPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(RELATE_EPAYMENT, payments);
        bundle.putParcelableArrayList(WORK_LOCATION, (ArrayList<? extends Parcelable>) spaceLocation);
        bundle.putLong(WORK_ORDER_ID, woId);
        bundle.putInt(WORK_ORDER_STATUS, status);
        bundle.putBoolean(CAN_EDITABLE, true);
        fragment.setArguments(bundle);
        return fragment;
    }

}
