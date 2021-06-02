package com.facilityone.wireless.componentservice.inventory;

import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;

/**
 * Created by peter.peng on 2018/11/23.
 */

public interface InventoryService extends IService {
    int TYPE_FROM_WORKORDER = 3001;

    /**
     * 获取物资预定详情页面
     *
     * @param type
     * @param activityId
     * @param status
     * @return
     */
    BaseFragment getReserveRecordInfoFragment(int type, long activityId, int status,int workorderStatus);

    /**
     * 消息界面进入预定详情
     * @param activityId
     * @return
     */
    BaseFragment getReserveRecordInfoFragment(long activityId);

    /**
     * 获取物资预定界面
     *
     * @param type
     * @param woId
     * @param woCode
     * @return
     */
    BaseFragment getInventoryReserveFragment(int type, long woId, String woCode);

    /**
     * 库存查询
     *
     * @return
     */
    BaseFragment getInventoryQueryFragment();

    /**
     * 物料详情
     *
     * @param inventoryId
     * @return
     */
    BaseFragment getMaterialInfoFragment(Long inventoryId);
}
