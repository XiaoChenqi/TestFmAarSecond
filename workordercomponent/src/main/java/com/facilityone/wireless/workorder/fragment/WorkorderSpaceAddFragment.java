package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.ec.module.ISelectDataService;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.ec.selectdata.SelectDataFragment;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.CustomContentItemView;
import com.facilityone.wireless.a.arch.widget.EditNumberView;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderSpaceAddPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:添加修改空间位置
 * Date: 2018/9/29 10:16 AM
 */
public class WorkorderSpaceAddFragment extends BaseFragment<WorkorderSpaceAddPresenter> implements View.OnClickListener {

    private CustomContentItemView mEtLocation;
    private EditNumberView mEtDealWay;

    public static final String WORKORDER_LOCATION = "workorder_location";
    public static final String WORKORDER_LOCATION_S = "workorder_location_s";
    private static final String WORKORDER_ID = "workorder_id";
    private static final int REQUEST_LOCATION = 30012;

    private Long mWoId;
    private boolean mAdd;
    private WorkorderService.WorkOrderLocationsBean mLocationsBean;
    private List<WorkorderService.WorkOrderLocationsBean> mLocationsBeans;
    private LocationBean mAddLocationsBean;

    @Override
    public WorkorderSpaceAddPresenter createPresenter() {
        return new WorkorderSpaceAddPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_space_update;
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
        Bundle arguments = getArguments();
        if (arguments != null) {
            mWoId = arguments.getLong(WORKORDER_ID);
            mLocationsBean = arguments.getParcelable(WORKORDER_LOCATION);
            mLocationsBeans = arguments.getParcelableArrayList(WORKORDER_LOCATION_S);
        }
    }

    private void initView() {
        mEtLocation = findViewById(R.id.space_location_civ);
        mEtDealWay = findViewById(R.id.space_deal_way_env);

        if (mLocationsBean != null) {
            mAdd = false;
            setTitle(StringUtils.formatString(mLocationsBean.locationName));
            mEtLocation.setVisibility(View.GONE);
            mEtDealWay.setDesc(StringUtils.formatString(mLocationsBean.repairDesc));
        } else {
            mLocationsBean = new WorkorderService.WorkOrderLocationsBean();
            mAdd = true;
            setTitle(R.string.workorder_position_add_title);
        }
        setRightTextButton(R.string.workorder_save, R.id.workorder_space_save_menu_id);
        mEtLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startForResult(SelectDataFragment.getInstance(ISelectDataService.DATA_TYPE_LOCATION), REQUEST_LOCATION);
    }

    @Override
    public void onRightTextMenuClick(View view) {
        WorkorderOptService.WorkOrderSpaceReq request = new WorkorderOptService.WorkOrderSpaceReq();
        request.woId = mWoId;
        request.location = mAddLocationsBean;
        request.repairDesc = mEtDealWay.getDesc();
        if (mAdd) {
            request.operateType = WorkorderConstant.WORKORDER_SPACE_ADD_OPT_TYPE;
            if (TextUtils.isEmpty(mEtLocation.getTipText().toString()) || mAddLocationsBean == null) {
                ToastUtils.showShort(R.string.workorder_position_select_hint_tip);
                return;
            }
            mLocationsBean.locationName = mEtLocation.getTipText().toString();
            mLocationsBean.location = request.location;
            mLocationsBean.repairDesc = request.repairDesc;
        } else {
            request.operateType = WorkorderConstant.WORKORDER_SPACE_UPDATE_OPT_TYPE;
            if (mLocationsBean != null) {
                request.recordId = mLocationsBean.recordId;
                request.location = mLocationsBean.location;
                mLocationsBean.repairDesc = request.repairDesc;
            }
        }

        if (request.location != null && mLocationsBeans != null) {
            Long siteId = request.location.siteId;
            Long buildingId = request.location.buildingId;
            Long floorId = request.location.floorId;
            Long roomId = request.location.roomId;
            boolean equalsLocation = false;
            for (WorkorderService.WorkOrderLocationsBean locationsBean : mLocationsBeans) {
                if (locationsBean.location != null) {
                    Long siteTId = locationsBean.location.siteId;
                    Long buildingTId = locationsBean.location.buildingId;
                    Long floorTId = locationsBean.location.floorId;
                    Long roomTId = locationsBean.location.roomId;
                    boolean equalsLocationRoom = false;
                    boolean equalsLocationFloor = false;
                    boolean equalsLocationBuilding = false;
                    boolean equalsLocationSite = false;
                    if (roomId == null && roomTId == null) {
                        equalsLocationRoom = true;
                    }
                    if (roomId != null && roomTId != null && roomId.equals(roomTId)) {
                        equalsLocationRoom = true;
                    }

                    if (floorId == null && floorTId == null) {
                        equalsLocationFloor = true;
                    }
                    if (floorId != null && floorTId != null && floorId.equals(floorTId)) {
                        equalsLocationFloor = true;
                    }

                    if (buildingId == null && buildingTId == null) {
                        equalsLocationBuilding = true;
                    }
                    if (buildingId != null && buildingTId != null && buildingId.equals(buildingTId)) {
                        equalsLocationBuilding = true;
                    }

//                    if (siteId == null && siteTId == null) {
//                        equalsLocationSite = true;
//                    }
//
//                    if (siteId != null && siteTId != null && siteId.equals(siteTId)) {
//                        equalsLocationSite = true;
//                    }

                    if (equalsLocationRoom && equalsLocationFloor && equalsLocationBuilding) {
                        equalsLocation = true;
                        break;
                    }
                }
            }

            if (mAdd && equalsLocation) {
                ToastUtils.showShort(R.string.workorder_position_add_again);
                return;
            }
        }

        getPresenter().editorWorkorderSpace(request);
    }

    public void saveResult(Long recordId) {
        if(recordId != null) {
            mLocationsBean.recordId = recordId;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(WORKORDER_LOCATION, mLocationsBean);
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        SelectDataBean bean = data.getParcelable(ISelectDataService.SELECT_OFFLINE_DATA_BACK);
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (bean == null) {
                    mEtLocation.setTipText("");
                    mAddLocationsBean = null;
                } else {
                    mEtLocation.setTipText(StringUtils.formatString(bean.getFullName()));
                    mAddLocationsBean = bean.getLocation();
                }
                break;
        }
    }

    public static WorkorderSpaceAddFragment getInstance(WorkorderService.WorkOrderLocationsBean l
            , Long woId, List<WorkorderService.WorkOrderLocationsBean> locations) {
        if (locations == null) {
            locations = new ArrayList<>();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(WORKORDER_LOCATION, l);
        bundle.putParcelableArrayList(WORKORDER_LOCATION_S, (ArrayList<? extends Parcelable>) locations);
        bundle.putLong(WORKORDER_ID, woId);
        WorkorderSpaceAddFragment fragment = new WorkorderSpaceAddFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
