package com.facilityone.wireless.workorder.presenter;

import com.facilityone.wireless.workorder.fragment.WorkorderDeviceEditorFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/9/21 下午5:31
 */
public class WorkorderDeviceEditorPresenter extends WorkorderBasePresenter<WorkorderDeviceEditorFragment> {
    @Override
    public void onEditorWorkorderDeviceSuccess() {
        getV().saveResult();
    }
}
