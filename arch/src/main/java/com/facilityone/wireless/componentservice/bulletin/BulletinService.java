package com.facilityone.wireless.componentservice.bulletin;

import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;

/**
 * Created by peter.peng on 2018/11/12.
 */

public interface BulletinService extends IService {

    BaseFragment getBulletinInfoFragment(long bulletinId);
}
