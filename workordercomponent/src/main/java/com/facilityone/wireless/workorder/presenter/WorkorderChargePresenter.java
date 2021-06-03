package com.facilityone.wireless.workorder.presenter;

import com.facilityone.wireless.workorder.fragment.WorkorderChargeFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/9/25 4:07 PM
 */
public class WorkorderChargePresenter extends WorkorderBasePresenter<WorkorderChargeFragment> {
    @Override
    public void onEditorWorkorderChargeSuccess() {
        getV().refreshList();
    }
}
