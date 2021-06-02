package com.facilityone.wireless.componentservice.patrol;

import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:获取巡检首页
 * Date: 2018/10/30 2:56 PM
 */
public interface PatrolService extends IService {
    BaseFragment getPatrolQuerySpotFragment(Long patrolTaskId, String title);
}
