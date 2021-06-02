package com.facilityone.wireless.componentservice.contract;

import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;

/**
 * Created by peter.peng on 2018/11/5.
 */

public interface ContractService extends IService {

    BaseFragment getContractInfoFragment(long contractId);
}
