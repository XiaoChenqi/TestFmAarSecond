package com.facilityone.wireless.workorder.module;

import com.facilityone.wireless.a.arch.ec.module.LocationBean;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:创建工单
 * Date: 2018/7/4 上午9:23
 */
public class WorkorderCreateService {

    //创建工单请求
    public static class WorkorderCreateReq {
        public Long userId;
        public String name;
        public String phone;
        public Long organizationId;
        public Long serviceTypeId;
        public String scDescription;
        public Long priorityId;
        public LocationBean location;
        public Long processId;
        public Long patrolItemDetailId;
        public Long reqId;
        public Long woType;
        public List<Long> equipmentIds;
        public List<String> pictures;
    }
}
