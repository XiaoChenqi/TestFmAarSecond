package com.facilityone.wireless.patrol.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.RelationOrdersAdapter;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.ec.module.OrdersBean;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.offline.dao.SiteDao;
import com.facilityone.wireless.a.arch.offline.model.service.PatrolDbService;
import com.facilityone.wireless.a.arch.utils.UrlUtils;
import com.facilityone.wireless.a.arch.widget.BottomTextListSheetBuilder;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.componentservice.workorder.WorkorderService;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.adapter.PatrolQueryEquAdapter;
import com.facilityone.wireless.patrol.module.PatrolConstant;
import com.facilityone.wireless.patrol.module.PatrolQueryService;
import com.facilityone.wireless.patrol.presenter.PatrolQueryEquPresenter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luojilab.component.componentlib.router.Router;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检查询设备页面
 * Date: 2018/11/22 11:20 AM
 */
public class PatrolQueryEquFragment extends BaseFragment<PatrolQueryEquPresenter> implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private LinearLayout mLLDeviceInfo;
    private LinearLayout mLLOrder;
    private RecyclerView mRvOrders;
    private RecyclerView mRvItems;
    private TextView mTvName;
    private TextView mTvStop;
    private TextView mTvType;

    //关联工单
    private List<OrdersBean> mOrdersBeen;
    private RelationOrdersAdapter mOrdersAdapter;
    //检查项
    private List<PatrolQueryService.PatrolQueryItemBean> mItemBeen;
    private PatrolQueryEquAdapter mAdapter;

    private static final String TASK_ID = "task_id";
    private static final String SPOT_ID = "spot_id";
    private static final String EQU_ID = "equ_id";
    private static final String READ_ONLY = "read_only";
    private static final String LOCATION_INFO = "location_info";
    private static final String LOCATION_NAME = "location_name";
    public static final int REQUEST_CREATE_ORDER = 50001;

    private boolean mReadOnly;
    private long mTaskId;
    private long mSpotId;
    private long mEquId;
    private String mLocationName;
    private LocationBean mLocationBean;

    @Override
    public PatrolQueryEquPresenter createPresenter() {
        return new PatrolQueryEquPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_patrol_query_equ;
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            mReadOnly = bundle.getBoolean(READ_ONLY, false);
            mTaskId = bundle.getLong(TASK_ID, 0L);
            mSpotId = bundle.getLong(SPOT_ID, 0L);
            mEquId = bundle.getLong(EQU_ID, -1L);
            mLocationName = bundle.getString(LOCATION_NAME, "");
            mLocationBean = bundle.getParcelable(LOCATION_INFO);
            SiteDao siteDao = new SiteDao();
            List<SelectDataBean> locationSites = siteDao.queryLocationSites();
            if(locationSites != null && locationSites.size() > 0) {
                SelectDataBean locationSite = locationSites.get(0);
                if(locationSite.getId() != null && locationSite.getId().equals(mLocationBean.siteId)) {
                    String locationSiteName = locationSite.getName();
                    mLocationName = locationSiteName + "/" + mLocationName;
                }
            }
        }

        if (mTaskId == 0L || mSpotId == 0L || mEquId == -1L) {
            pop();
            return;
        }

        initView();
        requestData();
    }

    public void requestData() {
        getPresenter().requestData(mTaskId, mSpotId, mEquId);
    }

    private void initView() {

        mLLDeviceInfo = findViewById(R.id.device_info_ll);
        mRvOrders = findViewById(R.id.order_rv);
        mTvName = findViewById(R.id.name_tv);
        mTvStop = findViewById(R.id.stop_tv);
        mTvType = findViewById(R.id.type_tv);
        mLLOrder = findViewById(R.id.order_ll);
        mRvItems = findViewById(R.id.item_rv);

        if (mEquId == PatrolDbService.COMPREHENSIVE_EQU_ID) {
            mLLDeviceInfo.setVisibility(View.GONE);
            setTitle(R.string.patrol_task_spot_content);
        } else {
            mLLDeviceInfo.setVisibility(View.VISIBLE);
        }

        mOrdersBeen = new ArrayList<>();
        mOrdersAdapter = new RelationOrdersAdapter(mOrdersBeen);
        mOrdersAdapter.setOnItemClickListener(this);
        mRvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvOrders.setAdapter(mOrdersAdapter);

        mItemBeen = new ArrayList<>();
        mAdapter = new PatrolQueryEquAdapter(mItemBeen, mReadOnly);
        mAdapter.setOnItemChildClickListener(this);
        mRvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvItems.setAdapter(mAdapter);

        mRvItems.setNestedScrollingEnabled(false);
        mRvOrders.setNestedScrollingEnabled(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        OrdersBean ordersBean = mOrdersBeen.get(position);
        WorkorderService workorderService = (WorkorderService) Router.getInstance().getService(WorkorderService.class.getSimpleName());
        if (workorderService != null) {
            BaseFragment workorderCreateFragment = workorderService.getWorkorderInfoFragment(-1, ordersBean.code, ordersBean.woId);
            start(workorderCreateFragment);
        }
    }

    public void refreshUI(PatrolQueryService.PatrolQueryEquResp data) {
        mTvName.setText(StringUtils.formatString(data.name));
        mTvType.setText(StringUtils.formatString(data.sysType));
        if (data.exceptionStatus != null && data.exceptionStatus.equals(PatrolConstant.EQU_STOP)) {
            mTvStop.setVisibility(View.VISIBLE);
        } else {
            mTvStop.setVisibility(View.GONE);
        }

        mOrdersBeen.clear();
        if (data.orders != null && data.orders.size() > 0) {
            mLLOrder.setVisibility(View.VISIBLE);
            mOrdersBeen.addAll(data.orders);
        } else {
            mLLOrder.setVisibility(View.GONE);
        }
        mOrdersAdapter.notifyDataSetChanged();

        mItemBeen.clear();
        if (data.contents != null && data.contents.size() > 0) {
            mItemBeen.addAll(data.contents);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        final PatrolQueryService.PatrolQueryItemBean patrolQueryItemBean = mItemBeen.get(position);
        if (patrolQueryItemBean == null) {
            return;
        }
        final String comment = StringUtils.formatString(patrolQueryItemBean.comment);
        List<String> imageIds = patrolQueryItemBean.imageIds;
        final List<LocalMedia> localMedias = new ArrayList<>();
        if (imageIds != null && imageIds.size() > 0) {
            for (String imageId : imageIds) {
                LocalMedia media = new LocalMedia();
                media.setSrc(imageId);
                media.setPath(UrlUtils.getImagePath(imageId));
                localMedias.add(media);
            }
        }
        if (view.getId() == R.id.pic_tv) {
            if (localMedias.size() > 0) {
                PictureSelector.create(this)
                        .themeStyle(R.style.picture_fm_style)
                        .openExternalPreview(0, localMedias);
            }
        } else if (view.getId() == R.id.del_tv) {
            List<String> menu = new ArrayList<>();
            menu.add(getString(R.string.patrol_task_query_repair));
            if (patrolQueryItemBean.processed == null || !patrolQueryItemBean.processed) {
                menu.add(getString(R.string.patrol_task_query_handle_mark));
            }
            menu.add(getString(R.string.patrol_task_submit_cancel));

            BottomTextListSheetBuilder builder = new BottomTextListSheetBuilder(getContext());
            builder.addArrayItem(menu);
            builder.setOnSheetItemClickListener(new BottomTextListSheetBuilder.OnSheetItemClickListener() {

                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    dialog.dismiss();
                    if (tag.equals(getString(R.string.patrol_task_query_handle_mark))) {
                        getPresenter().optTagDel(patrolQueryItemBean.patrolTaskSpotResultId);
                    } else if (tag.equals(getString(R.string.patrol_task_query_repair))) {
                        WorkorderService workorderService = (WorkorderService) Router.getInstance().getService(WorkorderService.class.getSimpleName());
                        if (workorderService != null) {
                            startForResult(workorderService.getWorkorderCreateFragment(WorkorderService.CREATE_ORDER_BY_PATROL_QUERY_REPAIR, mEquId, mLocationName, mLocationBean, localMedias, patrolQueryItemBean.patrolTaskSpotResultId, comment, null, null, null, true), REQUEST_CREATE_ORDER);
                        }
                    }
                }
            });
            builder.build().show();
        }

    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            requestData();
        }
    }

    public static PatrolQueryEquFragment getInstance(Long taskId, Long spotId, Long equId, Boolean readonly, LocationBean locationBean, String locationName) {
        Bundle bundle = new Bundle();
        bundle.putLong(TASK_ID, taskId);
        bundle.putLong(SPOT_ID, spotId);
        bundle.putLong(EQU_ID, equId);
        bundle.putString(LOCATION_NAME, locationName);
        bundle.putBoolean(READ_ONLY, readonly);
        bundle.putParcelable(LOCATION_INFO, locationBean);
        PatrolQueryEquFragment instance = new PatrolQueryEquFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
