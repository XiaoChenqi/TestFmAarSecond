package com.facilityone.wireless.patrol.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.commonpresenter.CommonBasePresenter;
import com.facilityone.wireless.a.arch.ec.module.FunctionService;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.a.arch.offline.util.PatrolQrcodeUtils;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.componentservice.common.permissions.PermissionsManager;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.fragment.PatrolMenuFragment;
import com.facilityone.wireless.patrol.module.PatrolConstant;
import com.facilityone.wireless.patrol.module.PatrolUrl;
import com.fm.tool.network.model.BaseResponse;
import com.fm.tool.scan.ScanActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检菜单
 * Date: 2018/10/30 3:01 PM
 */
public class PatrolMenuPresenter extends CommonBasePresenter<PatrolMenuFragment> {
    @Override
    public void getUndoNumberSuccess(JSONObject data) {
        List<FunctionService.FunctionBean> functionBeanList = getV().getFunctionBeanList();
        for (FunctionService.FunctionBean functionBean : functionBeanList) {
            try {
                switch (functionBean.index) {
                    case PatrolConstant.PATROL_TASK:
                        //巡检任务
                        functionBean.undoNum = data.getInt(PermissionsManager.PATROLTASKNUMBER);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getV().updateFunction(functionBeanList);
    }

    /**
     * 二维码扫描相关逻辑
     */
    public void scan() {
        Intent intent = new Intent(getV().getContext(), ScanActivity.class);
        getV().startActivity(intent);

        ScanActivity.setOnScanResultListener(new ScanActivity.OnScanResultListener() {
            @Override
            public void success(String QRCode) {
                LogUtils.d("TAG", "扫描结果==" + QRCode);
                String spotCode = PatrolQrcodeUtils.parseSpotCode(QRCode);
                if (TextUtils.isEmpty(QRCode)) {
                    ToastUtils.showShort(R.string.patrol_qrcode_no_match);
                    return;
                }
                getV().scanResult(spotCode);
            }
        });
    }

    /**
     * 此项目是否需要nfc才可以工作
     */
    public void requestProjectNeedNfc() {
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("projectId", FM.getProjectId());
        OkGo.<BaseResponse<Integer>>post(FM.getApiHost() + PatrolUrl.PATROL_NEED_NFC_DEL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(jsonObject))
                .execute(new FMJsonCallback<BaseResponse<Integer>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Integer>> response) {
                        Integer data = response.body().data;
                        if (data != null) {
                            SPUtils.getInstance(SPKey.SP_MODEL_PATROL).put(SPKey.PATROL_NEED_NFC, data);
                            getV().refreshScan(data == PatrolConstant.PATROL_NEED_NFC);
                        }
                    }
                });
    }
}
