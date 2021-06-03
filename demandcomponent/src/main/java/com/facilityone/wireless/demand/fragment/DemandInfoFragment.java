package com.facilityone.wireless.demand.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.AttachmentAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.AudioAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.GridImageAdapter;
import com.facilityone.wireless.a.arch.ec.audio.AudioPlayConnection;
import com.facilityone.wireless.a.arch.ec.audio.AudioPlayManager;
import com.facilityone.wireless.a.arch.ec.audio.AudioPlayService;
import com.facilityone.wireless.a.arch.ec.module.AttachmentBean;
import com.facilityone.wireless.a.arch.ec.module.ISelectDataService;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.ec.module.OrdersBean;
import com.facilityone.wireless.a.arch.ec.module.RequesterBean;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.ec.selectdata.SelectDataFragment;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.offline.dao.BuildingDao;
import com.facilityone.wireless.a.arch.offline.dao.CityDao;
import com.facilityone.wireless.a.arch.offline.dao.FloorDao;
import com.facilityone.wireless.a.arch.offline.dao.RoomDao;
import com.facilityone.wireless.a.arch.offline.dao.SiteDao;
import com.facilityone.wireless.a.arch.utils.UrlUtils;
import com.facilityone.wireless.a.arch.widget.BottomTextListSheetBuilder;
import com.facilityone.wireless.a.arch.widget.FMBottomInputSheetBuilder;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.a.arch.widget.PhoneMenuBuilder;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.FMThreadUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.basiclib.video.SimplePlayer;
import com.facilityone.wireless.basiclib.widget.FullyGridLayoutManager;
import com.facilityone.wireless.componentservice.workorder.WorkorderService;
import com.facilityone.wireless.demand.R;
import com.facilityone.wireless.demand.adapter.DemandRecordAdapter;
import com.facilityone.wireless.demand.adapter.DemandRelatedOrderAdapter;
import com.facilityone.wireless.demand.module.DemandConstant;
import com.facilityone.wireless.demand.module.DemandHelper;
import com.facilityone.wireless.demand.module.DemandService;
import com.facilityone.wireless.demand.presenter.DemandInfoPresenter;
import com.joanzapata.iconify.widget.IconTextView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luojilab.component.componentlib.router.Router;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/6/28 下午6:07
 */
public class DemandInfoFragment extends BaseFragment<DemandInfoPresenter> implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener, AudioAdapter.onRemoveAudioListener, BottomTextListSheetBuilder.OnSheetItemClickListener, FMBottomInputSheetBuilder.OnInputBtnClickListener {

    private int mType;
    private Long mDemandId;
    private LinearLayout mInputLL;
    private LinearLayout mExpandLL;
    private IconTextView mExpandArrow;
    private TextView mTvRequester;
    private TextView mTvType;
    private TextView mTvStatus;
    private TextView mTvLocation;
    private TextView mTvReserveTime;
    private LinearLayout mLlReserveTime;
    private IconTextView mCall;
    private LinearLayout mCallLl;
    private TextView mTvDesc;
    private TextView mTvOrigin;
    private RecyclerView mRvPhoto;
    private RecyclerView mRvVideo;
    private RecyclerView mRvAudio;
    private LinearLayout mLLMedia;

    private View mRecodeTopView;
    private View mRecodeBottomView;
    private LinearLayout mLLRecord;
    private RecyclerView mRvRecord;

    private LinearLayout mOrdersLL;
    private View mOrderTopView;
    private View mOrderBottomView;
    private RecyclerView mRvOrders;

    private LinearLayout mAttachmentLL;
    private View mAttachmentTopView;
    private View mAttachmentBottomView;
    private RecyclerView mRvAttachments;

    private TextView mEditServiceTypeTv;

    private GridImageAdapter mGridImageAdapter;
    private List<LocalMedia> mLocalMedias;
    private List<LocalMedia> tem;

    private static final String LIST_TYPE = "list_type";
    private static final String DEMAND_ID = "demand_id";
    private static final String DEMAND_FROM_MSG = "demand_from_msg";
    private static final int DEMAND_EVALUATE = 30001;
    private static final int REQUEST_CREATE_ORDER = 3003;
    private static final int REQUEST_DEMAND_TYPE = 3004;

    //音频
    private List<LocalMedia> mAudioSelectList;
    private AudioAdapter mAudioAdapter;
    //视频
    private List<LocalMedia> mVideoSelectList;
    private GridImageAdapter mVideoGridImageAdapter;

    //关联工单
    private List<OrdersBean> mOrdersBeen;
    private DemandRelatedOrderAdapter mOrdersAdapter;

    //附件
    private List<AttachmentBean> mAttachmentList;
    private AttachmentAdapter mAttachmentAdapter;

