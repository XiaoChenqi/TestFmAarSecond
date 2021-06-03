package com.facilityone.wireless.workorder.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderLaborerService;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.module.WorkorderUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/18 下午12:30
 */
public class WorkorderBasePresenter<V extends BaseFragment> extends BasePresenter<V> {
    //派工执行人
    public void getLaborerList(Long woId) {
        getV().showLoading();
        String request = "{\"userId\":" + FM.getEmId() + ",\"woId\":" + woId + "}";
        OkGo.<BaseResponse<List<WorkorderLaborerService.WorkorderLaborerGroupBean>>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_LABORER_LIST_URL)
                .tag(getV())
                .upJson(request)
                .isSpliceUrl(true)
                .execute(new FMJsonCallback<BaseResponse<List<WorkorderLaborerService.WorkorderLaborerGroupBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<WorkorderLaborerService.WorkorderLaborerGroupBean>>> response) {
                        List<WorkorderLaborerService.WorkorderLaborerGroupBean> data = response.body().data;
                        if (data != null && data.size() > 0) {
                            ArrayList<WorkorderLaborerService.WorkorderLaborerBean> members = new ArrayList<>();
                            for (WorkorderLaborerService.WorkorderLaborerGroupBean workorderLaborerGroupBean : data) {
                                if (workorderLaborerGroupBean.members != null) {
                                    members.addAll(workorderLaborerGroupBean.members);
                                }
                            }

                            onLaborerSuccess(members);
                        }
                    }

                    @Override
                    public void onFinish() {
                        getV().dismissLoading();
                    }
                });
    }

    public void onLaborerSuccess(ArrayList<WorkorderLaborerService.WorkorderLaborerBean> members) {

    }


    //审批执行人
    public void getApproverList(Long woId) {
        getV().showLoading();
        String request = "{\"postId\":" + woId + "}";
        OkGo.<BaseResponse<List<WorkorderLaborerService.WorkorderLaborerBean>>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_OPT_APPROVALS_URL)
                .tag(getV())
                .upJson(request)
                .isSpliceUrl(true)
                .execute(new FMJsonCallback<BaseResponse<List<WorkorderLaborerService.WorkorderLaborerBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<WorkorderLaborerService.WorkorderLaborerBean>>> response) {
                        List<WorkorderLaborerService.WorkorderLaborerBean> data = response.body().data;
                        onApproverSuccess((ArrayList<WorkorderLaborerService.WorkorderLaborerBean>) data);
                    }

                    @Override
                    public void onFinish() {
                        getV().dismissLoading();
                    }
                });
    }

    public void onApproverSuccess(ArrayList<WorkorderLaborerService.WorkorderLaborerBean> members) {

    }

    //编辑故障设备
    public void editorWorkorderDevice(WorkorderOptService.WorkOrderDeviceReq request) {
        getV().showLoading();
        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_EDITOR_DEVICE_URL)
                .tag(getV())
                .upJson(toJson(request))
                .isSpliceUrl(true)
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        onEditorWorkorderDeviceSuccess();
                        ToastUtils.showShort(R.string.workorder_operate_success);
                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        super.onError(response);
                        ToastUtils.showShort(R.string.workorder_device_exist);
                    }

                    @Override
                    public void onFinish() {
                        getV().dismissLoading();
                    }
                });
    }

    public void onEditorWorkorderDeviceSuccess() {
    }

    //编辑工具
    public void editorWorkorderTool(WorkorderOptService.WorkOrderToolReq request) {
        getV().showLoading();
        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_EDITOR_TOOL_URL)
                .tag(getV())
                .upJson(toJson(request))
                .isSpliceUrl(true)
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        onEditorWorkorderToolSuccess();
                        ToastUtils.showShort(R.string.workorder_operate_success);
                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        super.onError(response);
                        ToastUtils.showShort(R.string.workorder_operate_fail);
                    }

                    @Override
                    public void onFinish() {
                        getV().dismissLoading();
                    }
                });
    }

    public void onEditorWorkorderToolSuccess() {
    }

    //编辑收取明细
    public void editorWorkorderCharge(WorkorderOptService.WorkOrderChargeReq request) {
        getV().showLoading();
        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_EDITOR_CHARGE_URL)
                .tag(getV())
                .upJson(toJson(request))
                .isSpliceUrl(true)
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        onEditorWorkorderChargeSuccess();
                        ToastUtils.showShort(R.string.workorder_operate_success);
                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        super.onError(response);
                        ToastUtils.showShort(R.string.workorder_operate_fail);
                    }

                    @Override
                    public void onFinish() {
                        getV().dismissLoading();
                    }
                });
    }

    public void onEditorWorkorderChargeSuccess() {
    }
    
    
    //编辑收取明细
    public void editorWorkorderSpace(WorkorderOptService.WorkOrderSpaceReq request) {
        getV().showLoading();
        OkGo.<BaseResponse<Long>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_EDITOR_SPACE_URL)
                .tag(getV())
                .upJson(toJson(request))
                .isSpliceUrl(true)
                .execute(new FMJsonCallback<BaseResponse<Long>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Long>> response) {
                        onEditorWorkorderSpaceSuccess(response.body().data);
                        ToastUtils.showShort(R.string.workorder_operate_success);
                    }

                    @Override
                    public void onError(Response<BaseResponse<Long>> response) {
                        super.onError(response);
                        ToastUtils.showShort(R.string.workorder_operate_fail);
                    }

                    @Override
                    public void onFinish() {
                        getV().dismissLoading();
                    }
                });
    }

    public void onEditorWorkorderSpaceSuccess(Long recordId) {
    }

}
