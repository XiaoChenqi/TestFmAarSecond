package com.facilityone.wireless.workorder.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.fragment.WorkorderApprovalFragment;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderLaborerService;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.module.WorkorderUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/18 下午12:15
 */
public class WorkorderApprovalPresenter extends WorkorderBasePresenter<WorkorderApprovalFragment> {

    @Override
    public void onApproverSuccess(ArrayList<WorkorderLaborerService.WorkorderLaborerBean> members) {
        getV().setLaborers(members);
    }

    public void uploadApprovalData(Long woId, List<WorkorderLaborerService.WorkorderLaborerBean> uploadLaborers, String desc) {
        WorkorderOptService.WorkorderOptApprovalReq request = new WorkorderOptService.WorkorderOptApprovalReq();
        request.woId = woId;
        request.approverIds = new ArrayList<>();
        for (WorkorderLaborerService.WorkorderLaborerBean uploadLaborer : uploadLaborers) {
            request.approverIds.add(uploadLaborer.approverId);
        }
        request.approval = new WorkorderOptService.UploadApprovalBean();
        request.approval.templateId = null;
        request.approval.approvalType = WorkorderConstant.WORKORDER_APPROVAL_TYPE_DEFAULT;
        if (!TextUtils.isEmpty(desc)) {
            request.approval.parameters = new ArrayList<>();
            WorkorderOptService.UploadApprovalPBean bean = new WorkorderOptService.UploadApprovalPBean();
            bean.name = getV().getString(R.string.workorder_approval_parameter);
            bean.value = desc;
            request.approval.parameters.add(bean);
        }
        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_OPT_APPROVAL_URL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        getV().dismissLoading();
                        getV().setFragmentResult(ISupportFragment.RESULT_OK, new Bundle());
                        ToastUtils.showShort(R.string.workorder_operate_success);
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
}
