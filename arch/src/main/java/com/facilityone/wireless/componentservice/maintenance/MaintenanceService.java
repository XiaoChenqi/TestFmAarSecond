package com.facilityone.wireless.componentservice.maintenance;

import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;

/**
 * Created by peter.peng on 2018/11/15.
 */

public interface MaintenanceService extends IService {
    BaseFragment getPmInfo(Long pmId, Long todoId);
}
