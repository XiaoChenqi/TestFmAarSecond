package com.facilityone.wireless.workorder.module;

import com.facilityone.wireless.a.arch.ec.module.LocationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单操作
 * Date: 2018/7/17 下午5:41
 */
public class WorkorderOptService {
    //派工
    public static class WorkorderOptDispatchReq {
        public Long woId;
        public Long estimatedArrivalDate;
        public Long estimatedCompletionDate;
        public Long estimatedWorkingTime;
        public String sendWorkContent;
        public List<UploadLaborerBean> laborers;

        public WorkorderOptDispatchReq() {
            this.laborers = new ArrayList<>();
        }
    }

    public static class UploadLaborerBean {
        public Long laborerId;
        public boolean responsible;
    }

    //工单操作通用
    public static class WorkorderOptCommonReq {
        public Long woId;
        public Integer operateType;
        public String operateDescription;
    }

    //审批申请
    public static class WorkorderOptApprovalReq {
        public Long woId;
        public List<Long> approverIds;
        public UploadApprovalBean approval;
    }

    public static class UploadApprovalBean {
        public Long templateId;
        public Integer approvalType;
        public List<UploadApprovalPBean> parameters;
    }

    public static class UploadApprovalPBean {
        public String name;
        public String value;
    }

    //审批
    public static class WorkorderOptApprovalVReq {
        public Long woId;
        public Long approvalId;
        public Integer operateType;
        public String content;
    }

    /* 处理工单操作 */
    //填写内容
    public static class WorkorderInputSaveReq {
        public Long woId;
        public String workContent;
        public List<String> pictures;
    }

    //故障设备
    public static class WorkOrderDeviceReq {
        public Long woId;
        public Long equipmentId;
        public Integer operateType;
        public String failureDesc;
        public String repairDesc;
    }
    
    //空间位置
    public static class WorkOrderSpaceReq {
        public Long woId;
        public Long recordId;
        public Integer operateType;
        public String repairDesc;
        public LocationBean location;
    }

    //工具
    public static class WorkOrderToolReq {
        public Long woId;
        public Long toolId;
        public Integer operateType;
        public Integer amount;
        public Double cost;
        public String name;
        public String model;
        public String unit;
        public String comment;
    }
    
    //收取明细
    public static class WorkOrderChargeReq {
        public Long woId;
        public Long chargeId;
        public Integer operateType;
        public Double amount;
        public String name;
    }
}
