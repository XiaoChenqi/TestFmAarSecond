package com.facilityone.wireless.componentservice.asset;

import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;

/**
 * Created by peter.peng on 2018/10/19.
 */

public interface AssetService extends IService {

    BaseFragment getEquipmentInfoFragment(long equipmentId,boolean showMenu);
}
