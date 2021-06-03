package com.facilityone.wireless.workorder.presenter;

import android.content.Context;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.module.AttachmentBean;
import com.facilityone.wireless.a.arch.ec.module.Page;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.fragment.WorkorderQueryFragment;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.module.WorkorderUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.facilityone.wireless.basiclib.app.FM.getApiHost;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * ate: 2018/7/4 下午4:16
 */
public class WorkorderQueryPresenter extends BaseWorkOrderPresenter<WorkorderQueryFragment> {

    public void getConditionWorkorderList(final Page page, WorkorderService.WorkorderConditionBean conditionBean, final boolean refresh, boolean my) {
        WorkorderService.WorkorderQueryReq request = new WorkorderService.WorkorderQueryReq();
        request.page = page;
        String url = "";
        if (my) {
            url = FM.getApiHost() + WorkorderUrl.WORKORDER_LIST_TO_MY_QUERY_URL;
            WorkorderService.WorkorderConditionBean workorderConditionBean = new WorkorderService.WorkorderConditionBean();
            workorderConditionBean.emId = FM.getUserId();
            request.searchCondition = workorderConditionBean;
        }else {
            url = getApiHost() + WorkorderUrl.WORKORDER_LIST_TO_QUERY_URL;
            request.searchCondition = conditionBean;
        }
        OkGo.<BaseResponse<WorkorderService.WorkorderListResp>>post(url)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<WorkorderService.WorkorderListResp>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<WorkorderService.WorkorderListResp>> response) {
                        WorkorderService.WorkorderListResp data = response.body().data;
                        if (data == null || data.contents == null || data.contents.size() == 0) {
                            getV().refreshSuccessUI(null, data == null ? null : data.page, refresh);
                            return;
                        }
                        getV().refreshSuccessUI(data.contents, data.page, refresh);
                    }

                    @Override
                    public void onError(Response<BaseResponse<WorkorderService.WorkorderListResp>> response) {
                        super.onError(response);
                        getV().refreshErrorUI();
                    }
                });
    }

    /**
     * 工单状态
     *
     * @param context
     * @return
     */
    public List<AttachmentBean> getWorkorderStatus(Context context) {
        List<AttachmentBean> ts = new ArrayList<>();
        String[] stringArray = context.getResources().getStringArray(R.array.workorder_status);

        if (stringArray.length > 0) {
            for (int i = 0, length = stringArray.length - 1; i < length; i++) {
                AttachmentBean tb = new AttachmentBean();
                tb.value = (long) i;
                tb.name = stringArray[i];
                ts.add(tb);
            }
        }

        return ts;
    }

    @Override
    public void getPriority(List<AttachmentBean> as) {
        getV().setPriorityAs(as);
    }

    @Override
    public void setPriority(Map<Long, String> p) {
        getV().setPriority(p);
    }
}
