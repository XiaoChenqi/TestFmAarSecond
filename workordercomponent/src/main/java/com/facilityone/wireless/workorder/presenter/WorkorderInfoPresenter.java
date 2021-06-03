package com.facilityone.wireless.workorder.presenter;

import android.content.Context;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.widget.BottomTextListSheetBuilder;
import com.facilityone.wireless.a.arch.widget.FMBottomInputSheetBuilder;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.fragment.WorkorderApprovalFragment;
import com.facilityone.wireless.workorder.fragment.WorkorderDispatchFragment;
import com.facilityone.wireless.workorder.fragment.WorkorderInfoFragment;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderLaborerService;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.module.WorkorderUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/11 上午10:11
 */
public class WorkorderInfoPresenter extends BaseWorkOrderPresenter<WorkorderInfoFragment> {

    @Override
    public void getWorkorderInfoSuccess(Long woId, WorkorderService.WorkorderInfoBean data) {
        super.getWorkorderInfoSuccess(woId, data);
        if (data != null) {
            if (data.workOrderLaborers != null && data.workOrderLaborers.size() > 0) {
                boolean laborer = processingLaborer(data.workOrderLaborers, getV().getStatus(), data.status);
                if (!laborer) {
                    getV().setRefreshStatus(-1);
                }
                getV().setLaborer(laborer);
            }
            getV().refreshBasicInfoUI(data);
            //请求物料
            getWorkorderReserveRecordList(woId);
            getV().refreshHistoryUI(data);
            getV().refreshLaborerUI(data);
            getV().refreshEquipmentUI(data);
            getV().refreshToolsAndBplUI(data);
            getV().refreshPayment(data);
        } else {
            getV().refreshError();
        }
    }

    @Override
    public void getWorkorderInfoError() {
        super.getWorkorderInfoError();
        getV().refreshError();
    }

    /**
     * 获取工单信息
     *
     * @param woId
     */
//    public void getWorkorderInfo(final Long woId) {
//        String json = "{\"woId\":" + woId + "}";
//        OkGo.<BaseResponse<WorkorderService.WorkorderInfoBean>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_INFO_URL)
//                .tag(getV())
//                .isSpliceUrl(true)
//                .upJson(json)
//                .execute(new FMJsonCallback<BaseResponse<WorkorderService.WorkorderInfoBean>>() {
//                    @Override
//                    public void onSuccess(Response<BaseResponse<WorkorderService.WorkorderInfoBean>> response) {
//                        WorkorderService.WorkorderInfoBean data = response.body().data;
//                        if (data != null) {
//                            if (data.workOrderLaborers != null && data.workOrderLaborers.size() > 0) {
//                                boolean laborer = processingLaborer(data.workOrderLaborers, getV().getStatus(), data.status);
//                                if (!laborer) {
//                                    getV().setRefreshStatus(-1);
//                                }
//                                getV().setLaborer(laborer);
//                            }
//                            getV().refreshBasicInfoUI(data);
//                            //请求物料
//                            getWorkorderReserveRecordList(woId);
//                            getV().refreshHistoryUI(data);
//                            getV().refreshLaborerUI(data);
//                            getV().refreshEquipmentUI(data);
//                            getV().refreshToolsAndBplUI(data);
//                            getV().refreshPayment(data);
//                        } else {
//                            getV().refreshError();
//                        }
//                        getV().dismissLoading();
//                    }
//
//                    @Override
//                    public void onError(Response<BaseResponse<WorkorderService.WorkorderInfoBean>> response) {
//                        super.onError(response);
//                        getV().refreshError();
//                    }
//                });
//    }

    /**
     * 加工一下执行人是否可操作
     *
     * @param workOrderLaborers 执行人列表
     * @param status            用于排除查询页面
     * @param integer           工单状态
     * @return
     */
    public boolean processingLaborer(List<WorkorderLaborerService.WorkorderLaborerBean> workOrderLaborers, int status, Integer integer) {
        boolean laborerHave = false;//当前工单是否属于登录用户
        for (WorkorderLaborerService.WorkorderLaborerBean workOrderLaborer : workOrderLaborers) {
            if (status == WorkorderConstant.WORK_STATUS_NONE) {
                workOrderLaborer.canOpt = false;
                continue;
            }
            if (workOrderLaborer.laborerId != null && workOrderLaborer.laborerId.equals(FM.getEmId())) {
                laborerHave = true;
            }
            if (integer != null && integer == WorkorderConstant.WORK_STATUS_PROCESS) {
                if (workOrderLaborer.laborerId != null && workOrderLaborer.laborerId.equals(FM.getEmId())
                        && workOrderLaborer.status == WorkorderConstant.WORKORDER_STATUS_PERSONAL_ACCEPT) {
                    workOrderLaborer.canOpt = true;
                    continue;
                }
            }
            workOrderLaborer.canOpt = false;
        }
        return laborerHave;
    }

