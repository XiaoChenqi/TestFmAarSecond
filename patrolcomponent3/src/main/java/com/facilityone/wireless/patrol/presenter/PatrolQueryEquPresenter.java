package com.facilityone.wireless.patrol.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.facilityone.wireless.a.arch.offline.model.service.PatrolDbService;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.fragment.PatrolQueryEquFragment;
import com.facilityone.wireless.patrol.module.PatrolQueryService;
import com.facilityone.wireless.patrol.module.PatrolUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检查询设备
 * Date: 2018/11/22 11:21 AM
 */
public class PatrolQueryEquPresenter extends BasePresenter<PatrolQueryEquFragment> {
    /**
     * 设备信息
     *
     * @param taskId
     * @param spotId
     * @param equId
     */
    public void requestData(long taskId, long spotId, final long equId) {
        getV().showLoading();
        final PatrolQueryService.PatrolQueryEquReq req = new PatrolQueryService.PatrolQueryEquReq();
        req.equipmentId = equId;
        req.patrolTaskId = taskId;
        req.patrolTaskSpotId = spotId;

        OkGo.<BaseResponse<PatrolQueryService.PatrolQueryEquResp>>post(FM.getApiHost() + PatrolUrl.PATROL_TASK_QUERY_EQU_ITEM)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(req))
                .execute(new FMJsonCallback<BaseResponse<PatrolQueryService.PatrolQueryEquResp>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<PatrolQueryService.PatrolQueryEquResp>> response) {
                        PatrolQueryService.PatrolQueryEquResp data = response.body().data;
                        if (data != null) {
                            if (equId != PatrolDbService.COMPREHENSIVE_EQU_ID) {
                                getV().setTitle(StringUtils.formatString(data.code));
                            }
                            getV().refreshUI(data);
                            getV().dismissLoading();
                        } else {
                            getV().dismissLoading();
                            ToastUtils.showShort(R.string.patrol_get_data_error);
                            getV().pop();
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<PatrolQueryService.PatrolQueryEquResp>> response) {
                        super.onError(response);
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.patrol_get_data_error);
                        getV().pop();
                    }
                });
    }

    /**
     * 标记为已处理
     *
     * @param patrolTaskSpotResultId
     */
    public void optTagDel(Long patrolTaskSpotResultId) {
        getV().showLoading();
        String json = "{\"contentId\":" + patrolTaskSpotResultId + "}";
        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + PatrolUrl.PATROL_TASK_QUERY_EQU_ITEM_MARK_DEL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(json)
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        getV().requestData();
                        ToastUtils.showShort(R.string.patrol_operate_ok);
                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        super.onError(response);
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.patrol_operate_fail);
                    }
                });

    }
}
