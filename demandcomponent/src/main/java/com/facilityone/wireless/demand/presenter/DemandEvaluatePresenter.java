package com.facilityone.wireless.demand.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.demand.R;
import com.facilityone.wireless.demand.fragment.DemandEvaluateFragment;
import com.facilityone.wireless.demand.module.DemandService;
import com.facilityone.wireless.demand.module.DemandUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:评价
 * Date: 2018/7/3 下午2:40
 */
public class DemandEvaluatePresenter extends BasePresenter<DemandEvaluateFragment> {
    //评价
    public void uploadEvaluate(DemandService.DemandEvaluateReq request) {
        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + DemandUrl.DEMAND_EVALUATE_URL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.demand_evaluate_success);
                        getV().uploadSuccess();
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
