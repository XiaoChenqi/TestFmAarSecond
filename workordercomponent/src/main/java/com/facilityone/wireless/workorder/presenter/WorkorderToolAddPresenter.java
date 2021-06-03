package com.facilityone.wireless.workorder.presenter;

import com.facilityone.wireless.workorder.fragment.WorkorderToolAddFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/9/27 10:44 AM
 */
public class WorkorderToolAddPresenter extends WorkorderBasePresenter<WorkorderToolAddFragment> {

    @Override
    public void onEditorWorkorderToolSuccess() {
        getV().addUpdateSuccess();
    }
}
