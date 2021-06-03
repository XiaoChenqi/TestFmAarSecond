package com.facilityone.wireless.workorder.presenter;

import com.facilityone.wireless.workorder.fragment.WorkorderSpaceAddFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/9/29 10:17 AM
 */
public class WorkorderSpaceAddPresenter extends WorkorderBasePresenter<WorkorderSpaceAddFragment> {
    @Override
    public void onEditorWorkorderSpaceSuccess(Long recordId) {
        getV().saveResult(recordId);
    }
}
