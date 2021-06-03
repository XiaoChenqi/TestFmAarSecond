package com.facilityone.wireless.workorder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.GridImageAdapter;
import com.facilityone.wireless.a.arch.ec.module.ISelectDataService;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.ec.module.UserService;
import com.facilityone.wireless.a.arch.ec.selectdata.SelectDataFragment;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.FMFileUtils;
import com.facilityone.wireless.a.arch.utils.PictureSelectorManager;
import com.facilityone.wireless.a.arch.widget.BottomTextListSheetBuilder;
import com.facilityone.wireless.a.arch.widget.CustomContentItemView;
import com.facilityone.wireless.a.arch.widget.EditNumberView;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.GsonUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.basiclib.widget.FullyGridLayoutManager;
import com.facilityone.wireless.componentservice.workorder.WorkorderService;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderCreateDeviceAdapter;
import com.facilityone.wireless.workorder.module.WorkorderCreateService;
import com.facilityone.wireless.workorder.presenter.WorkorderCreatePresenter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:创建工单
 * Date: 2018/7/4 上午9:20
 */
public class WorkorderCreateFragment extends BaseFragment<WorkorderCreatePresenter> implements View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, BottomTextListSheetBuilder.OnSheetItemClickListener {

    private CustomContentItemView mContactCiv;
    private CustomContentItemView mTelCiv;
    private CustomContentItemView mDepCiv;
    private CustomContentItemView mLocationCiv;
    private CustomContentItemView mServiceTypeCiv;
    private CustomContentItemView mWorkorderTypeCiv;
    private CustomContentItemView mPriorityCiv;
    private EditNumberView mNumberView;
    private ImageView mAddMenuIv;
    private RecyclerView mPhotoRv;
    private RecyclerView mDeviceRv;
    private TextView mDeviceTv;

    private static final String EQUIPMENT_ID = "equipment_id";
    private static final String FROM_TYPE = "from_type";
    private static final String LOCATION_NAME = "location_name";
    private static final String LOCATION_INFO = "location_info";
    private static final String PIC_INFO = "pic_info";
    private static final String ITEM_ID = "item_id";
    private static final String ORDER_PEOPLE = "order_people";
    private static final String ORDER_PHONE = "order_phone";
    private static final String ORDER_DESC = "order_desc";
    private static final String DEMAND_ID = "demand_id";
    private static final String WATER_MARK = "water_mark";

    private static final int MAX_PHOTO = 1000;
    private static final int REQUEST_LOCATION = 20001;
    private static final int REQUEST_DEP = 20002;
    private static final int REQUEST_SERVICE_TYPE = 20003;
    private static final int REQUEST_WORKORDER_TYPE = 20004;
    private static final int REQUEST_PRIORITY = 20005;
    private static final int REQUEST_EQU = 20006;
    //图片
    private List<LocalMedia> mSelectList;
    private GridImageAdapter mGridImageAdapter;
    private WorkorderCreateService.WorkorderCreateReq mRequest;
    //故障设备
    private List<SelectDataBean> mDevices;
    private WorkorderCreateDeviceAdapter mDeviceAdapter;

    private int mFromType;
    private long mEquipmentId;
    private LocationBean mOtherLocationBean;
    private String mOtherLocationName;
    private List<LocalMedia> mOtherMedia;
    private Long mItemId;
    private List<LocalMedia> mLocalMedia;//拍照或选择

    //需求
    private Long mDemandId;
    private String mDesc;
    private String mPhone;
    private String mPeople;
    private SelectDataBean mDepSelectData;//部门选择数据
    private SelectDataBean mServiceTypeSelectData;//服务类型选择数据
    private boolean mWaterMark;

    @Override
    public WorkorderCreatePresenter createPresenter() {
        return new WorkorderCreatePresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_create;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initRecyclerView();
        getPresenter().getUserInfo();
        initData();
    }

