package com.facilityone.wireless.workorder.presenter;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.module.Page;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.workorder.fragment.WorkorderListFragment;
import com.facilityone.wireless.workorder.module.WorkorderConstant;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.module.WorkorderUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.Map;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单列表操作页面
 * Date: 2018/7/4 下午3:50
 */
public class WorkorderListPresenter extends BaseWorkOrderPresenter<WorkorderListFragment> {

    //请求工单列表
    public void getWorkorderList(Integer type, Page page, final boolean refresh) {
        getV().showLoading();
        final WorkorderService.WorkorderListReq request = new WorkorderService.WorkorderListReq();
        request.page = page;
        String url = "";
        switch (type) {
            case WorkorderConstant.WORKORER_PROCESS:
                url = WorkorderUrl.WORKORDER_LIST_UNDO_URL;
                break;
            case WorkorderConstant.WORKORER_DISPATCHING:
                url = WorkorderUrl.WORKORDER_LIST_DISPATCH_URL;
                break;
            case WorkorderConstant.WORKORER_AUDIT:
                url = WorkorderUrl.WORKORDER_LIST_APPROVAL_URL;
                break;
            case WorkorderConstant.WORKORER_ARCHIVE:
                url = WorkorderUrl.WORKORDER_LIST_TO_CLOSED_URL;
                break;
        }
        OkGo.<BaseResponse<WorkorderService.WorkorderListResp>>post(FM.getApiHost() + url)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<WorkorderService.WorkorderListResp>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<WorkorderService.WorkorderListResp>> response) {
                        getV().dismissLoading();
                        WorkorderService.WorkorderListResp data = response.body().data;
                        if (data == null || data.contents == null || data.contents.size() == 0) {
                            getV().refreshSuccessUI(null, data == null ? request.page : data.page, refresh);
                            return;
                        }
                        getV().refreshSuccessUI(data.contents, data.page, refresh);

                    }

                    @Override
                    public void onError(Response<BaseResponse<WorkorderService.WorkorderListResp>> response) {
                        super.onError(response);
                        getV().dismissLoading();
                        getV().refreshErrorUI();
                    }
                });
    }

    @Override
    public void setPriority(Map<Long, String> p) {
        getV().setPriority(p);
    }
}
