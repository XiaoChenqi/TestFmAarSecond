package com.facilityone.wireless.demand.serviceimpl;

import android.os.Bundle;

import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.componentservice.demand.DemandService;
import com.facilityone.wireless.demand.fragment.DemandFragment;
import com.facilityone.wireless.demand.fragment.DemandInfoFragment;
import com.facilityone.wireless.demand.module.DemandConstant;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/3/29 上午10:09
 */
public class DemandServiceImpl implements DemandService {

    @Override
    public BaseFragment getFragment(Bundle bundle) {
        return DemandFragment.getInstance(bundle);
    }

    @Override
    public BaseFragment getDemandInfoByQuery(Long demandId) {
        return DemandInfoFragment.getInstance(DemandConstant.DEMAND_REQUES_QUERY, demandId);
    }

    @Override
    public BaseFragment getDemandInfoByMsg(Long demandId) {
        return DemandInfoFragment.getInstance(DemandConstant.DEMAND_REQUES_QUERY, demandId, true);
    }
}