    private void initView() {
        String title = getString(R.string.workorder_create);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mFromType = bundle.getInt(FROM_TYPE, -1);
            mWaterMark = bundle.getBoolean(WATER_MARK, false);
            mEquipmentId = bundle.getLong(EQUIPMENT_ID, -1L);
            mItemId = bundle.getLong(ITEM_ID, -1L);
            mDemandId = bundle.getLong(DEMAND_ID, -1L);
            mOtherLocationBean = bundle.getParcelable(LOCATION_INFO);
            mOtherLocationName = bundle.getString(LOCATION_NAME, "");
            mPeople = bundle.getString(ORDER_PEOPLE, "");
            mPhone = bundle.getString(ORDER_PHONE, "");
            mDesc = bundle.getString(ORDER_DESC, "");
            mOtherMedia = bundle.getParcelableArrayList(PIC_INFO);
            if (mFromType == WorkorderService.CREATE_ORDER_BY_OTHER
                    || mFromType == WorkorderService.CREATE_ORDER_BY_PATROL_QUERY_REPAIR) {
                title = getString(R.string.workorder_report_fault);
            }
            if (mItemId == -1L) {
                mItemId = null;
            }

            if (mDemandId == -1L) {
                mDemandId = null;
            }
        }
        setTitle(title);
        setRightTextButton(R.string.workorder_submit, R.id.workorder_upload_menu_id);

        mContactCiv = findViewById(R.id.civ_contact);
        mTelCiv = findViewById(R.id.civ_tel);
        mDepCiv = findViewById(R.id.civ_dep);
        mLocationCiv = findViewById(R.id.civ_location);
        mServiceTypeCiv = findViewById(R.id.civ_service_type);
        mWorkorderTypeCiv = findViewById(R.id.civ_workorder_type);
        mPriorityCiv = findViewById(R.id.civ_priority);
        mNumberView = findViewById(R.id.env_desc);
        mAddMenuIv = findViewById(R.id.iv_add_menu);
        mPhotoRv = findViewById(R.id.rv_photo);
        mDeviceRv = findViewById(R.id.device_recyclerView);
        mDeviceTv = findViewById(R.id.device_tv);

        mDepCiv.setOnClickListener(this);
        mLocationCiv.setOnClickListener(this);
        mServiceTypeCiv.setOnClickListener(this);
        mWorkorderTypeCiv.setOnClickListener(this);
        mPriorityCiv.setOnClickListener(this);
        mAddMenuIv.setOnClickListener(this);

