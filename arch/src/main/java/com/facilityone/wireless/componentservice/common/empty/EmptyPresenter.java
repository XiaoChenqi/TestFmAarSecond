package com.facilityone.wireless.componentservice.common.empty;

import android.os.Bundle;

import com.blankj.utilcode.util.SPUtils;
import com.facilityone.wireless.a.arch.ec.commonpresenter.CommonBasePresenter;
import com.facilityone.wireless.a.arch.ec.module.FunctionService;
import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.componentservice.common.permissions.PermissionsManager;
import com.fm.tool.network.model.BaseResponse;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/10/15 5:24 PM
 */
public class EmptyPresenter extends CommonBasePresenter<EmptyFragment> {

    private final int type;

    public EmptyPresenter(int type) {
        this.type = type;
    }

    @Override
    public void onLogonSuccess() {
        Long pId = 10L;
        FM.getConfigurator().withProjectId(pId);

        SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.PROJECT_ID, pId);
        SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.PROJECT_NAME, "移动测试", true);

        HttpParams httpParams = new HttpParams("current_project", String.valueOf(pId));
        OkGo.getInstance().addCommonParams(httpParams);
        SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.HAVE_LOGON, true);

        getPermissions();

    }

    @Override
    public void getPermissionsSuccess(String data) {
        List<FunctionService.FunctionBean> list = PermissionsManager.HomeFunction.getInstance().show(data);

        //TODO xcq
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(IService.COMPONENT_RUNALONE, true);
//        getV().goFragment(bundle);

        //TODO

        for (FunctionService.FunctionBean bean : list) {
            if (bean.type == type) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(IService.COMPONENT_RUNALONE, true);
                if (bean.sortChildMenu != null) {
                    bundle.putSerializable(IService.FRAGMENT_CHILD_KEY, bean.sortChildMenu);

                }

                getV().goFragment(bundle);
            }
        }
    }

    @Override
    public void onLogonError() {
        getV().showLogonButton();
    }

    @Override
    public void getPermissionsError(Response<BaseResponse<List<String>>> response) {
        getV().showLogonButton();
    }
}
