package com.facilityone.wireless.workorder.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.facilityone.wireless.a.arch.ec.module.AttachmentBean;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.ec.module.Page;

import java.util.Comparator;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单服务
 * Date: 2018/7/4 下午4:20
 */
public class WorkorderService {

    //工单列表请求响应
    public static class WorkorderListReq {
        public Page page;
    }

    //工单查询
    public static class WorkorderQueryReq {
        public Page page;
        public WorkorderConditionBean searchCondition;
    }
    
    //计划性维护步骤编辑
    public static class WorkorderStepUpdateReq {
        public Long woId;
        public Long stepId;
        public Boolean finished;
        public String comment;
        public List<String> photos;
    }

    //工单执行人执行时间保存
    public static class WorkLaborerTimeReq {
        public Long woId;
        public Long laborerId;
        public Long actualArrivalDate;
        public Long actualFinishDate;
    }

    public static class WorkorderConditionBean {
        public String woCode;
        public String woDescription;
        public List<Long> priority;
        public List<Long> status;
        public Long typeId;
        public Long startDateTime;
        public Long endDateTime;
        public LocationBean location;
        public Long emId;

    }

    //工单详情
    public static class WorkorderInfoBean {
        public Long woId; // 工单ID
        public Long approvalId; // 审批ID
        public String code; // 工单号
        public String serviceTypeName; // 服务类型
        public Integer status; // 工单状态
        public String pfmCode;//dtz 专用
        public Long priorityId; // 工单优先级id
        public String priorityName; // 工单优先级
        public String woDescription; // 工单描述
        public Long createDateTime; // 工单创建时间
        public Long actualArrivalDateTime; // 工单开始时间
        public Long actualCompletionDateTime; // 工单完成时间
        public String actualWorkingTime; // 实际工作时间
        public String location; // 地理位置
        public String applicantName; // 工单请求者名称
        public String applicantPhone; // 工单请求者的电话
        /* 旧版字段 */
        public String laborer; // 执行人
        public String workContent; // 工作内容
        public Integer currentLaborerStatus;//当前用户工单状态
        public Long approvalSubmitDateTime; // 审批时间
        //抢单相关字段
        public Integer grabType;//抢单类型 0-普通工单  1-抢单工单
        public Integer grabStatus;//当前请求人对该工单的抢单状态
        public Integer category;//工单类型  0 — CM工单    1 — ZM工单   2 — PM工单
        public String type; // 工单类型
        public LocationBean locationId;//位置ID类
        public String organizationName; // 部门
        public Long workTeamId; // 工单组ID
        public Long estimateStartTime;//估计开始时间
        public Long estimateEndTime;//估计到达时间
        public Long reserveStartTime;//预约开始时间
        public Long reserveEndTime;//预约到达时间
        public String customerSignImgId; // 客户签名
        public String supervisorSignImgId; // 主管签名
        public List<String> requirementPictures; // 图片
        public List<String> requirementAudios; // 音频
        public List<String> requirementVideos; // 视频
        public List<String> requirementShortVideos; // 短视频
        public List<HistoriesBean> histories; //历史记录
        public List<ApprovalsBean> approvals; // 审批内容键值对
        public List<WorkOrderEquipmentsBean> workOrderEquipments; // 设备
        public List<WorkOrderLocationsBean> workOrderLocations;//空间位置
        public List<WorkorderLaborerService.WorkorderLaborerBean> workOrderLaborers; // 执行人
        public String sendWorkContent;//派发工作内容
        public List<WorkOrderToolsBean> workOrderTools; // 工具
        public List<ChargesBean> charges;//收费项
        public PmInfoBean pmInfo; // 计划性维护
        public List<StepsBean> steps; // 计划性维护信息 步骤
        public List<Integer> currentRoles; //当前角色权限
        public List<AttachmentBean> attachment;//附件
        public List<Long> approvers;//审批人列表
        public Boolean needScan; //故障设备是否需要扫描
        public List<PaymentsBean> payments;
        public List<String> pictures; // 图片
    }

    public static class PmInfoBean {
        public Long pmId; // 计划性维护ID
        public String name; // 计划性维护名称
        public String influence; // 计划性维护影响
        public Long priority; // 优先级
    }

