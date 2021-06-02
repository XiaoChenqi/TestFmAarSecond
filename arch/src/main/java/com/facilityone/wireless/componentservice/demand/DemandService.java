package com.facilityone.wireless.componentservice.demand;


import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:获取需求的首页fragment
 * Date: 2018/3/29 上午10:06
 */
public interface DemandService extends IService {

    /**
     * 和需求查询一样的进入需求详情
     *
     * @param demandId
     * @return
     */
    BaseFragment getDemandInfoByQuery(Long demandId);
    BaseFragment getDemandInfoByMsg(Long demandId);
}
