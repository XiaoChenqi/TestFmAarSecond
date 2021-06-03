package com.facilityone.wireless.demand.presenter;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.module.Page;
import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.demand.fragment.DemandListFragment;
import com.facilityone.wireless.demand.module.DemandService;
import com.facilityone.wireless.demand.module.DemandUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description: 需求列表 （通用）
 * Date: 2018/6/21 下午4:07
 */
public class DemandListPresenter extends BasePresenter<DemandListFragment> {

    public void getUnFinish(Integer type, Page page, Long startTime, Long endTime, final boolean refresh) {
        DemandService.DemandListReq request = new DemandService.DemandListReq();
        request.queryType = type;
        request.page = page;
        request.timeStart = startTime;
        request.timeEnd = endTime;

        OkGo.<BaseResponse<DemandService.DemandListResp>>post(FM.getApiHost() + DemandUrl.DEMAND_LIST_URL)
                .isSpliceUrl(true)
                .tag(getV())
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<DemandService.DemandListResp>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<DemandService.DemandListResp>> response) {
                        getV().dismissLoading();
                        DemandService.DemandListResp data = response.body().data;
                        if (data == null || data.contents == null || data.contents.size() == 0) {
                            getV().refreshSuccessUI(null,data.page,refresh);
                            return;
                        }
                        getV().refreshSuccessUI(data.contents,data.page,refresh);
                    }

                    @Override
                    public void onError(Response<BaseResponse<DemandService.DemandListResp>> response) {
                        super.onError(response);
                        getV().dismissLoading();
                        getV().refreshErrorUI();
                    }
                });
    }
}
