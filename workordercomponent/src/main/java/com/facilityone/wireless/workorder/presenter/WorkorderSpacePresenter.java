package com.facilityone.wireless.workorder.presenter;

import com.facilityone.wireless.workorder.fragment.WorkorderSpaceFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/9/20 下午5:02
 */
public class WorkorderSpacePresenter extends WorkorderBasePresenter<WorkorderSpaceFragment> {
    @Override
    public void onEditorWorkorderSpaceSuccess(Long recordId) {
        getV().refreshList();
    }
}
