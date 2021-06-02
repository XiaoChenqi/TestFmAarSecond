package com.facilityone.wireless.a.arch.ec.selectdata;

import android.text.TextUtils;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.module.CommonUrl;
import com.facilityone.wireless.a.arch.ec.module.WorkTeamBean;
import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.facilityone.wireless.basiclib.app.FM;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/11/19 2:32 PM
 */
public class WorkTeamSelectPresenter extends BasePresenter<WorkTeamSelectFragment> {


    public void filter(String curCharacter, List<WorkTeamBean> total) {
        if (TextUtils.isEmpty(curCharacter) || total == null) {
            getV().refreshHaveData(total);
            return;
        }
        curCharacter = curCharacter.replace(" ","");
        String lowerCase = curCharacter.toLowerCase();
        List<WorkTeamBean> temp = new ArrayList<>();
        for (WorkTeamBean bean : total) {
            if (bean.workTeamName != null) {
                String lowerCaseName = bean.workTeamName.toLowerCase();
                if (lowerCaseName.contains(lowerCase)) {
                    temp.add(bean);
                }
            }
        }
        getV().refreshHaveData(temp);

    }

    /**
     * 请求工作组
     */
    public void requestWorkTeam() {
        String json = "{\"projectId\":" + FM.getProjectId() + "}";
        OkGo.<BaseResponse<List<WorkTeamBean>>>post(FM.getApiHost() + CommonUrl.WORK_TEAM_URL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(json)
                .execute(new FMJsonCallback<BaseResponse<List<WorkTeamBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<WorkTeamBean>>> response) {
                        List<WorkTeamBean> data = response.body().data;
                        getV().setTotal(data);
                        getV().refreshHaveData(data);
                        getV().dismissLoading();
                    }

                    @Override
                    public void onError(Response<BaseResponse<List<WorkTeamBean>>> response) {
                        super.onError(response);
                        getV().error();
                        getV().dismissLoading();
                    }
                });


    }
}
