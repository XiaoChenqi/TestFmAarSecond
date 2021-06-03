package com.facilityone.wireless.workorder.presenter;

import com.facilityone.wireless.a.arch.ec.commonpresenter.CommonBasePresenter;
import com.facilityone.wireless.a.arch.ec.module.FunctionService;
import com.facilityone.wireless.componentservice.common.permissions.PermissionsManager;
import com.facilityone.wireless.workorder.fragment.WorkorderMenuFragment;
import com.facilityone.wireless.workorder.module.WorkorderConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/3 下午4:18
 */
public class WorkorderMenuPresenter extends CommonBasePresenter<WorkorderMenuFragment> {

    @Override
    public void getUndoNumberSuccess(JSONObject data) {
        List<FunctionService.FunctionBean> functionBeanList = getV().getFunctionBeanList();
        for (FunctionService.FunctionBean functionBean : functionBeanList) {
            try {
                switch (functionBean.index) {
                    case WorkorderConstant.WORKORER_PROCESS:
                        //待处理工单
                        functionBean.undoNum = data.getInt(PermissionsManager.UNDOORDERNUMBER);
                        break;
                    case WorkorderConstant.WORKORER_DISPATCHING:
                        //待派工工单
                        functionBean.undoNum = data.getInt(PermissionsManager.UNARRANGEORDERNUMBER);
                        break;
                    case WorkorderConstant.WORKORER_AUDIT:
                        //待审批工单
                        functionBean.undoNum = data.getInt(PermissionsManager.UNAPPROVALORDERNUMBER);
                        break;
                    case WorkorderConstant.WORKORER_ARCHIVE:
                        //待存档工单
                        functionBean.undoNum = data.getInt(PermissionsManager.UNARCHIVEDORDERNUMBER);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getV().updateFunction(functionBeanList);
    }
}