        mWorkorderTypeCiv.setTipText(getString(R.string.workorder_report_self));
    }

    private void initRecyclerView() {
        mSelectList = new ArrayList<>();
        mLocalMedia = new ArrayList<>();
        if (mOtherMedia != null && mOtherMedia.size() > 0) {
            mSelectList.addAll(mOtherMedia);
        }
        mGridImageAdapter = new GridImageAdapter(mSelectList, false, true, MAX_PHOTO);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getContext(),
                FullyGridLayoutManager.SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        mPhotoRv.setLayoutManager(manager);
        mPhotoRv.setAdapter(mGridImageAdapter);
        mGridImageAdapter.setOnItemChildClickListener(this);
        mGridImageAdapter.setOnItemClickListener(this);

        mDevices = new ArrayList<>();
        mDeviceAdapter = new WorkorderCreateDeviceAdapter(mDevices);
        mDeviceRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mDeviceRv.setAdapter(mDeviceAdapter);
        mDeviceAdapter.setOnItemClick(new WorkorderCreateDeviceAdapter.OnItemClick() {
            @Override
            public void onBtnDelete(final SelectDataBean device, final int position) {
                new FMWarnDialogBuilder(getContext()).setIconVisible(false)
                        .setSureBluBg(true)
                        .setTitle(R.string.workorder_tip_title)
                        .setSure(R.string.workorder_confirm)
                        .setTip(R.string.workorder_delete_device)
                        .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, View view) {
                                mDevices.remove(position);
                                mDeviceAdapter.notifyDataSetChanged();
                                if (mDevices.size() == 0) {
                                    mDeviceRv.setVisibility(View.GONE);
                                    mDeviceTv.setVisibility(View.GONE);
                                }
                                dialog.dismiss();
                            }
                        }).create(R.style.fmDefaultWarnDialog).show();
            }
        });
    }

    private void initData() {
        if (mEquipmentId != -1L && mEquipmentId != 0L) {
            getPresenter().getEquipmentFromDB(mEquipmentId);
        }

        mRequest = new WorkorderCreateService.WorkorderCreateReq();
        mRequest.woType = 0L;//工单类型默认自检
        if (mDemandId != null) {
            mRequest.woType = 0L;//需求报障默认为纠正性计划维护
            mRequest.reqId = mDemandId;
            mWorkorderTypeCiv.setTipText(getString(R.string.workorder_report_correct));
        }
        mRequest.patrolItemDetailId = mItemId;//巡检报障

        if (!TextUtils.isEmpty(mDesc)) {
            mNumberView.getDescEt().setText(mDesc);
        }


        if (!TextUtils.isEmpty(mOtherLocationName) && mOtherLocationBean != null) {
            mLocationCiv.setTipText(mOtherLocationName);
            mRequest.location = mOtherLocationBean;
        }
    }

    public void getUserInfoSuccess(String userInfo) {
        UserService.UserInfoBean userBean = null;
        if (!TextUtils.isEmpty(userInfo)) {
            userBean = GsonUtils.fromJson(userInfo, UserService.UserInfoBean.class);
        }
        if (TextUtils.isEmpty(mPeople)) {
            if (userBean != null) {
                mContactCiv.setInputText(userBean.name == null ? "" : userBean.name);
            }
        } else {
            mContactCiv.setInputText(mPeople);
        }
        if (TextUtils.isEmpty(mPhone)) {
            if (userBean != null) {
                mTelCiv.setInputText(userBean.phone == null ? "" : userBean.phone);
            }
        } else {
            mTelCiv.setInputText(mPhone);
        }
    }

    public void refreshDevice(SelectDataBean device) {
        if (device != null) {
            if (mFromType != WorkorderService.CREATE_ORDER_BY_PATROL_QUERY_REPAIR) {
                mLocationCiv.setTipText(StringUtils.formatString(device.getDesc()));
                mRequest.location = device.getLocation();
            }
            mDevices.add(device);
            mDeviceTv.setVisibility(View.VISIBLE);
            mDeviceRv.setVisibility(View.VISIBLE);
            mDeviceAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onRightTextMenuClick(View view) {
        if (prioritySelect()) {
            return;
        }

        if (TextUtils.isEmpty(mServiceTypeCiv.getTipText())) {
            ToastUtils.showShort(R.string.workorder_stype_hint);
            return;
        }

        if (TextUtils.isEmpty(mTelCiv.getInputText())) {
            ToastUtils.showShort(R.string.workorder_phone_hint);
            return;
        }

        if (TextUtils.isEmpty(mPriorityCiv.getTipText())) {
            ToastUtils.showShort(R.string.workorder_priority_hint);
            return;
        }

        showLoading();
        if (mDevices != null && mDevices.size() > 0) {
            mRequest.equipmentIds = new ArrayList<>();
            for (SelectDataBean device : mDevices) {
                mRequest.equipmentIds.add(device.getId());
            }
        }
        mRequest.userId = FM.getEmId();
        mRequest.name = mContactCiv.getInputText();
        mRequest.phone = mTelCiv.getInputText();
        mRequest.scDescription = mNumberView.getDesc();
        List<LocalMedia> temp = new ArrayList<>();
        for (LocalMedia localMedia : mSelectList) {
            if (TextUtils.isEmpty(localMedia.getSrc())) {
                temp.add(localMedia);
            }
        }

        if (mOtherMedia != null && mOtherMedia.size() > 0) {
            if (mRequest.pictures == null) {
                mRequest.pictures = new ArrayList<>();
            }
            for (LocalMedia localMedia : mOtherMedia) {
                mRequest.pictures.add(localMedia.getSrc());
            }
        }

        if (temp.size() > 0) {
            getPresenter().uploadFile(temp, mFromType);
        } else {
            getPresenter().createWorkorder(mFromType);
        }

    }

    public WorkorderCreateService.WorkorderCreateReq getRequest() {
        return mRequest;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.civ_dep) {
            startForResult(SelectDataFragment.getInstance(ISelectDataService.DATA_TYPE_DEP), REQUEST_DEP);
        } else if (view.getId() == R.id.civ_location) {
            startForResult(SelectDataFragment.getInstance(ISelectDataService.DATA_TYPE_LOCATION), REQUEST_LOCATION);
        } else if (view.getId() == R.id.civ_service_type) {
            startForResult(SelectDataFragment.getInstance(ISelectDataService.DATA_TYPE_SERVICE_TYPE), REQUEST_SERVICE_TYPE);
        } else if (view.getId() == R.id.civ_workorder_type) {
            startForResult(SelectDataFragment.getInstance(ISelectDataService.DATA_TYPE_WORKORDER_TYPE), REQUEST_WORKORDER_TYPE);
        } else if (view.getId() == R.id.iv_add_menu) {
            if (TextUtils.isEmpty(mLocationCiv.getTipText())) {
                ToastUtils.showShort(R.string.workorder_position_select_hint);
                return;
            }
            startForResult(SelectDataFragment.getInstance(ISelectDataService.DATA_TYPE_EQU
                    , mRequest.location
                    , mLocationCiv.getTipText()), REQUEST_EQU);
        } else if (view.getId() == R.id.civ_priority) {
            if (prioritySelect()) {
                return;
            }
            startForResult(SelectDataFragment.getInstance(ISelectDataService.DATA_TYPE_FLOW_PRIORITY
                    , mDepSelectData
                    , mServiceTypeSelectData
                    , mRequest.woType
                    , mRequest.location), REQUEST_PRIORITY);
        }
    }

    private boolean prioritySelect() {
        if (TextUtils.isEmpty(mLocationCiv.getTipText())) {
            ToastUtils.showShort(R.string.workorder_position_select_hint);
            return true;
        }

        if (TextUtils.isEmpty(mWorkorderTypeCiv.getTipText())) {
            ToastUtils.showShort(R.string.workorder_type_hint);
            return true;
        }

        if (TextUtils.isEmpty(mServiceTypeCiv.getTipText())) {
            ToastUtils.showShort(R.string.workorder_stype_hint);
            return true;
        }

        return false;
    }

    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
        if (view.getId() == R.id.iv_photo3) {
            new BottomTextListSheetBuilder(getContext())
                    .setShowTitle(true)
                    .setTitle(getString(R.string.workorder_select_photo_title))
                    .addItem(R.string.workorder_select_camera)
                    .addItem(R.string.workorder_select_photo)
                    .addItem(R.string.workorder_cancel)
                    .setOnSheetItemClickListener(WorkorderCreateFragment.this)
                    .build()
                    .show();
        } else if (view.getId() == R.id.ll_del) {
            new FMWarnDialogBuilder(getContext()).setIconVisible(false)
                    .setSureBluBg(true)
                    .setTitle(R.string.workorder_tip_title)
                    .setSure(R.string.workorder_confirm)
                    .setTip(R.string.workorder_delete_picture)
                    .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, View view) {
                            dialog.dismiss();
                            GridImageAdapter tempAdapter = (GridImageAdapter) adapter;
                            String path = "";
                            LocalMedia item = tempAdapter.getItem(position);
                            LocalMedia localMedia = tempAdapter.getData().get(position);

                            if (mOtherMedia != null && mOtherMedia.contains(localMedia)) {
                                mOtherMedia.remove(localMedia);
                            }

                            if (mLocalMedia != null && mLocalMedia.contains(localMedia)) {
                                mLocalMedia.remove(localMedia);
                            }

                            tempAdapter.remove(position);
//                            if (item != null && !TextUtils.isEmpty(item.getSrc())) {
//                                //Src不为空，说明图片是从其他地方传过来的
//                                tempAdapter.remove(position);
//                            } else {
//                                //src为空，说明图片是本页面添加的，有可能压缩或裁切
//                                if (item != null) {
//                                    if (item.isCut() && !item.isCompressed()) {
//                                        path = item.getCutPath();
//                                    } else if (item.isCompressed() || (item.isCut() && item.isCompressed())) {
//                                        path = item.getCompressPath();
//                                    }
//                                }
//                                tempAdapter.remove(position);
////                                if (!"".equals(path)) {
////                                    FileUtils.deleteFile(path);
////                                }
//                            }
                        }
                    }).create(R.style.fmDefaultWarnDialog).show();
        }
    }

    @Override
    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
        String projectName = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.PROJECT_NAME,"");
        String inputText = projectName + "\r\n" + mLocationCiv.getTipText();
        if (!mWaterMark) {
            inputText = null;
        }
        if (position == 0) {
            if (mSelectList.size() < MAX_PHOTO) {
                PictureSelectorManager.camera(WorkorderCreateFragment.this, PictureConfig.REQUEST_CAMERA, inputText);
            } else {
                ToastUtils.showShort(String.format(Locale.getDefault(), getString(R.string.workorder_select_photo_at_most), MAX_PHOTO));
            }
        } else if (position == 1) {
            PictureSelectorManager.MultipleChoose(WorkorderCreateFragment.this, MAX_PHOTO, mLocalMedia, PictureConfig.CHOOSE_REQUEST, inputText);
        }
        dialog.dismiss();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        PictureSelector.create(WorkorderCreateFragment.this)
                .themeStyle(R.style.picture_fm_style)
                .openExternalPreview(position, mSelectList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    mSelectList.clear();
                    mLocalMedia.clear();
                    if (mOtherMedia != null && mOtherMedia.size() > 0) {
                        mSelectList.addAll(mOtherMedia);
                    }
                    mLocalMedia.addAll(selectList);
                    mSelectList.addAll(selectList);
                    mGridImageAdapter.replaceData(mSelectList);
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    List<LocalMedia> selectCamera = PictureSelector.obtainMultipleResult(data);
                    mSelectList.addAll(selectCamera);
                    mLocalMedia.addAll(selectCamera);
                    mGridImageAdapter.replaceData(mSelectList);
                    break;
            }
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (data == null || resultCode != RESULT_OK) {
            return;
        }
        SelectDataBean bean = data.getParcelable(ISelectDataService.SELECT_OFFLINE_DATA_BACK);
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (bean == null) {
                    mLocationCiv.setTipText("");
                    mRequest.location = null;
                } else {
                    mLocationCiv.setTipText(StringUtils.formatString(bean.getFullName()));
                    mRequest.location = bean.getLocation();
                    LogUtils.d("location :" + mRequest.location.roomId
                            + "--" + mRequest.location.floorId
                            + "--" + mRequest.location.buildingId
                            + "--" + mRequest.location.siteId);
                }
                mDevices.clear();
                mDeviceRv.setVisibility(View.GONE);
                mDeviceTv.setVisibility(View.GONE);
                mDeviceAdapter.notifyDataSetChanged();
                if (mDeviceAdapter.getData().size() == 0) {
                    mDeviceTv.setVisibility(View.GONE);
                }
                setNullPriority();
                break;
            case REQUEST_DEP:
                if (bean == null) {
                    mDepSelectData = null;
                    mDepCiv.setTipText("");
                    mDepCiv.showClearIocn(false);
                    mRequest.organizationId = null;
                } else {
                    mDepSelectData = bean;
                    mDepCiv.setTipText(StringUtils.formatString(bean.getFullName()));
                    mDepCiv.showClearIocn(!TextUtils.isEmpty(bean.getFullName()));
                    mRequest.organizationId = bean.getId();
                    LogUtils.d("dep :" + mRequest.organizationId);
                }
                setNullPriority();
                break;
            case REQUEST_SERVICE_TYPE:
                if (bean == null) {
                    mServiceTypeSelectData = null;
                    mServiceTypeCiv.setTipText("");
                    mRequest.serviceTypeId = null;
                } else {
                    mServiceTypeSelectData = bean;
                    mServiceTypeCiv.setTipText(StringUtils.formatString(bean.getFullName()));
                    mRequest.serviceTypeId = bean.getId();
                    LogUtils.d("service type :" + mRequest.serviceTypeId);
                }
                setNullPriority();
                break;
            case REQUEST_WORKORDER_TYPE:
                if (bean != null) {
                    mWorkorderTypeCiv.setTipText(StringUtils.formatString(bean.getFullName()));
                    mRequest.woType = bean.getId();
                    LogUtils.d("wo type :" + mRequest.woType);
                    setNullPriority();
                }
                break;
            case REQUEST_PRIORITY:
                if (bean != null) {
                    mPriorityCiv.setTipText(StringUtils.formatString(bean.getFullName()));
                    mRequest.priorityId = bean.getId();
                    mRequest.processId = bean.getParentId();//流程id
                    LogUtils.d("priority type :" + mRequest.priorityId);
                } else {
                    setNullPriority();
                }
                break;
            case REQUEST_EQU:
                if (bean != null) {
                    for (SelectDataBean device : mDevices) {
                        if (device.getId().equals(bean.getId())) {
                            ToastUtils.showShort(R.string.workorder_equipment_exist);
                            return;
                        }
                    }
                    mDevices.add(bean);
                    mDeviceTv.setVisibility(View.VISIBLE);
                    mDeviceRv.setVisibility(View.VISIBLE);
                    mDeviceAdapter.notifyDataSetChanged();
                    LogUtils.d("device id :" + bean.getId());
                }
                break;
        }
    }

    private void setNullPriority() {
        mPriorityCiv.setTipText("");
        mRequest.priorityId = null;
        mRequest.processId = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //删除所有照片文件
        FileUtils.deleteAllInDir(FMFileUtils.getPicPath());
    }

    public static WorkorderCreateFragment getInstance() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(WATER_MARK, true);
        WorkorderCreateFragment fragment = new WorkorderCreateFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static WorkorderCreateFragment getInstance(int fromType, long equipmentId) {
        WorkorderCreateFragment fragment = new WorkorderCreateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FROM_TYPE, fromType);
        bundle.putLong(EQUIPMENT_ID, equipmentId);
        bundle.putBoolean(WATER_MARK, true);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BaseFragment getInstance(int fromType, long equipmentId, String locationName,
                                           LocationBean locationBean,
                                           List<LocalMedia> localMedias,
                                           Long itemId, String desc,
                                           Long demandId, String phone, String people) {
        return getInstance(fromType
                , equipmentId
                , locationName
                , locationBean
                , localMedias
                , itemId, desc, demandId, phone, people, true);
    }

    public static BaseFragment getInstance(int fromType, long equipmentId, String locationName,
                                           LocationBean locationBean,
                                           List<LocalMedia> localMedias,
                                           Long itemId, String desc,
                                           Long demandId, String phone, String people, boolean waterMark) {
        demandId = demandId == null ? -1L : demandId;
        itemId = itemId == null ? -1L : itemId;
        localMedias = localMedias == null ? new ArrayList<LocalMedia>() : localMedias;
        locationBean = locationBean == null ? new LocationBean() : locationBean;
        WorkorderCreateFragment fragment = new WorkorderCreateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FROM_TYPE, fromType);
        bundle.putLong(EQUIPMENT_ID, equipmentId);
        bundle.putLong(ITEM_ID, itemId);
        bundle.putLong(DEMAND_ID, demandId);
        bundle.putBoolean(WATER_MARK, waterMark);
        bundle.putString(LOCATION_NAME, StringUtils.formatString(locationName));
        bundle.putString(ORDER_DESC, StringUtils.formatString(desc));
        bundle.putString(ORDER_PHONE, StringUtils.formatString(phone));
        bundle.putString(ORDER_PEOPLE, StringUtils.formatString(people));
        bundle.putParcelable(LOCATION_INFO, locationBean);
        bundle.putParcelableArrayList(PIC_INFO, (ArrayList<? extends Parcelable>) localMedias);
        fragment.setArguments(bundle);
        return fragment;
    }
}
