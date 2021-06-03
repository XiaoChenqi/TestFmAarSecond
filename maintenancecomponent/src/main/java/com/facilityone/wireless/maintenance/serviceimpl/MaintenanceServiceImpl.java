package com.facilityone.wireless.maintenance.serviceimpl;

import android.os.Bundle;

import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.componentservice.maintenance.MaintenanceService;
import com.facilityone.wireless.maintenance.fragment.MaintenanceContentFragment;
import com.facilityone.wireless.maintenance.fragment.MaintenanceFragment;

/**
 * Created by peter.peng on 2018/11/15.
 */

public class MaintenanceServiceImpl implements MaintenanceService {
    @Override
    public BaseFragment getFragment(Bundle bundle) {
        return MaintenanceFragment.getInstance();
    }

    @Override
    public BaseFragment getPmInfo(Long pmId, Long todoId) {
        return MaintenanceContentFragment.getInstance(true, pmId, todoId);
    }
}