    //历史记录
    private List<DemandService.RecordsBean> mRecordsBeanList;
    private DemandRecordAdapter mDemandRecordAdapter;

    private String tel;
    private boolean orderIn;
    private DemandService.DemandInfoBean mData;
    private Long mReqTypeId;//新的需求类型 ID
    private boolean notQuery;
    private boolean fromMsg;

    @Override
    public DemandInfoPresenter createPresenter() {
        return new DemandInfoPresenter();
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_demand_info;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initMedia();
        getDemandInfo();
        requestPermission();
    }

    private void requestPermission() {
        PermissionUtils.permission(PermissionConstants.STORAGE,
                PermissionConstants.MICROPHONE,
                PermissionConstants.PHONE)
                .request();
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt(LIST_TYPE);
            fromMsg = bundle.getBoolean(DEMAND_FROM_MSG, false);
            mDemandId = bundle.getLong(DEMAND_ID);
        }
    }

    private void initView() {
        if (mType != DemandConstant.DEMAND_REQUES_QUERY) {
            notQuery = true;
            setMoreMenu();
        } else {
            notQuery = false;
        }

        if (fromMsg) {
            notQuery = true;
            setMoreMenu();
        }

        mInputLL = findViewById(R.id.ll_input);
        mExpandLL = findViewById(R.id.ll_expend);
        mExpandArrow = findViewById(R.id.itv_expand);

        mTvRequester = findViewById(R.id.tv_requester);
        mTvType = findViewById(R.id.tv_type);
        mTvStatus = findViewById(R.id.tv_status);
        mTvLocation = findViewById(R.id.tv_location);
        mTvReserveTime = findViewById(R.id.tv_reserve_time);
        mLlReserveTime = findViewById(R.id.ll_reserve_time);
        mCall = findViewById(R.id.itv_call);
        mCallLl = findViewById(R.id.demand_info_call_ll);
        mTvDesc = findViewById(R.id.tv_desc);
        mTvOrigin = findViewById(R.id.tv_origin);
        mRvPhoto = findViewById(R.id.rv_photo);
        mRvVideo = findViewById(R.id.rv_video);
        mRvAudio = findViewById(R.id.rv_audio);
        mLLMedia = findViewById(R.id.ll_media);

        mOrdersLL = findViewById(R.id.ll_relation_orders);
        mOrderTopView = findViewById(R.id.view_relation_orders_top);
        mOrderBottomView = findViewById(R.id.view_relation_orders_bottom);
        mRvOrders = findViewById(R.id.rv_relation_orders);

        mAttachmentLL = findViewById(R.id.ll_attachment);
        mAttachmentTopView = findViewById(R.id.view_attachment_top);
        mAttachmentBottomView = findViewById(R.id.view_attachment_bottom);
        mRvAttachments = findViewById(R.id.rv_attachment);

        mRecodeTopView = findViewById(R.id.view_solve_record_top);
        mRecodeBottomView = findViewById(R.id.view_solve_record_bottom);
        mLLRecord = findViewById(R.id.ll_solve_record);
        mRvRecord = findViewById(R.id.rv_record);

        mEditServiceTypeTv = findViewById(R.id.service_type_edit_tv);

        mRvAttachments.setNestedScrollingEnabled(false);
        mRvPhoto.setNestedScrollingEnabled(false);
        mRvVideo.setNestedScrollingEnabled(false);
        mRvAudio.setNestedScrollingEnabled(false);
        mRvOrders.setNestedScrollingEnabled(false);
        mRvRecord.setNestedScrollingEnabled(false);

        mInputLL.setOnClickListener(this);
        mExpandLL.setOnClickListener(this);
        mCallLl.setOnClickListener(this);
        mEditServiceTypeTv.setOnClickListener(this);

        if (mType == DemandConstant.DEMAND_REQUES_UNFINISH) {
            mInputLL.setVisibility(View.VISIBLE);
            mEditServiceTypeTv.setVisibility(View.VISIBLE);
        } else if (mType == DemandConstant.DEMAND_REQUEST_UNCHECK) {
            mEditServiceTypeTv.setVisibility(View.VISIBLE);
        }
    }

    private void initMedia() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getContext(),
                FullyGridLayoutManager.SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        mRvPhoto.setLayoutManager(manager);
        mLocalMedias = new ArrayList<>();
        tem = new ArrayList<>();
        mGridImageAdapter = new GridImageAdapter(mLocalMedias, true);
        mGridImageAdapter.setOnItemClickListener(this);
        mRvPhoto.setAdapter(mGridImageAdapter);

        FullyGridLayoutManager manager2 = new FullyGridLayoutManager(getContext(),
                FullyGridLayoutManager.SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);

        mRvVideo.setLayoutManager(manager2);
        mVideoSelectList = new ArrayList<>();
        mVideoGridImageAdapter = new GridImageAdapter(mVideoSelectList, false, true);
        mVideoGridImageAdapter.setOnItemClickListener(this);
        mRvVideo.setAdapter(mVideoGridImageAdapter);

        mAudioSelectList = new ArrayList<>();
        mAudioAdapter = new AudioAdapter(mAudioSelectList, true, getContext());
        mAudioAdapter.setOnRemoveAudioListener(this);
        mRvAudio.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvAudio.setAdapter(mAudioAdapter);

        mOrdersBeen = new ArrayList<>();
        mOrdersAdapter = new DemandRelatedOrderAdapter(mOrdersBeen);
        mOrdersAdapter.setOnItemClickListener(this);
        mRvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvOrders.setAdapter(mOrdersAdapter);

        mAttachmentList = new ArrayList<>();
        mAttachmentAdapter = new AttachmentAdapter(mAttachmentList);
        mAttachmentAdapter.setOnItemClickListener(this);
        mRvAttachments.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvAttachments.setAdapter(mAttachmentAdapter);

        mRecordsBeanList = new ArrayList<>();
        mDemandRecordAdapter = new DemandRecordAdapter(mRecordsBeanList);
        mRvRecord.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvRecord.setAdapter(mDemandRecordAdapter);
    }

    public void getDemandInfo() {
        getPresenter().getDemandInfo(mDemandId);
    }

    @Override
    public void onMoreMenuClick(View view) {
        if (fromMsg) {
            if (mData != null && mData.status != null) {
                switch (mData.status) {
                    case DemandConstant.DEMAND_STATUS_CREATED://待审批需求
                        BottomTextListSheetBuilder builder2 = new BottomTextListSheetBuilder(getContext());
                        builder2.addItem(R.string.demand_approval_title)
                                .addItem(R.string.demand_cancel)
                                .setOnSheetItemClickListener(DemandInfoFragment.this)
                                .build().show();
                        break;
                    case DemandConstant.DEMAND_STATUS_PROGRESS://待处理需求
                        BottomTextListSheetBuilder builder = new BottomTextListSheetBuilder(getContext());
                        if (!orderIn) {
                            builder.addItem(R.string.demand_generate_work_order);
                        }
                        builder.addItem(R.string.demand_finish)
                                .addItem(R.string.demand_cancel)
                                .setOnSheetItemClickListener(DemandInfoFragment.this)
                                .build().show();

                        break;
                    case DemandConstant.DEMAND_STATUS_COMPLETED://待评价需求
                        BottomTextListSheetBuilder builder3 = new BottomTextListSheetBuilder(getContext());
                        builder3.addItem(R.string.demand_atisfaction)
                                .addItem(R.string.demand_cancel)
                                .setOnSheetItemClickListener(DemandInfoFragment.this)
                                .build().show();
                        break;
                }
            }
        } else {
            switch (mType) {
                case DemandConstant.DEMAND_REQUEST_UNCHECK://待审批需求
                    BottomTextListSheetBuilder builder2 = new BottomTextListSheetBuilder(getContext());
                    builder2.addItem(R.string.demand_approval_title)
                            .addItem(R.string.demand_cancel)
                            .setOnSheetItemClickListener(DemandInfoFragment.this)
                            .build().show();
                    break;
                case DemandConstant.DEMAND_REQUES_UNFINISH://待处理需求
                    BottomTextListSheetBuilder builder = new BottomTextListSheetBuilder(getContext());
                    if (!orderIn) {
                        builder.addItem(R.string.demand_generate_work_order);
                    }
                    builder.addItem(R.string.demand_finish)
                            .addItem(R.string.demand_cancel)
                            .setOnSheetItemClickListener(DemandInfoFragment.this)
                            .build().show();
                    break;
                case DemandConstant.DEMAND_REQUES_FINISH://待评价需求
                    BottomTextListSheetBuilder builder3 = new BottomTextListSheetBuilder(getContext());
                    builder3.addItem(R.string.demand_atisfaction)
                            .addItem(R.string.demand_cancel)
                            .setOnSheetItemClickListener(DemandInfoFragment.this)
                            .build().show();
                    break;
            }
        }
    }

    public void updateUI(DemandService.DemandInfoBean data) {
        mData = data;
        setTitle(data.code);
        if (data.requester != null && data.createDate != null) {
            Date date = new Date(data.createDate);
            mTvRequester.setText(String.format(getString(R.string.demand_create_by),
                    data.requester.name,
                    TimeUtils.date2String(date, DateUtils.SIMPLE_DATE_FORMAT_ALL)));
        }

        updateTag(data);
        mTvType.setText(StringUtils.formatString(data.type));
        updateOrigin(data);
        if (!TextUtils.isEmpty(data.locationName)) {
            mTvLocation.setText(StringUtils.formatString(data.locationName, ""));
        } else if (data.location != null) {
            String tempName = "";
            if (data.location.roomId != null && data.location.roomId != 0L) {
                RoomDao dao = new RoomDao();
                tempName = dao.queryLocationName(data.location.roomId);
            } else if (data.location.floorId != null && data.location.floorId != 0L) {
                FloorDao dao = new FloorDao();
                tempName = dao.queryLocationName(data.location.floorId);
            } else if (data.location.buildingId != null && data.location.buildingId != 0L) {
                BuildingDao dao = new BuildingDao();
                tempName = dao.queryLocationName(data.location.buildingId);
            } else if (data.location.siteId != null && data.location.siteId != 0L) {
                SiteDao dao = new SiteDao();
                tempName = dao.queryLocationName(data.location.siteId);
            } else if (data.location.cityId != null && data.location.cityId != 0L) {
                CityDao dao = new CityDao();
                tempName = dao.queryLocationName(data.location.cityId);
            }
            mTvLocation.setText(StringUtils.formatString(tempName, ""));
        }
        if(data.reserveStartTime != null || data.reserveEndTime != null) {
            mLlReserveTime.setVisibility(View.VISIBLE);
            StringBuffer stringBuffer = new StringBuffer("");
            if(data.reserveStartTime != null) {
                stringBuffer.append(TimeUtils.date2String(new Date(data.reserveStartTime), DateUtils.SIMPLE_DATE_FORMAT_ALL));
            }
            stringBuffer.append("~");
            if(data.reserveEndTime != null) {
                stringBuffer.append(TimeUtils.date2String(new Date(data.reserveEndTime), DateUtils.SIMPLE_DATE_FORMAT_ALL));
            }
            mTvReserveTime.setText(stringBuffer.toString());
        }else {
            mLlReserveTime.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(data.telephone)) {
            tel = data.telephone;
            mCall.setVisibility(View.VISIBLE);
        }
        mTvDesc.setText(StringUtils.formatString(data.desc));
        updateMedia(data);
        updateOrders(data);
        updateRecords(data);

    }

    private void updateRecords(DemandService.DemandInfoBean data) {
        if (data.records != null && data.records.size() > 0) {
            mRecordsBeanList.clear();
            mRecordsBeanList.addAll(data.records);
            mDemandRecordAdapter.notifyDataSetChanged();
            showOrHideRecords(true);
        } else {
            showOrHideRecords(false);
        }
    }

    private void updateOrders(DemandService.DemandInfoBean data) {
        if (data.orders != null && data.orders.size() > 0) {
            mOrdersBeen.clear();
            mOrdersBeen.addAll(data.orders);
            mOrdersAdapter.notifyDataSetChanged();
            showOrHideOrders(true);
        } else {
            showOrHideOrders(false);
        }
    }

    private void updateTag(DemandService.DemandInfoBean data) {
        boolean hasPermission = permission(data);
        if (data.status != null) {
            int resId = R.drawable.demand_fill_blue_background;
            mTvStatus.setVisibility(View.VISIBLE);
            mTvStatus.setText(DemandHelper.getDemandStatusMap(getContext()).get(data.status));
            boolean showMoreMenu = true;
            switch (data.status) {
                case DemandConstant.DEMAND_STATUS_CREATED:
                    resId = R.drawable.demand_fill_created_bg;
                    break;
                case DemandConstant.DEMAND_STATUS_PROGRESS:
                    resId = R.drawable.demand_fill_progress_bg;
                    break;
                case DemandConstant.DEMAND_STATUS_COMPLETED:
                    resId = R.drawable.demand_fill_completed_bg;
                    break;
                case DemandConstant.DEMAND_STATUS_EVALUATED:
                    showMoreMenu = false;
                    resId = R.drawable.demand_fill_evaluated_bg;
                    break;
                case DemandConstant.DEMAND_STATUS_CANCEL:
                    showMoreMenu = false;
                    resId = R.drawable.demand_fill_cancel_bg;
                    break;
            }
            mTvStatus.setBackgroundResource(resId);

            hideShowMoreMenu(showMoreMenu & notQuery && hasPermission);

            if (fromMsg) {
                mInputLL.setVisibility(View.GONE);
                mEditServiceTypeTv.setVisibility(View.GONE);
                if (data.status == DemandConstant.DEMAND_STATUS_PROGRESS && hasPermission) {
                    mInputLL.setVisibility(View.VISIBLE);
                    mEditServiceTypeTv.setVisibility(View.VISIBLE);
                } else if (data.status == DemandConstant.DEMAND_STATUS_CREATED && hasPermission) {
                    mEditServiceTypeTv.setVisibility(View.VISIBLE);
                }
            }

        } else {
            mTvStatus.setVisibility(View.GONE);
            hideShowMoreMenu(false);
        }
    }

    private boolean permission(DemandService.DemandInfoBean data) {
        boolean hasPermission = false;
        if (data.status != null && data.currentRoles != null && data.currentRoles.size() > 0) {
            switch (data.status) {
                case DemandConstant.DEMAND_STATUS_CREATED://待审批需求
                    hasPermission = data.currentRoles.contains(DemandConstant.DEMAND_RULES_SP);
                    break;
                case DemandConstant.DEMAND_STATUS_PROGRESS://待处理需求
                    hasPermission = data.currentRoles.contains(DemandConstant.DEMAND_RULES_DEL);
                    break;
                case DemandConstant.DEMAND_STATUS_COMPLETED://待评价需求
                    hasPermission = data.currentRoles.contains(DemandConstant.DEMAND_RULES_PJ);
                    break;
            }
        }

        return hasPermission;
    }

    private void updateOrigin(DemandService.DemandInfoBean data) {
        if (data.origin != null) {
            String origin = "";
            switch (data.origin) {
                case DemandConstant.DEMAND_ORIGIN_WEB:
                    origin = getString(R.string.demand_web);
                    break;
                case DemandConstant.DEMAND_ORIGIN_APP:
                    origin = getString(R.string.demand_phone);
                    break;
                case DemandConstant.DEMAND_ORIGIN_WECHAT:
                    origin = getString(R.string.demand_wechat);
                    break;
                case DemandConstant.DEMAND_ORIGIN_EMAIL:
                    origin = getString(R.string.demand_email);
                    break;
                case DemandConstant.DEMAND_ORIGIN_DINGDING:
                    origin = getString(R.string.demand_dingding);
                    break;
            }
            mTvOrigin.setText(origin);
        }
    }

    private void updateMedia(DemandService.DemandInfoBean data) {
        if ((data.videos != null && data.videos.size() > 0)
                || (data.audios != null && data.audios.size() > 0)
                || (data.images != null && data.images.size() > 0)) {
            mExpandArrow.setVisibility(View.VISIBLE);
        }

        image(data);
        audio(data);
        video(data);
        attachment(data);
    }

    private void attachment(DemandService.DemandInfoBean data) {
        if (data.attachment != null && data.attachment.size() > 0) {
            mAttachmentList.clear();
            showOrHideAttachments(true);
            for (AttachmentBean attachmentBean : data.attachment) {
                attachmentBean.url = UrlUtils.getAttachmentPath(attachmentBean.src);
            }
            mAttachmentList.addAll(data.attachment);
            mAttachmentAdapter.notifyDataSetChanged();
        } else {
            showOrHideAttachments(false);
        }
    }

    private void video(final DemandService.DemandInfoBean data) {
        if (data.videos != null && data.videos.size() > 0) {
            mVideoSelectList.clear();
            FMThreadUtils.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final List<LocalMedia> tt = new ArrayList<>();
                    for (String video : data.videos) {
                        String mediaPath = UrlUtils.getMediaPath(video);
                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setDuration(UrlUtils.getRingDuring(mediaPath));
                        localMedia.setPath(mediaPath);
                        localMedia.setPictureType("video/mp4");
                        localMedia.setMimeType(PictureMimeType.ofAudio());
                        tt.add(localMedia);
                    }
                    FM.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mVideoSelectList.addAll(tt);
                            mVideoGridImageAdapter.notifyDataSetChanged();
                            mRvVideo.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        } else {
            mRvVideo.setVisibility(View.GONE);
        }
    }

    private void audio(final DemandService.DemandInfoBean data) {
        if (data.audios != null && data.audios.size() > 0) {
            mAudioSelectList.clear();
            initAudioPlayService();
            FMThreadUtils.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final List<LocalMedia> t = new ArrayList<>();
                    for (String audio : data.audios) {
                        String mediaPath = UrlUtils.getMediaPath(audio);
                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setDuration(UrlUtils.getRingDuring(mediaPath));
                        localMedia.setPath(mediaPath);
                        localMedia.setPictureType("audio/amr");
                        localMedia.setMimeType(PictureMimeType.ofAudio());
                        t.add(localMedia);
                    }

                    FM.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mAudioSelectList.addAll(t);
                            mAudioAdapter.notifyDataSetChanged();
                            mRvAudio.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        } else {
            mRvAudio.setVisibility(View.GONE);
        }
    }

    private void image(DemandService.DemandInfoBean data) {
        if (data.images != null && data.images.size() > 0) {
            tem.clear();
            mLocalMedias.clear();
            mRvPhoto.setVisibility(View.VISIBLE);
            for (String image : data.images) {
                LocalMedia media = new LocalMedia();
                media.setPath(UrlUtils.getImagePath(image));
                media.setDuration(data.images.size());
                media.setPictureType(PictureMimeType.JPEG);
                tem.add(media);
            }
            if (tem.size() > FullyGridLayoutManager.SPAN_COUNT) {
                List<LocalMedia> localMedias = tem.subList(0, FullyGridLayoutManager.SPAN_COUNT);
                mLocalMedias.addAll(localMedias);
            } else {
                if (tem.size() == FullyGridLayoutManager.SPAN_COUNT) {
                    LocalMedia localMedia = tem.get(FullyGridLayoutManager.SPAN_COUNT - 1);
                    localMedia.setDuration(-1L);
                }
                mLocalMedias.addAll(tem);
            }
            mGridImageAdapter.notifyDataSetChanged();
        } else {
            mRvPhoto.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.ll_input) {
            FMBottomInputSheetBuilder builder = new FMBottomInputSheetBuilder(getContext());
            builder.setOnSaveInputListener(this);
            QMUIBottomSheet build = builder.build();
            builder.getSingleBtn().setVisibility(View.VISIBLE);
            builder.setTitle(R.string.demand_job_content);
            builder.setDescHint(R.string.demand_input_job_content_p);
            builder.setBtnText(R.string.demand_save);
            builder.setShowTip(getString(R.string.demand_input_job_content));
            build.show();

        } else if (viewId == R.id.ll_expend) {
            //展开
            Boolean tag = (Boolean) v.getTag();
            if (tag == null) {
                tag = false;
            }
            v.setTag(!tag);
            expand(!tag);
        } else if (viewId == R.id.demand_info_call_ll) {
            if (TextUtils.isEmpty(tel)) {
                return;
            }
            final String[] split = tel.split("/");
            PhoneMenuBuilder builder = new PhoneMenuBuilder(getContext());
            builder.addItems(split, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PhoneUtils.dial(split[which]);
                    dialog.dismiss();
                }
            });
            builder.create(R.style.fmDefaultDialog).show();
        } else if (viewId == R.id.service_type_edit_tv) {
            //修改服务类型
            startForResult(SelectDataFragment.getInstance(ISelectDataService.DATA_TYPE_DEMAND_TYPE), REQUEST_DEMAND_TYPE);
        }
    }

    //显示和隐藏工单
    private void showOrHideOrders(boolean show) {
        orderIn = show;
        mOrdersLL.setVisibility(show ? View.VISIBLE : View.GONE);
        mOrderTopView.setVisibility(show ? View.VISIBLE : View.GONE);
        mOrderBottomView.setVisibility(show ? View.VISIBLE : View.GONE);
        mRvOrders.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    //记录
    private void showOrHideRecords(boolean show) {
        mRecodeTopView.setVisibility(show ? View.VISIBLE : View.GONE);
        mRecodeBottomView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLLRecord.setVisibility(show ? View.VISIBLE : View.GONE);
        mRvRecord.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    //附件
    private void showOrHideAttachments(boolean show) {
        mAttachmentLL.setVisibility(show ? View.VISIBLE : View.GONE);
        mAttachmentTopView.setVisibility(show ? View.VISIBLE : View.GONE);
        mAttachmentBottomView.setVisibility(show ? View.VISIBLE : View.GONE);
        mRvAttachments.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void expand(Boolean expand) {
        mExpandArrow.setText(expand ? R.string.icon_arrow_up : R.string.icon_arrow_down);
        mLLMedia.setVisibility(expand ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == mGridImageAdapter) {
            if (tem != null && tem.size() > 4) {
                position = 0;
            }
            PictureSelector.create(DemandInfoFragment.this)
                    .themeStyle(R.style.picture_fm_style)
                    .openExternalPreview(position, tem);
        } else if (adapter == mVideoGridImageAdapter) {
            // 预览视频
            LocalMedia localMedia = mVideoSelectList.get(position);
            SimplePlayer.startActivity(this, localMedia.getPath());
        } else if (adapter == mOrdersAdapter) {
            OrdersBean ordersBean = mOrdersBeen.get(position);
            WorkorderService workorderService = (WorkorderService) Router.getInstance().getService(WorkorderService.class.getSimpleName());
            if (workorderService != null) {
                BaseFragment workorderCreateFragment = workorderService.getWorkorderInfoFragment(-1, ordersBean.code, ordersBean.woId);
                start(workorderCreateFragment);
            }
        } else if (adapter == mAttachmentAdapter) {
            AttachmentBean attachmentBean = mAttachmentList.get(position);
            getPresenter().openAttachment(attachmentBean.url, attachmentBean.name, getContext());
        }
    }

    @Override
    public void onRemove(int position) {

    }

    @Override
    public void onAudioClick(ImageView imageView, String path, int position) {
        if (AudioPlayManager.getAudioPlayService() != null) {
            AudioPlayManager.getAudioPlayService().setImageView(imageView);
            AudioPlayManager.getAudioPlayService().stopPlayVoiceAnimation();
            AudioPlayManager.getAudioPlayService().play(path);
        }
    }

    private void initAudioPlayService() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), AudioPlayService.class);
        AudioPlayConnection audioPlayConnection = new AudioPlayConnection();
        getActivity().bindService(intent, audioPlayConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
        if (tag.equals(getString(R.string.demand_generate_work_order))) {
            WorkorderService workorderService = (WorkorderService) Router.getInstance().getService(WorkorderService.class.getSimpleName());
            if (workorderService != null) {
                if (mData != null) {
                    LocationBean location = mData.location;
                    String locationName = mData.locationName;
                    List<String> images = mData.images;
                    String desc = mData.desc;
                    RequesterBean requester = mData.requester;
                    if (requester == null || requester.name == null) {
                        ToastUtils.showShort(R.string.demand_no_demand_person);
                        return;
                    }

                    List<LocalMedia> localMedias = new ArrayList<>();
                    if (images != null && images.size() > 0) {
                        for (String image : images) {
                            LocalMedia media = new LocalMedia();
                            media.setPath(UrlUtils.getImagePath(image));
//                            media.setDuration(image);
                            localMedias.add(media);
                        }
                    }
                    SiteDao siteDao = new SiteDao();
                    List<SelectDataBean> locationSites = siteDao.queryLocationSites();
                    if (locationSites != null && locationSites.size() > 0) {
                        SelectDataBean locationSite = locationSites.get(0);
                        String locationSiteName = locationSite.getName();
                        if (TextUtils.isEmpty(locationName)) {
                            locationName = locationSiteName;
                        } else {
                            locationName = locationSiteName + "/" + locationName;
                        }
                        if (location == null) {
                            location = new LocationBean();
                            location.siteId = locationSite.getId();
                        } else {
                            location.siteId = locationSite.getId();
                        }
                    }


                    startForResult(workorderService.getWorkorderCreateFragment(WorkorderService.CREATE_ORDER_BY_OTHER
                            , -1L, locationName, location, null, null, desc, mDemandId, mData.telephone, requester.name), REQUEST_CREATE_ORDER);

                } else {
                    ToastUtils.showShort(R.string.demand_data_error);
                }
            }

        } else if (tag.equals(getString(R.string.demand_finish))) {
            if (mData != null && mData.orders != null && mData.orders.size() > 0) {
                for (OrdersBean order : mData.orders) {
                    if (!(order.status == DemandConstant.WORK_STATUS_VERIFIED || order.status == DemandConstant.WORK_STATUS_ARCHIVED)) {
                        ToastUtils.showShort(R.string.demand_relate_work_order_error_tip);
                        dialog.dismiss();
                        return;
                    }
                }
            }
            showLoading();
            DemandService.DemandOptReq request = new DemandService.DemandOptReq();
            request.reqId = mDemandId;
            request.operateType = DemandConstant.DEMAND_OPT_TYPE_FINISH;
            getPresenter().optDemand(request);
        } else if (tag.equals(getString(R.string.demand_atisfaction))) {
            startForResult(DemandEvaluateFragment.getInstance(mDemandId), DEMAND_EVALUATE);
        } else if (tag.equals(getString(R.string.demand_approval_title))) {
            FMBottomInputSheetBuilder builder = new FMBottomInputSheetBuilder(getContext());
            builder.setOnSaveInputListener(this);
            QMUIBottomSheet build = builder.build();
            builder.setTitle(R.string.demand_approval);
            builder.setDescHint(R.string.demand_input_approval_reason_p);
            builder.getLLTwoBtn().setVisibility(View.VISIBLE);
            builder.setLeftBtnText(R.string.demand_refuse);
            builder.setRightBtnText(R.string.demand_pass);
            builder.setTwoBtnRightInput(false);
            builder.setShowTip(getString(R.string.demand_input_approval_reason));
            build.show();
        }
        dialog.dismiss();
    }

    @Override
    public void onSaveClick(QMUIBottomSheet dialog, String input) {
        dialog.dismiss();
        showLoading();
        DemandService.DemandOptReq request = new DemandService.DemandOptReq();
        request.operateType = DemandConstant.DEMAND_OPT_TYPE_SAVE;
        request.desc = input;
        request.reqId = mDemandId;
        getPresenter().optDemand(request);
    }

    @Override
    public void onLeftClick(QMUIBottomSheet dialog, String input) {
        dialog.dismiss();
        showLoading();
        DemandService.DemandOptReq req = new DemandService.DemandOptReq();
        req.reqId = mDemandId;
        req.desc = input;
        req.operateType = DemandConstant.DEMAND_OPT_TYPE_REFUSE;
        getPresenter().optDemand(req);
    }

    @Override
    public void onRightClick(QMUIBottomSheet dialog, String input) {
        dialog.dismiss();
        showLoading();
        DemandService.DemandOptReq req = new DemandService.DemandOptReq();
        req.reqId = mDemandId;
        req.desc = input;
        req.operateType = DemandConstant.DEMAND_OPT_TYPE_PASS;
        getPresenter().optDemand(req);
    }

    public void solveResult(DemandService.DemandOptReq request) {
        switch (request.operateType) {
            case DemandConstant.DEMAND_OPT_TYPE_FINISH://完成 
            case DemandConstant.DEMAND_OPT_TYPE_PASS:  // 通过
            case DemandConstant.DEMAND_OPT_TYPE_REFUSE:// 拒绝
                dismissLoading();
                if (request.operateType == DemandConstant.DEMAND_OPT_TYPE_FINISH) {
                    ToastUtils.showShort(R.string.demand_finish_success);
                } else if (request.operateType == DemandConstant.DEMAND_OPT_TYPE_PASS || request.operateType == DemandConstant.DEMAND_OPT_TYPE_REFUSE) {
                    ToastUtils.showShort(R.string.demand_approval_success);
                }
                Bundle bundle = new Bundle();
                bundle.putInt(DemandConstant.DEMANDOPTTYPE, DemandConstant.DEMAND_OPT_TYPE_FINISH);
                setFragmentResult(RESULT_OK, bundle);
                pop();
                break;
            case DemandConstant.DEMAND_OPT_TYPE_SAVE://保存
                if (request.reqTypeId != null) {
                    dismissLoading();
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt(DemandConstant.DEMANDOPTTYPE, DemandConstant.DEMAND_OPT_TYPE_FINISH);
                    setFragmentResult(RESULT_OK, bundle2);
                    pop();
                } else {
                    getPresenter().getDemandInfo(mDemandId);
                }
                break;

        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == DEMAND_EVALUATE) {
                //Bundle bundle = new Bundle();
                //bundle.putInt(DemandConstant.DEMANDOPTTYPE, DemandConstant.DEMAND_OPT_TYPE_PLEASED);
                //setFragmentResult(RESULT_OK, bundle);
                //pop();
                getDemandInfo();
            } else if (requestCode == REQUEST_CREATE_ORDER) {
                getDemandInfo();
            } else if (requestCode == REQUEST_DEMAND_TYPE) {
                SelectDataBean bean = data.getParcelable(ISelectDataService.SELECT_OFFLINE_DATA_BACK);
                if (bean != null) {
                    mReqTypeId = bean.getId();
                    new FMWarnDialogBuilder(getContext()).setIconVisible(false)
                            .setSureBluBg(true)
                            .setTitle(R.string.demand_remind)
                            .setSure(R.string.demand_sure)
                            .setTip(R.string.demand_edit_type)
                            .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, View view) {
                                    dialog.dismiss();
                                    changeServiceType();
                                }
                            }).create(R.style.fmDefaultWarnDialog).show();
                }
            }
        }
    }

    private void changeServiceType() {
        if (mReqTypeId != null) {
            showLoading();
            DemandService.DemandOptReq request = new DemandService.DemandOptReq();
            request.reqId = mDemandId;
            request.operateType = DemandConstant.DEMAND_OPT_TYPE_SAVE;
            request.reqTypeId = mReqTypeId;
            getPresenter().optDemand(request);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(DemandConstant.DEMANDOPTTYPE, DemandConstant.DEMAND_OPT_TYPE_FINISH);
            setFragmentResult(RESULT_OK, bundle);
            pop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (AudioPlayManager.getAudioPlayService() != null) {
            if (AudioPlayManager.getAudioPlayService().isPlaying()) {
                AudioPlayManager.getAudioPlayService().stopPlaying();
            }
            AudioPlayManager.getAudioPlayService().stopPlayVoiceAnimation();
            AudioPlayManager.getAudioPlayService().quit();
        }
        getPresenter().clearAttachment();
    }

    public static DemandInfoFragment getInstance(Integer type, Long reqId) {
        Bundle bundle = new Bundle();
        bundle.putInt(LIST_TYPE, type);
        bundle.putLong(DEMAND_ID, reqId);
        DemandInfoFragment instance = new DemandInfoFragment();
        instance.setArguments(bundle);
        return instance;
    }


    public static DemandInfoFragment getInstance(Integer type, Long reqId, boolean fromMsg) {
        Bundle bundle = new Bundle();
        bundle.putInt(LIST_TYPE, type);
        bundle.putLong(DEMAND_ID, reqId);
        bundle.putBoolean(DEMAND_FROM_MSG, fromMsg);
        DemandInfoFragment instance = new DemandInfoFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
