package com.facilityone.wireless.maintenance.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.module.AttachmentBean;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.UrlUtils;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.componentservice.workorder.WorkorderService;
import com.facilityone.wireless.maintenance.R;
import com.facilityone.wireless.maintenance.adapter.MaintenanceAttachmentAdapter;
import com.facilityone.wireless.maintenance.adapter.MaintenanceEquipmentAdapter;
import com.facilityone.wireless.maintenance.adapter.MaintenanceMaterialAdapter;
import com.facilityone.wireless.maintenance.adapter.MaintenanceSpaceAdapter;
import com.facilityone.wireless.maintenance.adapter.MaintenanceStepAdapter;
import com.facilityone.wireless.maintenance.adapter.MaintenanceToolAdapter;
import com.facilityone.wireless.maintenance.adapter.MaintenanceWorkorderAdapter;
import com.facilityone.wireless.maintenance.model.MaintenanceConstant;
import com.facilityone.wireless.maintenance.model.MaintenanceHelper;
import com.facilityone.wireless.maintenance.model.MaintenanceService;
import com.facilityone.wireless.maintenance.presenter.MaintenanceContentPresenter;
import com.joanzapata.iconify.widget.IconTextView;
import com.luojilab.component.componentlib.router.Router;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by peter.peng on 2018/11/20.
 * 计划性维护详情的维护内容页面
 */

public class MaintenanceContentFragment extends BaseFragment<MaintenanceContentPresenter> implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    private TextView mNameTv;//维保名称
    private TextView mInfluenceTv;//影响
    private TextView mPeriodTv;//周期
    private TextView mFirstTimeTv;//首次维护时间
    private TextView mNextTimeTv;//下次维护时间
    private TextView mStartEndTimeTv;//维保周期
    private TextView mNeedTimeTv;//耗时
    private TextView mAutoGenerateOrderTv;//自动生成工单
    private TextView mAheadDayTv;//提前天数
    private RecyclerView mStepRv;//步骤
    private RecyclerView mMaterialRv;//物料
    private RecyclerView mToolRv;//工具
    private RecyclerView mEquipmentRv;//维护设备
    private RecyclerView mSpaceRv;//空间位置
    private RecyclerView mWorkOrderRv;//维护工单
    private RecyclerView mAttachmentRv;//附件
    private LinearLayout mStepLl;//步骤布局
    private LinearLayout mMaterialLl;//物料布局
    private LinearLayout mToolLl;//工具布局
    private LinearLayout mEquipmentLl;//维护设备布局
    private LinearLayout mSpaceLl;//空间位置布局
    private LinearLayout mWorkOrderLl;//维护工单布局
    private LinearLayout mAttachmentLl;//附件布局
