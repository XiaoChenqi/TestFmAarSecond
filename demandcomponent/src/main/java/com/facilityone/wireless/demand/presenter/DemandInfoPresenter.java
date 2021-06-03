package com.facilityone.wireless.demand.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.commonpresenter.CommonBasePresenter;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.demand.R;
import com.facilityone.wireless.demand.fragment.DemandInfoFragment;
import com.facilityone.wireless.demand.module.DemandService;
import com.facilityone.wireless.demand.module.DemandUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/6/28 下午6:08
 */
public class DemandInfoPresenter extends CommonBasePresenter<DemandInfoFragment> {

    //获取需求详情
    public void getDemandInfo(Long demandId) {
        getV().showLoading();
        final String request = "{\"reqId\":" + demandId + "}";
        OkGo.<BaseResponse<DemandService.DemandInfoBean>>post(FM.getApiHost() + DemandUrl.DEMAND_INFO_URL)
                .isSpliceUrl(true)
                .tag(this)
                .upJson(request)
                .execute(new FMJsonCallback<BaseResponse<DemandService.DemandInfoBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<DemandService.DemandInfoBean>> response) {
                        DemandService.DemandInfoBean data = response.body().data;
                        if (data != null) {
                            getV().updateUI(data);
                        }
                        getV().dismissLoading();
                    }

                    @Override
                    public void onError(Response<BaseResponse<DemandService.DemandInfoBean>> response) {
                        ToastUtils.showShort(R.string.demand_operate_fail);
                        getV().hideShowMoreMenu(false);
                        getV().dismissLoading();
                    }
                });

    }

    //需求操作 需求审核，保存，完成
    public void optDemand(final DemandService.DemandOptReq request) {
        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + DemandUrl.DEMAND_OPT_URL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        getV().solveResult(request);
                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        super.onError(response);
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.demand_operate_fail);
                    }
                });
    }
}
