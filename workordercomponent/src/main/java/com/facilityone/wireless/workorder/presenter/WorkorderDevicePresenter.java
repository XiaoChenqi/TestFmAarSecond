package com.facilityone.wireless.workorder.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.fragment.WorkorderDeviceFragment;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.module.WorkorderUrl;
import com.fm.tool.network.model.BaseResponse;
import com.fm.tool.scan.ScanActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/9/20 下午5:02
 */
public class WorkorderDevicePresenter extends WorkorderBasePresenter<WorkorderDeviceFragment> {
    @Override
    public void onEditorWorkorderDeviceSuccess() {
        getV().refreshList();
    }

    public void scan(final WorkorderService.WorkOrderEquipmentsBean equipmentCode) {
        Intent intent = new Intent(getV().getContext(), ScanActivity.class);
        getV().startActivity(intent);

        ScanActivity.setOnScanResultListener(new ScanActivity.OnScanResultListener() {
            @Override
            public void success(String QRCode) {
                LogUtils.d("TAG", "扫描结果==" + QRCode);
                if (equipmentCode == null || TextUtils.isEmpty(QRCode) || TextUtils.isEmpty(equipmentCode.equipmentCode)) {
                    ToastUtils.showShort(R.string.workorder_qrcode_no_match);
                    return;
                }
                String[] split = QRCode.split("\\|");
                if (split.length >= 2 && split[1] != null && split[1].equals(equipmentCode.equipmentCode)) {
                    getV().result(equipmentCode);
                } else {
                    ToastUtils.showShort(R.string.workorder_qrcode_no_match);
                }
            }
        });
    }

    public void getWorkorderInfo(final Long woId) {
        getV().showLoading();
        String json = "{\"woId\":" + woId + "}";
        OkGo.<BaseResponse<WorkorderService.WorkorderInfoBean>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_INFO_URL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(json)
                .execute(new FMJsonCallback<BaseResponse<WorkorderService.WorkorderInfoBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<WorkorderService.WorkorderInfoBean>> response) {
                        WorkorderService.WorkorderInfoBean data = response.body().data;
                        if (data != null) {
                            getV().refreshEquipmentUI(data);
                        } else {
                            getV().refreshError();
                        }
                        getV().dismissLoading();
                    }

                    @Override
                    public void onError(Response<BaseResponse<WorkorderService.WorkorderInfoBean>> response) {
                        super.onError(response);
                        getV().refreshError();
                        getV().dismissLoading();
                    }
                });
    }
}