    /**
     * 更多按钮点击后显示的菜单
     *  @param context    上下文
     * @param status     工单状态
     * @param woId       工单id
     * @param approvalId 执行人id
     * @param code       工单code
     * @param workOrderMaterials 物料
     */
    public void onMoreMenuClick(final Context context,
                                final int status,
                                final boolean acceptWorkOrder,
                                final Long woId,
                                final Long approvalId,
                                final String code,
                                final String sendWorkContent,
                                final Long estimateStartTime,
                                final Long estimateEndTime, final List<WorkorderService.WorkorderReserveRocordBean> workOrderMaterials) {
        List<String> menu = new ArrayList<>();
        boolean finished = false;
        switch (status) {
            case WorkorderConstant.WORK_STATUS_CREATED:// 已创建
            case WorkorderConstant.WORK_STATUS_SUSPENDED_NO:// 已暂停(不继续工作)
                menu.add(getV().getString(R.string.workorder_arrange_order));
                menu.add(getV().getString(R.string.workorder_stop));
                menu.add(getV().getString(R.string.workorder_approval_title));
                finished = true;
                break;
            case WorkorderConstant.WORK_STATUS_PUBLISHED:// 已发布
                menu.add(getV().getString(R.string.workorder_accept_order));
                menu.add(getV().getString(R.string.workorder_back_order));
                menu.add(getV().getString(R.string.workorder_approval_title));
                break;
            case WorkorderConstant.WORK_STATUS_PROCESS:// 处理中
                if (acceptWorkOrder) {
                    menu.add(getV().getString(R.string.workorder_finish));
                    menu.add(getV().getString(R.string.workorder_stop_order));
                    menu.add(getV().getString(R.string.workorder_stop));
                    menu.add(getV().getString(R.string.workorder_back_order));
                    menu.add(getV().getString(R.string.workorder_approval_title));
                } else {
                    menu.add(getV().getString(R.string.workorder_accept_order));
                    menu.add(getV().getString(R.string.workorder_back_order));
                    menu.add(getV().getString(R.string.workorder_approval_title));
                }
                break;
            case WorkorderConstant.WORK_STATUS_SUSPENDED_GO:// 已暂停(继续工作)
                menu.add(getV().getString(R.string.workorder_continue_order));
                break;
            case WorkorderConstant.WORK_STATUS_TERMINATED:// 已终止
            case WorkorderConstant.WORK_STATUS_COMPLETED:// 已完成
                menu.add(getV().getString(R.string.workorder_verify_tip));
                menu.add(getV().getString(R.string.workorder_archive));
                break;
            case WorkorderConstant.WORK_STATUS_VERIFIED:// 已验证
                menu.add(getV().getString(R.string.workorder_archive));
                break;
            case WorkorderConstant.WORK_STATUS_ARCHIVED:// 已存档
                break;
            case WorkorderConstant.WORK_STATUS_APPROVAL:// 待审批
                menu.add(getV().getString(R.string.workorder_approval_order));
                break;
        }

        if (menu.size() == 0) {
            return;
        }
        menu.add(getV().getString(R.string.workorder_cancel));

        BottomTextListSheetBuilder builder = new BottomTextListSheetBuilder(context);
        builder.addArrayItem(menu);
        final boolean finalFinished = finished;
        builder.setOnSheetItemClickListener(new BottomTextListSheetBuilder.OnSheetItemClickListener() {
            @Override
            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                dialog.dismiss();
                if (tag.equals(getV().getString(R.string.workorder_arrange_order))) {
                    boolean isDispatch = true;
                    if(workOrderMaterials != null && workOrderMaterials.size() > 0) {
                        for (WorkorderService.WorkorderReserveRocordBean workorderReserveRocordBean : workOrderMaterials) {
                            if(workorderReserveRocordBean.reservationPersonId == null
                                    || workorderReserveRocordBean.administrator == null
                                    || workorderReserveRocordBean.supervisor == null) {
                                isDispatch = false;
                            }
                        }
                    }
                    if(isDispatch) {
                        getV().startForResult(WorkorderDispatchFragment.getInstance(woId, code, sendWorkContent, estimateStartTime, estimateEndTime),
                                WorkorderInfoFragment.DISPATCH_REQUEST_CODE);
                    }else {
                        FMWarnDialogBuilder warnDialogBuilder = new FMWarnDialogBuilder(context);
                        warnDialogBuilder.setTitle(getV().getString(R.string.workorder_tip_title));
                        warnDialogBuilder.setSure(getV().getString(R.string.workorder_confirm));
                        warnDialogBuilder.setCancel(getV().getString(R.string.workorder_cancel));
                        warnDialogBuilder.setTip(R.string.workorder_none_any_people);
                        warnDialogBuilder.addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, View view) {
                                dialog.dismiss();
                                getV().startForResult(WorkorderDispatchFragment.getInstance(woId, code, sendWorkContent, estimateStartTime, estimateEndTime),
                                        WorkorderInfoFragment.DISPATCH_REQUEST_CODE);
                            }
                        });
                        warnDialogBuilder.addOnBtnCancelClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, View view) {
                                dialog.dismiss();
                            }
                        });
                        warnDialogBuilder.create(R.style.fmDefaultWarnDialog).show();
                    }
                } else if (tag.equals(getV().getString(R.string.workorder_stop))) {
                    if (getV().isAllStart() || finalFinished) {
                        endWorkOrder(context, woId);
                    } else {
                        ToastUtils.showShort(R.string.workorder_terminal_error);
                    }
                } else if (tag.equals(getV().getString(R.string.workorder_approval_title))) {
                    getV().startForResult(WorkorderApprovalFragment.getInstance(woId)
                            , WorkorderInfoFragment.APPROVAL_REQUEST_CODE);
                } else if (tag.equals(getV().getString(R.string.workorder_approval_order))) {
                    approvalWorkOrder(context, woId, approvalId);
                } else if (tag.equals(getV().getString(R.string.workorder_verify_tip))) {
                    verifiedWorkOrder(context, woId);
                } else if (tag.equals(getV().getString(R.string.workorder_archive))) {
                    workorderOptCommon(woId, null, WorkorderConstant.WORKORDER_OPT_TYPE_ARCHIVE);
                } else if (tag.equals(getV().getString(R.string.workorder_accept_order))) {
                    workorderOptCommon(woId, null, WorkorderConstant.WORKORDER_OPT_TYPE_ORDER, false);
                } else if (tag.equals(getV().getString(R.string.workorder_finish))) {
                    if (getV().isAllStart()) {
                        int allDeviceFinished = getV().isAllDeviceFinished();
                        if (allDeviceFinished == 0) {
                            workorderOptCommon(woId, null, WorkorderConstant.WORKORDER_OPT_TYPE_COMPLETION);
                        } else {
                            new FMWarnDialogBuilder(context).setIconVisible(false)
                                    .setSureBluBg(true)
                                    .setTitle(R.string.workorder_tip_title)
                                    .setSure(R.string.workorder_confirm)
                                    .setTip(String.format(getV().getString(R.string.workorder_device_un_completed_tip),allDeviceFinished))
                                    .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                                        @Override
                                        public void onClick(QMUIDialog dialog, View view) {
                                            dialog.dismiss();
                                            workorderOptCommon(woId, null, WorkorderConstant.WORKORDER_OPT_TYPE_COMPLETION);
                                        }
                                    }).create(R.style.fmDefaultWarnDialog).show();
                        }
                    } else {
                        ToastUtils.showShort(R.string.workorder_finish_error);
                    }
                } else if (tag.equals(getV().getString(R.string.workorder_stop_order))) {
                    suspendedWorkOrder(context, woId);
                } else if (tag.equals(getV().getString(R.string.workorder_back_order))) {
                    backWorkOrder(context, woId);
                } else if (tag.equals(getV().getString(R.string.workorder_approval_title))) {
                    approvalWorkOrder(context, woId, approvalId);
                } else if (tag.equals(getV().getString(R.string.workorder_continue_order))) {
                    workorderOptCommon(woId, null, WorkorderConstant.WORKORDER_OPT_TYPE_CONTINUE,
                            false);
                }
            }
        });
        builder.build().show();

    }

    //终止
    private void endWorkOrder(Context context, final Long woId) {
        FMBottomInputSheetBuilder builder = new FMBottomInputSheetBuilder(context);
        builder.setOnSaveInputListener(new FMBottomInputSheetBuilder.OnInputBtnClickListener() {
            @Override
            public void onSaveClick(QMUIBottomSheet dialog, String input) {
                dialog.dismiss();
                workorderOptCommon(woId, input, WorkorderConstant.WORKORDER_OPT_TYPE_TERMINATE);
            }

            @Override
            public void onLeftClick(QMUIBottomSheet dialog, String input) {

            }

            @Override
            public void onRightClick(QMUIBottomSheet dialog, String input) {

            }
        });
        QMUIBottomSheet build = builder.build();
        builder.getSingleBtn().setVisibility(View.VISIBLE);
        builder.setTitle(R.string.workorder_terminal);
        builder.setDescHint(R.string.workorder_hint_reason_stop);
        builder.setBtnText(R.string.workorder_stop);
        builder.setSingleBtnBg(R.drawable.btn_common_bg_selector);
        builder.setSingleNeedInput(false);
        build.show();
    }

    //审批
    private void approvalWorkOrder(Context context, final Long woId, final Long approvalId) {
        FMBottomInputSheetBuilder builder = new FMBottomInputSheetBuilder(context);
        builder.setOnSaveInputListener(new FMBottomInputSheetBuilder.OnInputBtnClickListener() {
            @Override
            public void onSaveClick(QMUIBottomSheet dialog, String input) {

            }

            @Override
            public void onLeftClick(QMUIBottomSheet dialog, String input) {
                dialog.dismiss();
                approvalWorkorder(woId, approvalId, input, WorkorderConstant.WORKORDER_APPROVAL_FAIL);
            }

            @Override
            public void onRightClick(QMUIBottomSheet dialog, String input) {
                dialog.dismiss();
                approvalWorkorder(woId, approvalId, input, WorkorderConstant.WORKORDER_APPROVAL_PASS);
            }
        });
        QMUIBottomSheet build = builder.build();
        builder.getLLTwoBtn().setVisibility(View.VISIBLE);
        builder.setTitle(R.string.workorder_approval_tip);
        builder.setDescHint(getV().getString(R.string.wrokorder_hint_reason_approval));
        builder.getRightBtn().setBackgroundResource(R.drawable.btn_common_bg_selector);
        builder.setTwoBtnLeftInput(false);
        builder.setTwoBtnRightInput(false);
        builder.setLeftBtnText(R.string.workorder_reject);
        builder.setRightBtnText(R.string.workorder_pass);
        build.show();
    }

    //审批网络请求
    private void approvalWorkorder(final Long woId, final Long approvalId, String input, int type) {
        getV().showLoading();
        WorkorderOptService.WorkorderOptApprovalVReq request = new WorkorderOptService.WorkorderOptApprovalVReq();
        request.woId = woId;
        request.approvalId = approvalId;
        request.operateType = type;
        request.content = input;
        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_OPT_APPROVAL_V_URL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.workorder_operate_success);
                        getV().setNeedJump(true);
