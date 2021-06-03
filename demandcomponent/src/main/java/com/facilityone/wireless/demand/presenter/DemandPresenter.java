package com.facilityone.wireless.demand.presenter;

import com.facilityone.wireless.a.arch.ec.commonpresenter.CommonBasePresenter;
import com.facilityone.wireless.a.arch.ec.module.FunctionService;
import com.facilityone.wireless.componentservice.common.permissions.PermissionsManager;
import com.facilityone.wireless.demand.fragment.DemandFragment;
import com.facilityone.wireless.demand.module.DemandConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/6/21 上午11:18
 */
public class DemandPresenter extends CommonBasePresenter<DemandFragment> {

    @Override
    public void getUndoNumberSuccess(JSONObject data) {
        List<FunctionService.FunctionBean> functionBeanList = getV().getFunctionBeanList();
        for (FunctionService.FunctionBean functionBean : functionBeanList) {
            try {
                switch (functionBean.index) {
                    case DemandConstant.DEMAND_UNCHECK:
                        functionBean.undoNum = data.getInt(PermissionsManager.UNAPPROVALREQUIREMENTNUMBER);
                        break;
                    case DemandConstant.DEMAND_UNFINISH:
                        functionBean.undoNum = data.getInt(PermissionsManager.UNDOREQUIREMENTNUMBER);
                        break;
                    case DemandConstant.DEMAND_FINISH:
                        functionBean.undoNum = data.getInt(PermissionsManager.UNEVALUATEREQUIREMENTNUMBER);
                        break;
                    case DemandConstant.DEMAND_CREATE://创建需求
                    case DemandConstant.DEMAND_QUERY://需求查询
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getV().updateFunction(functionBeanList);
    }
}
