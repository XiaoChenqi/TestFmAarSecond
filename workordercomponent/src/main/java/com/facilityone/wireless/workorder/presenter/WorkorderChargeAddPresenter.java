package com.facilityone.wireless.workorder.presenter;

import com.facilityone.wireless.workorder.fragment.WorkorderChargeAddFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/9/27 10:44 AM
 */
public class WorkorderChargeAddPresenter extends WorkorderBasePresenter<WorkorderChargeAddFragment> {

    @Override
    public void onEditorWorkorderChargeSuccess() {
        getV().addUpdateSuccess();
    }
}
