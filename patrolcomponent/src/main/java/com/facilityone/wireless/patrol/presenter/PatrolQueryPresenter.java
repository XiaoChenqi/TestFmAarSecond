package com.facilityone.wireless.patrol.presenter;

import android.content.Context;
import android.support.annotation.ArrayRes;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.module.AttachmentBean;
import com.facilityone.wireless.a.arch.ec.module.Page;
import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.fragment.PatrolQueryFragment;
import com.facilityone.wireless.patrol.module.PatrolQueryService;
import com.facilityone.wireless.patrol.module.PatrolUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检查询
 * Date: 2018/11/19 9:40 AM
 */
public class PatrolQueryPresenter extends BasePresenter<PatrolQueryFragment> {
    /**
     * 获取巡检查询任务
     *
     * @param page
     * @param conditionBean
     * @param refresh
     */
    public void getConditionList(Page page, PatrolQueryService.PatrolQueryConditionBean conditionBean, final boolean refresh) {
        PatrolQueryService.PatrolQueryReq request = new PatrolQueryService.PatrolQueryReq();
        request.searchCondition = conditionBean;
        request.page = page;
        OkGo.<BaseResponse<PatrolQueryService.PatrolQueryResp>>post(FM.getApiHost() + PatrolUrl.PATROL_TASK_QUERY)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<PatrolQueryService.PatrolQueryResp>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<PatrolQueryService.PatrolQueryResp>> response) {
                        PatrolQueryService.PatrolQueryResp data = response.body().data;
                        getV().refreshSuccessUI(data.contents, data.page, refresh);
                    }

                    @Override
                    public void onError(Response<BaseResponse<PatrolQueryService.PatrolQueryResp>> response) {
                        super.onError(response);
                        getV().refreshErrorUI();
                    }
                });
    }

    /**
     * 任务或点位状态
     *
     * @param context
     * @return
     */
    public List<AttachmentBean> getStatus(Context context, @ArrayRes int arrayId) {
        List<AttachmentBean> ts = new ArrayList<>();
        String[] stringArray = context.getResources().getStringArray(arrayId);

        if (stringArray.length > 0) {
            for (int i = 0, length = stringArray.length; i < length; i++) {
                AttachmentBean tb = new AttachmentBean();
                tb.value =  i;
                tb.name = stringArray[i];
                ts.add(tb);
            }
        }

        return ts;
    }

    /**
     * 获取排序方式
     * @param context
     * @return
     */
    public List<AttachmentBean> getSortOrder(Context context) {
        List<AttachmentBean> ts = new ArrayList<>();
        String[] stringArray = context.getResources().getStringArray(R.array.patrol_sort_order_label);

        if (stringArray.length > 0) {
            for (int i = 0, length = stringArray.length; i < length; i++) {
                AttachmentBean tb = new AttachmentBean();
                if (i == 0){
                    tb.check = true;
                }
                tb.value = i;
                tb.name = stringArray[i];
                ts.add(tb);
            }
        }

        return ts;

    }
}