//    private ImageView mMoreMenuIv;//更多按钮

    private LinearLayout mStepTitleLl;//步骤标题
    private LinearLayout mMaterialTitleLl;//物料标题
    private LinearLayout mToolTitleLl;//工具标题
    private LinearLayout mEquipmentTitleLl;//维护设备标题
    private LinearLayout mSpaceTitleLl;//空间位置标题
    private LinearLayout mWorkOrderTitleLl;//维护工单标题
    private IconTextView mStepExtendItv;//步骤下拉图标
    private IconTextView mMaterialExtendItv;//物料下拉图标
    private IconTextView mToolExtendItv;//工具下拉图标
    private IconTextView mEquipmentExtendItv;//维护设备下拉图标
    private IconTextView mSpaceExtendItv;//空间位置下拉图标
    private IconTextView mWorkOrderItv;//维护工单下拉图标

    private static final String MAINTENANCE_INFO = "maintenance_info";
    private static final String MAINTENANCE_FROM = "maintenance_from";
    private static final String MAINTENANCE_PM_ID = "maintenance_pm_id";
    private static final String MAINTENANCE_TODO_ID = "maintenance_todo_id";

    private MaintenanceStepAdapter mStepAdapter;
    private List<MaintenanceService.Step> mStepList;//步骤列表
    private MaintenanceMaterialAdapter mMaterialAdapter;
    private List<MaintenanceService.Material> mMaterialList;//物料列表
    private MaintenanceToolAdapter mToolAdapter;
    private List<MaintenanceService.Tool> mToolList;//工具列表
    private MaintenanceEquipmentAdapter mEquipmentAdapter;
    private List<MaintenanceService.MaintenanceEquipment> mEquipmentList;//维护设备列表
    private MaintenanceSpaceAdapter mSpaceAdapter;
    private List<MaintenanceService.Space> mSpaceList;//空间位置列表
    private MaintenanceAttachmentAdapter mAttachmentAdapter;
    private List<MaintenanceService.MaintenanceWorkOrder> mWorkOrderList;//维护工单列表
    private MaintenanceWorkorderAdapter mWorkorderAdapter;
    private List<AttachmentBean> mAttachmentBeanList;//附件列表

    //计划性维护详情信息
    private MaintenanceService.MaintenanceInfoBean mMaintenanceInfo;
    private boolean mFrom;
    private long mPmId;
    private long mTodoId;

    @Override
    public MaintenanceContentPresenter createPresenter() {
        return new MaintenanceContentPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_maintenance_content;
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


    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mFrom = bundle.getBoolean(MAINTENANCE_FROM, false);
            mPmId = bundle.getLong(MAINTENANCE_PM_ID, -1L);
            mTodoId = bundle.getLong(MAINTENANCE_TODO_ID, -1L);
            mMaintenanceInfo = bundle.getParcelable(MAINTENANCE_INFO);
        }

        if (mFrom) {
//            if (mPmId == -1L || mTodoId == -1L) {
//                pop();
//                return;
//            }
            getPresenter().getMaintenanceInfo(mPmId, mTodoId);
        } else {
            refreshView();
        }
    }


    private void initView() {
        setTitle(R.string.maintenance_detail_title);

        mNameTv = findViewById(R.id.maintenance_content_name_tv);
        mInfluenceTv = findViewById(R.id.maintenance_content_influence_tv);
        mPeriodTv = findViewById(R.id.maintenance_content_period_tv);
        mFirstTimeTv = findViewById(R.id.maintenance_content_first_time_tv);
        mNextTimeTv = findViewById(R.id.maintenance_content_next_time_tv);
        mStartEndTimeTv = findViewById(R.id.maintenance_content_start_end_time_tv);
        mNeedTimeTv = findViewById(R.id.maintenance_content_need_time_tv);
        mAutoGenerateOrderTv = findViewById(R.id.maintenance_content_auto_generate_order_tv);
        mAheadDayTv = findViewById(R.id.maintenance_content_ahead_day_tv);
        mStepRv = findViewById(R.id.step_recyclerView);
        mMaterialRv = findViewById(R.id.material_recyclerView);
        mToolRv = findViewById(R.id.tool_recyclerView);
        mEquipmentRv = findViewById(R.id.equipment_recyclerView);
        mSpaceRv = findViewById(R.id.space_recyclerView);
        mWorkOrderRv = findViewById(R.id.work_order_recyclerView);
        mAttachmentRv = findViewById(R.id.attachment_recyclerView);
        mStepLl = findViewById(R.id.maintenance_content_step_ll);
        mMaterialLl = findViewById(R.id.maintenance_content_material_ll);
        mToolLl = findViewById(R.id.maintenance_content_tool_ll);
        mEquipmentLl = findViewById(R.id.maintenance_equipment_ll);
        mSpaceLl = findViewById(R.id.maintenance_space_ll);
        mWorkOrderLl = findViewById(R.id.maintenance_work_order_ll);
        mAttachmentLl = findViewById(R.id.maintenance_content_attachment_ll);
//        mMoreMenuIv = findViewById(R.id.more_menu_iv);
        mStepTitleLl = findViewById(R.id.maintenance_content_step_title_ll);
        mMaterialTitleLl = findViewById(R.id.maintenance_content_material_title_ll);
        mToolTitleLl = findViewById(R.id.maintenance_content_tool_title_ll);
        mEquipmentTitleLl = findViewById(R.id.maintenance_equipment_title_ll);
        mSpaceTitleLl = findViewById(R.id.maintenance_space_title_ll);
        mWorkOrderTitleLl = findViewById(R.id.maintenance_work_order_title_ll);
        mStepExtendItv = findViewById(R.id.maintenance_content_step_expand_itv);
        mMaterialExtendItv = findViewById(R.id.maintenance_content_material_expand_itv);
        mToolExtendItv = findViewById(R.id.maintenance_content_tool_expand_itv);
        mEquipmentExtendItv = findViewById(R.id.maintenance_equipment_expand_itv);
        mSpaceExtendItv = findViewById(R.id.maintenance_space_expand_itv);
        mWorkOrderItv = findViewById(R.id.maintenance_work_order_expand_itv);

        mStepRv.setNestedScrollingEnabled(false);
        mMaterialRv.setNestedScrollingEnabled(false);
        mToolRv.setNestedScrollingEnabled(false);
        mEquipmentRv.setNestedScrollingEnabled(false);
        mSpaceRv.setNestedScrollingEnabled(false);
        mWorkOrderRv.setNestedScrollingEnabled(false);
        mAttachmentRv.setNestedScrollingEnabled(false);

        //设置更多按钮的点击事件
//        mMoreMenuIv.setOnClickListener(this);
        mStepTitleLl.setOnClickListener(this);
        mMaterialTitleLl.setOnClickListener(this);
        mToolTitleLl.setOnClickListener(this);
        mEquipmentTitleLl.setOnClickListener(this);
        mSpaceTitleLl.setOnClickListener(this);
        mWorkOrderTitleLl.setOnClickListener(this);

        mStepRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mStepList = new ArrayList<>();
        mStepAdapter = new MaintenanceStepAdapter(mStepList);
        mStepRv.setAdapter(mStepAdapter);

        mMaterialRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mMaterialList = new ArrayList<>();
        mMaterialAdapter = new MaintenanceMaterialAdapter(mMaterialList);
        mMaterialRv.setAdapter(mMaterialAdapter);

        mToolRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mToolList = new ArrayList<>();
        mToolAdapter = new MaintenanceToolAdapter(mToolList);
        mToolRv.setAdapter(mToolAdapter);

        mEquipmentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mEquipmentList = new ArrayList<>();
        mEquipmentAdapter = new MaintenanceEquipmentAdapter(mEquipmentList);
        mEquipmentRv.setAdapter(mEquipmentAdapter);

        mSpaceRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mSpaceList = new ArrayList<>();
        mSpaceAdapter = new MaintenanceSpaceAdapter(mSpaceList);
        mSpaceRv.setAdapter(mSpaceAdapter);

        mWorkOrderRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mWorkOrderList = new ArrayList<>();
        mWorkorderAdapter = new MaintenanceWorkorderAdapter(mWorkOrderList);
        mWorkOrderRv.setAdapter(mWorkorderAdapter);
        mWorkorderAdapter.setOnItemClickListener(this);

        mAttachmentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAttachmentBeanList = new ArrayList<>();
        mAttachmentAdapter = new MaintenanceAttachmentAdapter(mAttachmentBeanList);
        mAttachmentRv.setAdapter(mAttachmentAdapter);
        mAttachmentAdapter.setOnItemClickListener(this);

    }

    public void setMaintenanceInfo(MaintenanceService.MaintenanceInfoBean maintenanceInfo) {
        mMaintenanceInfo = maintenanceInfo;
        refreshView();
    }

    /**
     * 根据数据刷新视图
     */
    private void refreshView() {
        //刷新基本信息
        refreshBasicInfo();
        //刷新步骤
        refreshStep();
        //刷新物料
        refreshMaterial();
        //刷新工具
        refreshTool();
        //刷新设备
        refreshEquipment();
        //刷新空间位置
        refreshSpace();
        //刷新维护工单
        refreshWorkOrder();
        //刷新附件
        refreshAttachment();
        dismissLoading();
    }

    private void refreshWorkOrder() {
        if (mMaintenanceInfo != null && mMaintenanceInfo.workOrders != null && mMaintenanceInfo.workOrders.size() > 0) {
            mWorkOrderList.clear();
            mWorkOrderLl.setVisibility(View.VISIBLE);
            mWorkOrderList.addAll(mMaintenanceInfo.workOrders);
            mWorkorderAdapter.notifyDataSetChanged();
        } else {
            mWorkOrderLl.setVisibility(View.GONE);
        }
    }

    private void refreshSpace() {
        if (mMaintenanceInfo != null && mMaintenanceInfo.spaces != null && mMaintenanceInfo.spaces.size() > 0) {
            mSpaceList.clear();
            mSpaceLl.setVisibility(View.VISIBLE);
            mSpaceList.addAll(mMaintenanceInfo.spaces);
            mSpaceAdapter.notifyDataSetChanged();
        } else {
            mSpaceLl.setVisibility(View.GONE);
        }
    }

    private void refreshEquipment() {
        if (mMaintenanceInfo != null && mMaintenanceInfo.equipments != null && mMaintenanceInfo.equipments.size() > 0) {
            mEquipmentList.clear();
            mEquipmentLl.setVisibility(View.VISIBLE);
            mEquipmentList.addAll(mMaintenanceInfo.equipments);
            mEquipmentAdapter.notifyDataSetChanged();
        } else {
            mEquipmentLl.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新附件
     */
    private void refreshAttachment() {
        if (mMaintenanceInfo != null && mMaintenanceInfo.pictures != null && mMaintenanceInfo.pictures.size() > 0) {
            mAttachmentBeanList.clear();
            mAttachmentLl.setVisibility(View.VISIBLE);
            mAttachmentBeanList.addAll(mMaintenanceInfo.pictures);
            mAttachmentAdapter.notifyDataSetChanged();
        } else {
            mAttachmentLl.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新工具
     */
    private void refreshTool() {
        if (mMaintenanceInfo != null && mMaintenanceInfo.pmTools != null && mMaintenanceInfo.pmTools.size() > 0) {
            mToolList.clear();
            mToolLl.setVisibility(View.VISIBLE);
            mToolList.addAll(mMaintenanceInfo.pmTools);
            mToolAdapter.notifyDataSetChanged();
        } else {
            mToolLl.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新物料
     */
    private void refreshMaterial() {
        if (mMaintenanceInfo != null && mMaintenanceInfo.pmMaterials != null && mMaintenanceInfo.pmMaterials.size() > 0) {
            mMaterialList.clear();
            mMaterialLl.setVisibility(View.VISIBLE);
            mMaterialList.addAll(mMaintenanceInfo.pmMaterials);
            mMaterialAdapter.notifyDataSetChanged();
        } else {
            mMaterialLl.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新步骤
     */
    private void refreshStep() {
        if (mMaintenanceInfo != null && mMaintenanceInfo.pmSteps != null && mMaintenanceInfo.pmSteps.size() > 0) {
            mStepList.clear();
            mStepLl.setVisibility(View.VISIBLE);
            mStepList.addAll(mMaintenanceInfo.pmSteps);
            mStepAdapter.notifyDataSetChanged();
        } else {
            mStepLl.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新基本信息
     */
    private void refreshBasicInfo() {
        if (mMaintenanceInfo != null) {
            mNameTv.setText(StringUtils.formatString(mMaintenanceInfo.name));
            mInfluenceTv.setText(StringUtils.formatString(mMaintenanceInfo.influence));
            mPeriodTv.setText(StringUtils.formatString(mMaintenanceInfo.period));
            if (mMaintenanceInfo.dateFirstTodo != null) {
                mFirstTimeTv.setText(TimeUtils.date2String(new Date(mMaintenanceInfo.dateFirstTodo), DateUtils.SIMPLE_DATE_FORMAT_YMD));
            } else {
                mFirstTimeTv.setText("");
            }
            if (mMaintenanceInfo.dateNextTodo != null) {
                mNextTimeTv.setText(TimeUtils.date2String(new Date(mMaintenanceInfo.dateNextTodo), DateUtils.SIMPLE_DATE_FORMAT_YMD));
            } else {
                mNextTimeTv.setText("");
            }

            StringBuffer sb = new StringBuffer("");
            if (mMaintenanceInfo.startTime == null) {
                mMaintenanceInfo.startTime = 1L;
            }
            if (mMaintenanceInfo.endTime == null) {
                mMaintenanceInfo.endTime = 12L;
            }
            if ("en_US".equals(getPresenter().getCurLanguage())) {
                sb.append(MaintenanceHelper.getMonthMap(getContext()).get(mMaintenanceInfo.startTime)
                        + getString(R.string.maintenance_task_month_to));
                if (mMaintenanceInfo.startTime > mMaintenanceInfo.endTime) {
                    sb.append(getString(R.string.maintenance_task_next)
                            + MaintenanceHelper.getMonthMap(getContext()).get(mMaintenanceInfo.endTime)
                            + getString(R.string.maintenance_month));
                } else {
                    sb.append(MaintenanceHelper.getMonthMap(getContext()).get(mMaintenanceInfo.endTime)
                            + getString(R.string.maintenance_month));
                }
            } else {
                sb.append(mMaintenanceInfo.startTime + getString(R.string.maintenance_task_month_to));
                if (mMaintenanceInfo.startTime > mMaintenanceInfo.endTime) {
                    sb.append(getString(R.string.maintenance_task_next) + mMaintenanceInfo.endTime + getString(R.string.maintenance_month));
                } else {
                    sb.append(mMaintenanceInfo.endTime + getString(R.string.maintenance_month));
                }
            }
            mStartEndTimeTv.setText(sb.toString());

            mNeedTimeTv.setText(StringUtils.formatString(mMaintenanceInfo.estimatedWorkingTime));
            mAutoGenerateOrderTv.setText(mMaintenanceInfo.autoGenerateOrder != null && mMaintenanceInfo.autoGenerateOrder ? getString(R.string.maintenance_yes) : getString(R.string.maintenance_no));
            if (mMaintenanceInfo.ahead != null) {
                mAheadDayTv.setText(mMaintenanceInfo.ahead + getString(R.string.maintenance_day));
            } else {
                mAheadDayTv.setText("");
            }
        }
    }


    /**
     * 当点击右下方更多按钮时回调
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
//        if(v.getId() == R.id.more_menu_iv) {
//            showBottomDialog();
//        }else
        if (v.getId() == R.id.maintenance_content_step_title_ll) {
            //点击步骤标题
            //当前步骤隐藏部分是否显示，
            Boolean tag = (Boolean) v.getTag();
            if (tag == null) {
                tag = false;
            }
            //更改点击后的步骤隐藏部分的显示状态
            tag = !tag;
            v.setTag(tag);
            //设置隐藏部分的显示与否及下拉图标的状态
            mStepExtendItv.setText(tag ? R.string.icon_arrow_up : R.string.icon_arrow_down);
            mStepRv.setVisibility(tag ? View.VISIBLE : View.GONE);
        } else if (v.getId() == R.id.maintenance_content_material_title_ll) {
            //点击物料标题
            //当前物料隐藏部分是否显示，
            Boolean tag = (Boolean) v.getTag();
            if (tag == null) {
                tag = false;
            }
            //更改点击后的物料隐藏部分的显示状态
            tag = !tag;
            v.setTag(tag);
            //设置隐藏部分的显示与否及下拉图标的状态
            mMaterialExtendItv.setText(tag ? R.string.icon_arrow_up : R.string.icon_arrow_down);
            mMaterialRv.setVisibility(tag ? View.VISIBLE : View.GONE);
        } else if (v.getId() == R.id.maintenance_content_tool_title_ll) {
            //点击工具标题
            //当前工具隐藏部分是否显示，
            Boolean tag = (Boolean) v.getTag();
            if (tag == null) {
                tag = false;
            }
            //更改点击后的工具隐藏部分的显示状态
            tag = !tag;
            v.setTag(tag);
            //设置隐藏部分的显示与否及下拉图标的状态
            mToolExtendItv.setText(tag ? R.string.icon_arrow_up : R.string.icon_arrow_down);
            mToolRv.setVisibility(tag ? View.VISIBLE : View.GONE);
        } else if (v.getId() == R.id.maintenance_equipment_title_ll) {
            //点击维护设备标题
            //当前维护设备隐藏部分是否显示，
            Boolean tag = (Boolean) v.getTag();
            if (tag == null) {
                tag = false;
            }
            //更改点击后的维护设备隐藏部分的显示状态
            tag = !tag;
            v.setTag(tag);
            //设置隐藏部分的显示与否及下拉图标的状态
            mEquipmentExtendItv.setText(tag ? R.string.icon_arrow_up : R.string.icon_arrow_down);
            mEquipmentRv.setVisibility(tag ? View.VISIBLE : View.GONE);
        } else if (v.getId() == R.id.maintenance_space_title_ll) {
            //点击空间位置标题
            //当前空间位置隐藏部分是否显示，
            Boolean tag = (Boolean) v.getTag();
            if (tag == null) {
                tag = false;
            }
            //更改点击后的空间位置隐藏部分的显示状态
            tag = !tag;
            v.setTag(tag);
            //设置隐藏部分的显示与否及下拉图标的状态
            mSpaceExtendItv.setText(tag ? R.string.icon_arrow_up : R.string.icon_arrow_down);
            mSpaceRv.setVisibility(tag ? View.VISIBLE : View.GONE);
        } else if (v.getId() == R.id.maintenance_work_order_title_ll) {
            //点击维护工单标题
            //当前维护工单隐藏部分是否显示，
            Boolean tag = (Boolean) v.getTag();
            if (tag == null) {
                tag = false;
            }
            //更改点击后的维护工单隐藏部分的显示状态
            tag = !tag;
            v.setTag(tag);
            //设置隐藏部分的显示与否及下拉图标的状态
            mWorkOrderItv.setText(tag ? R.string.icon_arrow_up : R.string.icon_arrow_down);
            mWorkOrderRv.setVisibility(tag ? View.VISIBLE : View.GONE);
        }
    }


    /**
     * 显示底部dialog
     */
//    private void showBottomDialog() {
//
//        MaintenanceBottomGridSheetBuilder builder = new MaintenanceBottomGridSheetBuilder(getContext());
//        builder.addItem(R.drawable.maintance_object, getString(R.string.maintenance_object_tip), MaintenanceConstant.TAG_MENU_MAINTENANCE_OBJECT, FMBottomGridSheetBuilder.FIRST_LINE)
//                .addItem(R.drawable.maintance_work_order, getString(R.string.maintenance_work_order_tip), MaintenanceConstant.TAG_MENU_MAINTENANCE_WORK_ORDER, FMBottomGridSheetBuilder.FIRST_LINE)
//                .setIsShowButton(false)
//                .setOnSheetItemClickListener(new FMBottomGridSheetBuilder.OnSheetItemClickListener() {
//                    @Override
//                    public void onClick(QMUIBottomSheet dialog, View itemView) {
//                        dialog.dismiss();
//                        int tag = (int) itemView.getTag();
//                        switch (tag) {
//                            case MaintenanceConstant.TAG_MENU_MAINTENANCE_OBJECT://对象
//                                startWithPop(MaintenanceObjectFragment.getInstance(mMaintenanceInfo));
//                                break;
//                            case MaintenanceConstant.TAG_MENU_MAINTENANCE_WORK_ORDER://维护工单
//                                startWithPop(MaintenanceWorkorderFragment.getInstance(mMaintenanceInfo));
//                                break;
//                        }
//                    }
//                })
//                .build()
//                .show();
//    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if(adapter == mAttachmentAdapter) {
            //打开附件
            AttachmentBean attachmentBean = ((MaintenanceAttachmentAdapter) adapter).getData().get(position);
            if (attachmentBean != null) {
                attachmentBean.url = UrlUtils.getAttachmentPath(attachmentBean.src);
                getPresenter().openAttachment(attachmentBean.url, attachmentBean.name, getContext());
            }
        }else if(adapter == mWorkorderAdapter) {
            Router router = Router.getInstance();
            WorkorderService workorderService = (WorkorderService) router.getService(WorkorderService.class.getSimpleName());
            if(workorderService != null) {
                MaintenanceService.MaintenanceWorkOrder maintenanceWorkOrder = ((MaintenanceWorkorderAdapter) adapter).getData().get(position);
                if(maintenanceWorkOrder != null) {
                    int workorderStatus = maintenanceWorkOrder.status;
                    String code = maintenanceWorkOrder.code;
                    Long woId = maintenanceWorkOrder.woId;
                    BaseFragment workorderInfoFragment = workorderService.getWorkorderInfoFragment(MaintenanceConstant.WORKORDER_STATUS_NONE, code, woId);
                    start(workorderInfoFragment);
                }

            }
        }

    }

    public static MaintenanceContentFragment getInstance(MaintenanceService.MaintenanceInfoBean maintenanceInfoBean) {
        MaintenanceContentFragment fragment = new MaintenanceContentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MAINTENANCE_INFO, maintenanceInfoBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MaintenanceContentFragment getInstance(boolean fromHome, Long pmId, Long todoId) {
        MaintenanceContentFragment fragment = new MaintenanceContentFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(MAINTENANCE_FROM, fromHome);
        bundle.putLong(MAINTENANCE_PM_ID, pmId);
        bundle.putLong(MAINTENANCE_TODO_ID, todoId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
