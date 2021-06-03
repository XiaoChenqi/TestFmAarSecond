package com.facilityone.wireless.workorder.presenter;

import com.facilityone.wireless.workorder.fragment.WorkorderPaymentFragment;
import com.facilityone.wireless.workorder.module.WorkorderService;

/**
 * Created by: owen.
 * Date: on 2018/12/11 下午4:09.
 * Description:
 * email:
 */

public class WorkorderPaymentPresenter extends BaseWorkOrderPresenter<WorkorderPaymentFragment>{

    @Override
    public void getWorkorderInfoSuccess(Long woId, WorkorderService.WorkorderInfoBean data) {
        super.getWorkorderInfoSuccess(woId, data);
        getV().refreshPayment(data);
    }

    @Override
    public void getWorkorderInfoError() {
        super.getWorkorderInfoError();
    }
}
