package com.facilityone.wireless.demand.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.commonpresenter.CommonBasePresenter;
import com.facilityone.wireless.a.arch.ec.module.CommonUrl;
import com.facilityone.wireless.a.arch.ec.module.ConstantMeida;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.demand.R;
import com.facilityone.wireless.demand.fragment.DemandCreateFragment;
import com.facilityone.wireless.demand.module.DemandCreateService;
import com.facilityone.wireless.demand.module.DemandUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/6/21 下午4:07
 */
public class DemandCreatePresenter extends CommonBasePresenter<DemandCreateFragment> {
    @Override
    public void uploadFileSuccess(List<String> ids, int type) {
        switch (type) {
            case ConstantMeida.IMAGE:
                if (ids != null) {
                    getV().getRequest().photoIds = ids;
                }
                break;
            case ConstantMeida.VIDEO:
                if (ids != null) {
                    getV().getRequest().videoIds = ids;
                }
                break;
            case ConstantMeida.AUDIO:
                if (ids != null) {
                    getV().getRequest().audioIds = ids;
                }
                break;
        }
    }

    @Override
    public void uploadFileFinish(int type) {
        switch (type) {
            case ConstantMeida.IMAGE:
                if (getV().getVideoSelectList().size() > 0) {
                    uploadFile(getV().getVideoSelectList(), CommonUrl.UPLOAD_VIDEO_URL, ConstantMeida.VIDEO);
                } else if (getV().getAudioSelectList().size() > 0) {
                    uploadFile(getV().getAudioSelectList(), CommonUrl.UPLOAD_VOICE_URL, ConstantMeida.AUDIO);
                } else {
                    createDemand();
                }
                break;

            case ConstantMeida.VIDEO:
                if (getV().getAudioSelectList().size() > 0) {
                    uploadFile(getV().getAudioSelectList(), CommonUrl.UPLOAD_VOICE_URL, ConstantMeida.AUDIO);
                } else {
                    createDemand();
                }
                break;
            case ConstantMeida.AUDIO:
                createDemand();
                break;
        }
    }

    public void createDemand() {
        DemandCreateService.DemandCreateReq request = getV().getRequest();
        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + DemandUrl.DEMAND_CREATE_URL)
                .isSpliceUrl(true)
                .tag(getV())
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.demand_create_success);
                        getV().pop();
                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        super.onError(response);
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.demand_create_failed);
                    }
                });

    }

    @Override
    public void getUserInfo() {
        getV().showLoading();
        super.getUserInfo();
    }

    @Override
    public void getUserInfoSuccess(String toJson) {
        getV().refreshUserInfo(toJson);
        getV().dismissLoading();
    }
}
