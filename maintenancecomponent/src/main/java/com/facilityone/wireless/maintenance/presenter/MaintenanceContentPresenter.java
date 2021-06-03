package com.facilityone.wireless.maintenance.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.commonpresenter.CommonBasePresenter;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.maintenance.R;
import com.facilityone.wireless.maintenance.fragment.MaintenanceContentFragment;
import com.facilityone.wireless.maintenance.model.MaintenanceService;
import com.facilityone.wireless.maintenance.model.MaintenanceUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.Locale;

/**
 * Created by peter.peng on 2018/11/20.
 */

public class MaintenanceContentPresenter extends CommonBasePresenter<MaintenanceContentFragment> {

    /**
     * 获取当前语言
     * @return
     */
    public String getCurLanguage() {
        Locale locale = getV().getContext().getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh")) {
            return "zh_CN";
        } else if (language.endsWith("en")) {
            return "en_US";
        } else {
            return "zh_CN";
        }
    }

    /**
     * 联网获取计划性维护详情信息
     *
     * @param postId
     * @param todoId
     */
    public void getMaintenanceInfo(Long postId, Long todoId) {
        getV().showLoading();
        String request = "{\"postId\":" + postId + ",\"todoId\":" + todoId + "}";
        OkGo.<BaseResponse<MaintenanceService.MaintenanceInfoBean>>post(FM.getApiHost() + MaintenanceUrl.MAINTENANCE_INFO_URL)
                .isSpliceUrl(true)
                .tag(getV())
                .upJson(request)
                .execute(new FMJsonCallback<BaseResponse<MaintenanceService.MaintenanceInfoBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<MaintenanceService.MaintenanceInfoBean>> response) {
                        MaintenanceService.MaintenanceInfoBean data = response.body().data;
                        if (data != null) {
                            getV().setMaintenanceInfo(data);
                        } else {
                            getV().dismissLoading();
                            ToastUtils.showShort(R.string.maintenance_data_error);
                            getV().pop();
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<MaintenanceService.MaintenanceInfoBean>> response) {
                        super.onError(response);
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.maintenance_data_error);
                        getV().pop();
                    }
                });
    }
}