//                        getV().setRefreshStatus(WorkorderConstant.WORK_STATUS_NONE);
                        getV().pop();
                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        ToastUtils.showShort(R.string.workorder_operate_fail);
                        super.onError(response);
                        getV().dismissLoading();
                    }
                });
    }

    //验证
    private void verifiedWorkOrder(Context context, final Long woId) {
        FMBottomInputSheetBuilder builder = new FMBottomInputSheetBuilder(context);
        builder.setOnSaveInputListener(new FMBottomInputSheetBuilder.OnInputBtnClickListener() {
            @Override
            public void onSaveClick(QMUIBottomSheet dialog, String input) {

            }

            @Override
            public void onLeftClick(QMUIBottomSheet dialog, String input) {
                dialog.dismiss();
                workorderOptCommon(woId, input, WorkorderConstant.WORKORDER_OPT_TYPE_VERIFY_FAIL);
            }

            @Override
            public void onRightClick(QMUIBottomSheet dialog, String input) {
                dialog.dismiss();
                workorderOptCommon(woId, input, WorkorderConstant.WORKORDER_OPT_TYPE_VERIFY_PASS);
            }
        });
        QMUIBottomSheet build = builder.build();
        builder.getLLTwoBtn().setVisibility(View.VISIBLE);
        builder.setTitle(R.string.workorder_tip_verify);
        builder.setDescHint(R.string.workorder_desc);
        builder.getRightBtn().setBackgroundResource(R.drawable.btn_common_bg_selector);
        builder.setTwoBtnLeftInput(false);
        builder.setTwoBtnRightInput(false);
        builder.setLeftBtnText(R.string.workorder_reject);
        builder.setRightBtnText(R.string.workorder_pass);
        build.show();
    }

    //暂停
    private void suspendedWorkOrder(Context context, final Long woId) {
        FMBottomInputSheetBuilder builder = new FMBottomInputSheetBuilder(context);
        builder.setOnSaveInputListener(new FMBottomInputSheetBuilder.OnInputBtnClickListener() {
            @Override
            public void onSaveClick(QMUIBottomSheet dialog, String input) {

            }

            @Override
            public void onLeftClick(QMUIBottomSheet dialog, String input) {
                dialog.dismiss();
                workorderOptCommon(woId, input, WorkorderConstant.WORKORDER_OPT_TYPE_SUSPENSION_NO_FURTHER);
            }

            @Override
            public void onRightClick(QMUIBottomSheet dialog, String input) {
                dialog.dismiss();
                workorderOptCommon(woId, input, WorkorderConstant.WORKORDER_OPT_TYPE_SUSPENSION_CONTINUED);
            }
        });
        QMUIBottomSheet build = builder.build();
        builder.getLLTwoBtn().setVisibility(View.VISIBLE);
        builder.setTitle(R.string.workorder_pause_tip);
        builder.setShowTip(true);
        builder.setTip(R.string.workorder_pause_tip_a);
        builder.setDescHint(R.string.workorder_pause_reason_hint);
        builder.getRightBtn().setBackgroundResource(R.drawable.btn_common_bg_selector);
        builder.setTwoBtnLeftInput(false);
        builder.setTwoBtnRightInput(false);
        builder.setLeftBtnText(R.string.workorder_over_order);
        builder.setRightBtnText(R.string.workorder_continue_order);
        build.show();
    }

    //退单
    private void backWorkOrder(Context context, final Long woId) {
        FMBottomInputSheetBuilder builder = new FMBottomInputSheetBuilder(context);
        builder.setOnSaveInputListener(new FMBottomInputSheetBuilder.OnInputBtnClickListener() {
            @Override
            public void onSaveClick(QMUIBottomSheet dialog, String input) {
                dialog.dismiss();
                workorderOptCommon(woId, input, WorkorderConstant.WORKORDER_OPT_TYPE_SINGLE_BACK);
            }

            @Override
            public void onLeftClick(QMUIBottomSheet dialog, String input) {

            }

            @Override
            public void onRightClick(QMUIBottomSheet dialog, String input) {

            }
        });
        QMUIBottomSheet build = builder.build();
        builder.getSingleBtn().setVisibility(View.VISIBLE);
        builder.setTitle(R.string.workorder_back_order);
        builder.setDescHint(R.string.workorder_back_reason_hint);
        builder.setBtnText(R.string.workorder_back_order);
        builder.setSingleBtnBg(R.drawable.btn_common_bg_selector);
        builder.setSingleNeedInput(false);
        build.show();
    }

    //通用工单操作
    private void workorderOptCommon(Long woId, String input, int type) {
        workorderOptCommon(woId, input, type, true);
    }

    private void workorderOptCommon(Long woId, String input, final int type, final boolean needJump) {
        getV().showLoading();
        WorkorderOptService.WorkorderOptCommonReq request = new WorkorderOptService.WorkorderOptCommonReq();
        request.woId = woId;
        request.operateDescription = input;
        request.operateType = type;

        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_OPT_COMMON_URL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.workorder_operate_success);
                        getV().setNeedJump(needJump);
                        if (needJump) {
                            if(type == WorkorderConstant.WORKORDER_OPT_TYPE_SUSPENSION_CONTINUED){
                                getV().setRefreshStatus(WorkorderConstant.WORK_STATUS_SUSPENDED_GO);
                                getV().setNeedJump(false);
                            }else if(type == WorkorderConstant.WORKORDER_OPT_TYPE_VERIFY_PASS){
                                getV().setRefreshStatus(WorkorderConstant.WORK_STATUS_VERIFIED);
                                getV().setNeedJump(false);
                            }
                            getV().pop();
                        } else {
                            getV().getRefreshLayout().autoRefresh();
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        ToastUtils.showShort(R.string.workorder_operate_fail);
                        super.onError(response);
                        getV().dismissLoading();
                    }
                });
    }

    @Override
    public void setPriority(Map<Long, String> p) {
        getV().setPriority(p);
    }

    @Override
    public void getWorkOrderMaterialError() {
        getV().refreshMaterialUI(null);
    }

    @Override
    public void getWorkOrderMaterialSuccess(List<WorkorderService.WorkorderReserveRocordBean> data) {
        getV().refreshMaterialUI(data);
    }
}
