package com.facilityone.wireless.workorder.presenter;

import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.commonpresenter.CommonBasePresenter;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.fragment.WorkorderInputFragment;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.module.WorkorderUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;

import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/25 下午4:18
 */
public class WorkorderInputPresenter extends CommonBasePresenter<WorkorderInputFragment> {

    @Override
    public void uploadFileSuccess(List<String> ids, int type) {
        WorkorderOptService.WorkorderInputSaveReq request = getV().getWorkorderInputSaveReq();
        request.pictures = ids;
    }

    @Override
    public void uploadFileFinish(int type) {
        saveInputContent();
    }

    public void saveInputContent() {
        WorkorderOptService.WorkorderInputSaveReq request = getV().getWorkorderInputSaveReq();
        OkGo.<BaseResponse<Object>>post(FM.getApiHost()+ WorkorderUrl.WORKORDER_PROCESS_INPUT_URL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        getV().dismissLoading();
                        getV().setFragmentResult(ISupportFragment.RESULT_OK, new Bundle());
                        ToastUtils.showShort(R.string.workorder_operate_success);
                        getV().pop();
                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        super.onError(response);
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.workorder_operate_fail);
                    }
                });
    }
}
