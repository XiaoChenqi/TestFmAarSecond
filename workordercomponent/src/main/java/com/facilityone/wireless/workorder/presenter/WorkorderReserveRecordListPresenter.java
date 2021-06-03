package com.facilityone.wireless.workorder.presenter;

import com.facilityone.wireless.workorder.fragment.WorkorderReserveRecordListFragment;
import com.facilityone.wireless.workorder.module.WorkorderService;

import java.util.List;

/**
 * Created by peter.peng on 2018/12/14.
 */

public class WorkorderReserveRecordListPresenter extends BaseWorkOrderPresenter<WorkorderReserveRecordListFragment> {

    @Override
    public void getWorkOrderMaterialSuccess(List<WorkorderService.WorkorderReserveRocordBean> data) {
        getV().getWorkorderReserveRecordListSuccess(data);
    }

    @Override
    public void getWorkOrderMaterialError() {
        getV().getWorkorderReserveRecordListError();
    }
}
