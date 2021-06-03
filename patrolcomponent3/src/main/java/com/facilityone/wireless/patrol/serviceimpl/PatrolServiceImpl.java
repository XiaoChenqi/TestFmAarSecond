package com.facilityone.wireless.patrol.serviceimpl;

import android.os.Bundle;

import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.componentservice.patrol.PatrolService;
import com.facilityone.wireless.patrol.fragment.PatrolMenuFragment;
import com.facilityone.wireless.patrol.fragment.PatrolQuerySpotFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/10/30 2:55 PM
 */
public class PatrolServiceImpl implements PatrolService {
    @Override
    public BaseFragment getFragment(Bundle bundle) {
        return PatrolMenuFragment.getInstance(bundle);
    }

    @Override
    public BaseFragment getPatrolQuerySpotFragment(Long patrolTaskId, String title) {
        return PatrolQuerySpotFragment.getInstance(patrolTaskId,title);
    }
}
