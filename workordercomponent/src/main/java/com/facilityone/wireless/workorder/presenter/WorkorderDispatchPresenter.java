package com.facilityone.wireless.workorder.presenter;

import android.os.Bundle;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.fragment.WorkorderDispatchFragment;
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
 * Date: 2018/7/17 上午10:20
 */
public class WorkorderDispatchPresenter extends WorkorderBasePresenter<WorkorderDispatchFragment> {

    @Override
    public void onLaborerSuccess(ArrayList<WorkorderLaborerService.WorkorderLaborerBean> members) {
        getV().setLaborers(members);
    }

    /**
     * 派工
     *
     * @param woId
     * @param startTime
     * @param endTime
     * @param desc
     * @param uploadLaborers
     */
    public void uploadDispatchData(Long woId,
                                   Long startTime,
                                   Long endTime,
                                   String desc,
                                   List<WorkorderLaborerService.WorkorderLaborerBean> uploadLaborers) {

        WorkorderOptService.WorkorderOptDispatchReq request = new WorkorderOptService.WorkorderOptDispatchReq();
        request.woId = woId;
        request.estimatedArrivalDate = startTime;
        request.estimatedCompletionDate = endTime;
        if (startTime != null && endTime != null) {
            request.estimatedWorkingTime = TimeUtils.getTimeSpan(endTime, startTime, TimeConstants.MIN);
        }
        request.sendWorkContent = desc;
        for (WorkorderLaborerService.WorkorderLaborerBean uploadLaborer : uploadLaborers) {
            WorkorderOptService.UploadLaborerBean b = new WorkorderOptService.UploadLaborerBean();
            b.responsible = uploadLaborer.leader;
            b.laborerId = uploadLaborer.emId;
            request.laborers.add(b);
        }

        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_OPT_DISPATCH_URL)
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