    public static class HistoriesBean implements Parcelable {
        public Long historyId;
        public Integer step;
        public Long operationDate;
        public String handler;
        public String handlerImgId;
        public String content;
        public List<String> pictures;
        public List<AttachmentBean> attachment;

        public HistoriesBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.historyId);
            dest.writeValue(this.step);
            dest.writeValue(this.operationDate);
            dest.writeString(this.handler);
            dest.writeString(this.handlerImgId);
            dest.writeString(this.content);
            dest.writeStringList(this.pictures);
            dest.writeTypedList(this.attachment);
        }

        protected HistoriesBean(Parcel in) {
            this.historyId = (Long) in.readValue(Long.class.getClassLoader());
            this.step = (Integer) in.readValue(Integer.class.getClassLoader());
            this.operationDate = (Long) in.readValue(Long.class.getClassLoader());
            this.handler = in.readString();
            this.handlerImgId = in.readString();
            this.content = in.readString();
            this.pictures = in.createStringArrayList();
            this.attachment = in.createTypedArrayList(AttachmentBean.CREATOR);
        }

        public static final Creator<HistoriesBean> CREATOR = new Creator<HistoriesBean>() {
            @Override
            public HistoriesBean createFromParcel(Parcel source) {
                return new HistoriesBean(source);
            }

            @Override
            public HistoriesBean[] newArray(int size) {
                return new HistoriesBean[size];
            }
        };
    }

    public static class ApprovalsBean {
        public Long approvalId;
        public List<ApprovalResultsBean> approvalResults;
        public List<ApprovalContentBean> approvalContent;
    }

    public static class ApprovalResultsBean {
        public Long approverId;
        public String arDescription;
        public String approver;
        public Integer result;
    }

    public static class ApprovalContentBean {
        public String name;
        public String value;
    }

    public static class WorkOrderEquipmentsBean  implements Parcelable{
        public Long equipmentId; // 设备ID
        public String equipmentCode; // 设备编码
        public String equipmentName; // 设备名称
        public String location; // 设备安装位置
        public String equipmentSystemName; // 设备所属系统
        public String failureDesc; // 故障描述
        public String repairDesc; // 维修描述
        public Integer finished;//设备维保状态
        public boolean isLocal;//本地添加

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.equipmentId);
            dest.writeString(this.equipmentCode);
            dest.writeString(this.equipmentName);
            dest.writeString(this.location);
            dest.writeString(this.equipmentSystemName);
            dest.writeString(this.failureDesc);
            dest.writeString(this.repairDesc);
            dest.writeValue(this.finished);
            dest.writeByte(this.isLocal ? (byte) 1 : (byte) 0);
        }

        public WorkOrderEquipmentsBean() {
        }

        protected WorkOrderEquipmentsBean(Parcel in) {
            this.equipmentId = (Long) in.readValue(Long.class.getClassLoader());
            this.equipmentCode = in.readString();
            this.equipmentName = in.readString();
            this.location = in.readString();
            this.equipmentSystemName = in.readString();
            this.failureDesc = in.readString();
            this.repairDesc = in.readString();
            this.finished = (Integer) in.readValue(Integer.class.getClassLoader());
            this.isLocal = in.readByte() != 0;
        }

        public static final Creator<WorkOrderEquipmentsBean> CREATOR = new Creator<WorkOrderEquipmentsBean>() {
            @Override
            public WorkOrderEquipmentsBean createFromParcel(Parcel source) {
                return new WorkOrderEquipmentsBean(source);
            }

            @Override
            public WorkOrderEquipmentsBean[] newArray(int size) {
                return new WorkOrderEquipmentsBean[size];
            }
        };
    }

    public static class WorkOrderLocationsBean implements Parcelable {
        public Long recordId;
        public LocationBean location;
        public String locationName;
        public String repairDesc;
        public Boolean finished;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.recordId);
            dest.writeParcelable(this.location, flags);
            dest.writeString(this.locationName);
            dest.writeString(this.repairDesc);
            dest.writeValue(this.finished);
        }

        public WorkOrderLocationsBean() {
        }

        protected WorkOrderLocationsBean(Parcel in) {
            this.recordId = (Long) in.readValue(Long.class.getClassLoader());
            this.location = in.readParcelable(LocationBean.class.getClassLoader());
            this.locationName = in.readString();
            this.repairDesc = in.readString();
            this.finished = (Boolean) in.readValue(Boolean.class.getClassLoader());
        }

        public static final Parcelable.Creator<WorkOrderLocationsBean> CREATOR = new Parcelable.Creator<WorkOrderLocationsBean>() {
            @Override
            public WorkOrderLocationsBean createFromParcel(Parcel source) {
                return new WorkOrderLocationsBean(source);
            }

            @Override
            public WorkOrderLocationsBean[] newArray(int size) {
                return new WorkOrderLocationsBean[size];
            }
        };
    }

    public static class WorkOrderToolsBean implements Parcelable {
        public Long toolId; // 物料或工具ID
        public String name; // 物料或工具名称
        public String brand; // 物料或工具品牌
        public String model; // 型号
        public Integer amount; // 物料或工具个数
        public String unit; // 物料或工具单位
        public Double cost; // 物料或工具费用
        public String comment; // 物料或工具备注


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.toolId);
            dest.writeString(this.name);
            dest.writeString(this.brand);
            dest.writeString(this.model);
            dest.writeValue(this.amount);
            dest.writeString(this.unit);
            dest.writeValue(this.cost);
            dest.writeString(this.comment);
        }

        public WorkOrderToolsBean() {
        }

        protected WorkOrderToolsBean(Parcel in) {
            this.toolId = (Long) in.readValue(Long.class.getClassLoader());
            this.name = in.readString();
            this.brand = in.readString();
            this.model = in.readString();
            this.amount = (Integer) in.readValue(Integer.class.getClassLoader());
            this.unit = in.readString();
            this.cost = (Double) in.readValue(Double.class.getClassLoader());
            this.comment = in.readString();
        }

        public static final Parcelable.Creator<WorkOrderToolsBean> CREATOR = new Parcelable.Creator<WorkOrderToolsBean>() {
            @Override
            public WorkOrderToolsBean createFromParcel(Parcel source) {
                return new WorkOrderToolsBean(source);
            }

            @Override
            public WorkOrderToolsBean[] newArray(int size) {
                return new WorkOrderToolsBean[size];
            }
        };
    }
    
    public static class StepsBean implements Comparator<StepsBean>,Parcelable {
        public Long stepId;//步骤ID
        public String step;//步骤
        public Integer sort;//序号
        public Boolean finished;//是否完成
        public String comment;//工作描述
        public Long workTeamId;//工作组ID
        public String workTeamName;//工作组名称
        public boolean isOperator;//是否为执行人，用于item是否可编辑
        public List<String> photos;// 步骤图片

        public boolean isOperator() {
            return isOperator;
        }

        public void setIsOperator(boolean isOperator) {
            this.isOperator = isOperator;
        }

        @Override
        public int compare(StepsBean lhs, StepsBean rhs) {
            return lhs.sort >= rhs.sort ? lhs.sort : rhs.sort;
        }


        public StepsBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.stepId);
            dest.writeString(this.step);
            dest.writeValue(this.sort);
            dest.writeValue(this.finished);
            dest.writeString(this.comment);
            dest.writeValue(this.workTeamId);
            dest.writeString(this.workTeamName);
            dest.writeByte(this.isOperator ? (byte) 1 : (byte) 0);
            dest.writeStringList(this.photos);
        }

        protected StepsBean(Parcel in) {
            this.stepId = (Long) in.readValue(Long.class.getClassLoader());
            this.step = in.readString();
            this.sort = (Integer) in.readValue(Integer.class.getClassLoader());
            this.finished = (Boolean) in.readValue(Boolean.class.getClassLoader());
            this.comment = in.readString();
            this.workTeamId = (Long) in.readValue(Long.class.getClassLoader());
            this.workTeamName = in.readString();
            this.isOperator = in.readByte() != 0;
            this.photos = in.createStringArrayList();
        }

        public static final Creator<StepsBean> CREATOR = new Creator<StepsBean>() {
            @Override
            public StepsBean createFromParcel(Parcel source) {
                return new StepsBean(source);
            }

            @Override
            public StepsBean[] newArray(int size) {
                return new StepsBean[size];
            }
        };
    }

    public static class ChargesBean implements Parcelable {
        public Long chargeId; //收费项ID
        public String name; //收费项名称
        public Double amount; //收费金额
        public Double tax; //收费税率


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.chargeId);
            dest.writeString(this.name);
            dest.writeValue(this.amount);
            dest.writeValue(this.tax);
        }

        public ChargesBean() {
        }

        protected ChargesBean(Parcel in) {
            this.chargeId = (Long) in.readValue(Long.class.getClassLoader());
            this.name = in.readString();
            this.amount = (Double) in.readValue(Double.class.getClassLoader());
            this.tax = (Double) in.readValue(Double.class.getClassLoader());
        }

        public static final Parcelable.Creator<ChargesBean> CREATOR = new Parcelable.Creator<ChargesBean>() {
            @Override
            public ChargesBean createFromParcel(Parcel source) {
                return new ChargesBean(source);
            }

            @Override
            public ChargesBean[] newArray(int size) {
                return new ChargesBean[size];
            }
        };
    }

    public static class PaymentsBean implements Parcelable{
        public Long paymentId; //缴费单 ID
        public String code;
        public Long createDateTime;
        public String location;
        public Integer status;
        public String customer;//缴费客户
        public String cost;//	缴费金额

        protected PaymentsBean(Parcel in) {
            if (in.readByte() == 0) {
                paymentId = null;
            } else {
                paymentId = in.readLong();
            }
            code = in.readString();
            if (in.readByte() == 0) {
                createDateTime = null;
            } else {
                createDateTime = in.readLong();
            }
            location = in.readString();
            if (in.readByte() == 0) {
                status = null;
            } else {
                status = in.readInt();
            }
            customer = in.readString();
            cost = in.readString();
        }

        public static final Creator<PaymentsBean> CREATOR = new Creator<PaymentsBean>() {
            @Override
            public PaymentsBean createFromParcel(Parcel in) {
                return new PaymentsBean(in);
            }

            @Override
            public PaymentsBean[] newArray(int size) {
                return new PaymentsBean[size];
            }
        };

        public PaymentsBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (paymentId == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(paymentId);
            }
            dest.writeString(code);
            if (createDateTime == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(createDateTime);
            }
            dest.writeString(location);
            if (status == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(status);
            }
            dest.writeString(customer);
            dest.writeString(cost);
        }
    }

    public static class WorkorderListResp {
        public Page page;
        public List<WorkorderItemBean> contents;
    }

    public static class WorkorderItemBean {
        public Long woId;
        public String code;
        public Long priorityId;
        public String priority;
        public String priorityName;
        public String woDescription;
        public Long createDateTime;
        public String location;
        public Integer status;
        public Integer currentLaborerStatus;
        public String applicantName;
        public String applicantPhone;
        public String serviceTypeName;
        public String workContent;
        public Long actualCompletionDateTime;
        public List<WorkorderApprovalBean> approvalContent;
    }

    public static class WorkorderApprovalBean {
        public String name;
        public String value;
    }


    /**
     * 关联工单预定物料实例
     */
    public static class WorkorderReserveRocordBean{
        public Long activityId; // 预定单ID
        public Long warehouseId; // 仓库ID
        public String warehouseName; // 仓库名称
        public String reservationCode; // 预定单号
        public String reservationPersonName; // 预订人
        public Long woId; // 关联工单ID
        public String woCode; // 关联工单编号
        public Long reservationDate; // 预定日期
        //        0 — 未审核
        //        1 — 通过（待出库）
        //        2 — 取消（已驳回）
        //        3 — 已出库
        //        4 — 取消出库（管理员取消）
        //        5 — 取消预定（预订人取消）
        public Integer status; // 预定的状态
        public Long reservationPersonId; //
        public Long administrator; // 仓库管理员ID
        public Long supervisor; // 主管ID
        public String operateDesc;//操作描述

    }
}
