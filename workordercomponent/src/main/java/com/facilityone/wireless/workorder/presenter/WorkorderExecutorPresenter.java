package com.facilityone.wireless.workorder.presenter;

import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.fragment.WorkorderExecutorFragment;
import com.facilityone.wireless.workorder.fragment.WorkorderInfoFragment;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.module.WorkorderUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/16 下午3:38
 */
public class WorkorderExecutorPresenter extends BasePresenter<WorkorderExecutorFragment> {

    public void saveWorkTime(Long woId, Long laborerId, final Long startTime, final Long endTime) {
        getV().showLoading();
        WorkorderService.WorkLaborerTimeReq request = new WorkorderService.WorkLaborerTimeReq();
        request.woId = woId;
        request.laborerId = laborerId;
        request.actualArrivalDate = startTime;
        request.actualFinishDate = endTime;

        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_LABORER_SAVE_TIME_URL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        ToastUtils.showShort(R.string.workorder_operate_success);
                        Bundle bundle = new Bundle();
                        bundle.putLong(WorkorderInfoFragment.ARRIVAL_DATE_TIME, startTime);
                        bundle.putLong(WorkorderInfoFragment.ARRIVAL_DATE_END_TIME, endTime);
                        getV().setFragmentResult(ISupportFragment.RESULT_OK, bundle);
                        getV().pop();
                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        super.onError(response);
                        ToastUtils.showShort(R.string.workorder_operate_fail);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getV().dismissLoading();
                    }
                });
    }
}
