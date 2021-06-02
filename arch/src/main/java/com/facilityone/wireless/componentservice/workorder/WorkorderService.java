package com.facilityone.wireless.componentservice.workorder;

import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:获取工单的首页fragment
 * Date: 2018/7/3 下午4:13
 */
public interface WorkorderService extends IService {

    int CREATE_ORDER_BY_OTHER = 2001;//其他页面来创建工单
    int CREATE_ORDER_BY_PATROL_QUERY_REPAIR = 2002;//巡检查询报修

    BaseFragment getWorkorderInfoFragment(int workorderStatus, String code, Long woId);

    BaseFragment getWorkorderQueryFragment(boolean my);

    BaseFragment getWorkorderCreateFragment(int fromType, long equipmentId);

    //巡检需求报障
    BaseFragment getWorkorderCreateFragment(int fromType,
                                            long equipmentId,
                                            String locationName,
                                            LocationBean locationBean,
                                            List<LocalMedia> localMedias,
                                            Long patrolTaskSpotResultId,
                                            String desc,
                                            Long demandId,
                                            String phone,
                                            String people);

    //图片是否加水印
    BaseFragment getWorkorderCreateFragment(int fromType, long equipmentId
            , String locationName, LocationBean locationBean
            , List<LocalMedia> localMedias, Long itemId
            , String desc, Long demandId, String phone, String people, boolean waterMark);
}
