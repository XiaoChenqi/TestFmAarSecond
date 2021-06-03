package com.facilityone.wireless.workorder.presenter;

import com.facilityone.wireless.workorder.fragment.WorkorderToolFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/9/25 4:07 PM
 */
public class WorkorderToolPresenter extends WorkorderBasePresenter<WorkorderToolFragment> {
    @Override
    public void onEditorWorkorderToolSuccess() {
        getV().refreshList();
    }
}
