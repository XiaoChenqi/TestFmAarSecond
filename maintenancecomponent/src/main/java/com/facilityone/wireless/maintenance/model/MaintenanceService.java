package com.facilityone.wireless.maintenance.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.facilityone.wireless.a.arch.ec.module.AttachmentBean;

import java.util.List;

/**
 * Created by peter.peng on 2018/11/16.
 */

public class MaintenanceService {




    /**
     * 维护日历数据bean
     */
    public static class MaintenanceCalendarBean implements MultiItemEntity{

        public Long pmId;//计划id
        public String pmName;//计划性维护任务名称
        public Long pmtodoId;//计划任务 ID
        public Long dateTodo;//时间
        public Boolean genStatus;//是否生成了工单
        //1 — 未处理
        //2 — 处理中
        //3 — 已结束
        //4 — 遗漏
        public Integer status;//任务状态
        public List<Long> woIds;//关联工单ID数组
        public int type;//类型
        public String title;//标题



        @Override
        public int getItemType() {
            return type;
        }
    }


    /**
     * 计划性维护详情数据bean
     */
    public static class MaintenanceInfoBean implements Parcelable{
        public Long pmId;//计划 ID
        public String name;//计划名称
        public Integer status;//任务状态
        public Integer priority;//优先级
        public String influence;//	影响
        public String period;//周期
        public Long dateFirstTodo;//首次维护时间
        public Long dateNextTodo;//下次维护时间
        public String estimatedWorkingTime;//预估工作耗时
        public Long startTime;//开始时间
        public Long endTime;//完成时间
        public Boolean autoGenerateOrder;//是否自动生成工单
        public Boolean genStatus;//是否已生成工单
        public Integer ahead;//提前生成天数
        public List<Step> pmSteps;//计划步骤数组
        public List<Material> pmMaterials;//物料数组
        public List<Tool> pmTools;//工具数组
        public List<MaintenanceEquipment> equipments;//维护设备数组
        public List<Space> spaces;//空间位置数组
        public List<MaintenanceWorkOrder> workOrders;//关联工单数组
        public List<AttachmentBean> pictures;//图片 ID 数组

        protected MaintenanceInfoBean(Parcel in) {
            pmId = (Long) in.readValue(Long.class.getClassLoader());
            name = in.readString();
            status = (Integer) in.readValue(Integer.class.getClassLoader());
            priority = (Integer) in.readValue(Integer.class.getClassLoader());
            influence = in.readString();
            period = in.readString();
            dateFirstTodo = (Long) in.readValue(Long.class.getClassLoader());
            dateNextTodo = (Long) in.readValue(Long.class.getClassLoader());
            estimatedWorkingTime = in.readString();
            startTime = (Long) in.readValue(Long.class.getClassLoader());
            endTime = (Long) in.readValue(Long.class.getClassLoader());
            autoGenerateOrder = (Boolean) in.readValue(Boolean.class.getClassLoader());
            genStatus = (Boolean) in.readValue(Boolean.class.getClassLoader());
            ahead = (Integer) in.readValue(Integer.class.getClassLoader());
            pmSteps = in.createTypedArrayList(Step.CREATOR);
            pmMaterials = in.createTypedArrayList(Material.CREATOR);
            pmTools = in.createTypedArrayList(Tool.CREATOR);
            equipments = in.createTypedArrayList(MaintenanceEquipment.CREATOR);
            spaces = in.createTypedArrayList(Space.CREATOR);
            workOrders = in.createTypedArrayList(MaintenanceWorkOrder.CREATOR);
            pictures = in.createTypedArrayList(AttachmentBean.CREATOR);
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(pmId);
            dest.writeString(name);
            dest.writeValue(status);
            dest.writeValue(priority);
            dest.writeString(influence);
            dest.writeString(period);
            dest.writeValue(dateFirstTodo);
            dest.writeValue(dateNextTodo);
            dest.writeString(estimatedWorkingTime);
            dest.writeValue(startTime);
            dest.writeValue(endTime);
            dest.writeValue(autoGenerateOrder);
            dest.writeValue(genStatus);
            dest.writeValue(ahead);
            dest.writeTypedList(pmSteps);
            dest.writeTypedList(pmMaterials);
            dest.writeTypedList(pmTools);
            dest.writeTypedList(equipments);
            dest.writeTypedList(spaces);
            dest.writeTypedList(workOrders);
            dest.writeTypedList(pictures);
        }

        public static final Creator<MaintenanceInfoBean> CREATOR = new Creator<MaintenanceInfoBean>() {
            @Override
            public MaintenanceInfoBean createFromParcel(Parcel in) {
                return new MaintenanceInfoBean(in);
            }

            @Override
            public MaintenanceInfoBean[] newArray(int size) {
                return new MaintenanceInfoBean[size];
            }
        };
    }

    /**
     * 步骤
     */
    public static class Step implements Parcelable{
        public Long pmSId;//步骤 ID
        public String comment;//步骤完成说明
        public Boolean finished;//是否已完成
        public String step;//步骤内容
        public String workTeamName;//相关工作组名称
        public Integer sort;//步骤序号
        public Long woId;//步骤关联工单 ID

        protected Step(Parcel in) {
            pmSId = (Long) in.readValue(Long.class.getClassLoader());
            comment = in.readString();
            finished = (Boolean) in.readValue(Boolean.class.getClassLoader());
            step = in.readString();
            workTeamName = in.readString();
            sort = (Integer) in.readValue(Integer.class.getClassLoader());
            woId = (Long) in.readValue(Long.class.getClassLoader());
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(pmSId);
            dest.writeString(comment);
            dest.writeValue(finished);
            dest.writeString(step);
            dest.writeString(workTeamName);
            dest.writeValue(sort);
            dest.writeValue(woId);
        }

        public static final Creator<Step> CREATOR = new Creator<Step>() {
            @Override
            public Step createFromParcel(Parcel in) {
                return new Step(in);
            }

            @Override
            public Step[] newArray(int size) {
                return new Step[size];
            }
        };
    }

    /**
     * 物料
     */
    public static class Material implements Parcelable{
        public Long pmmId;//计划物料 ID
        public Long materialId;//库存 ID
        public String name;//物料名称
        public String brand;//品牌
        public String model;//型号
        public Double price;//单价
        public String unit;//单位
        public Float amount;//数量
        public String comment;//说明
        public String mpComment;

        protected Material(Parcel in) {
            pmmId = (Long) in.readValue(Long.class.getClassLoader());
            materialId = (Long) in.readValue(Long.class.getClassLoader());
            name = in.readString();
            brand = in.readString();
            model = in.readString();
            price = (Double) in.readValue(Integer.class.getClassLoader());
            unit = in.readString();
            amount = (Float) in.readValue(Float.class.getClassLoader());
            comment = in.readString();
            mpComment = in.readString();
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(pmmId);
            dest.writeValue(materialId);
            dest.writeString(name);
            dest.writeString(brand);
            dest.writeString(model);
            dest.writeValue(price);
            dest.writeString(unit);
            dest.writeValue(amount);
            dest.writeString(comment);
            dest.writeString(mpComment);
        }

        public static final Creator<Material> CREATOR = new Creator<Material>() {
            @Override
            public Material createFromParcel(Parcel in) {
                return new Material(in);
            }

            @Override
            public Material[] newArray(int size) {
                return new Material[size];
            }
        };
    }

    /**
     * 工具
     */
    public static class Tool implements Parcelable{
        public Long pmtId;//计划工具 ID
        public String name;//工具名称
        public String model;//型号
        public String unit;//单位
        public Float amount;//数量
        public String comment;//说明
        public String brand;//品牌

        protected Tool(Parcel in) {
            pmtId = (Long) in.readValue(Long.class.getClassLoader());
            name = in.readString();
            model = in.readString();
            unit = in.readString();
            amount = (Float) in.readValue(Float.class.getClassLoader());
            comment = in.readString();
            brand = in.readString();
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(pmtId);
            dest.writeString(name);
            dest.writeString(model);
            dest.writeString(unit);
            dest.writeValue(amount);
            dest.writeString(comment);
            dest.writeString(brand);
        }

        public static final Creator<Tool> CREATOR = new Creator<Tool>() {
            @Override
            public Tool createFromParcel(Parcel in) {
                return new Tool(in);
            }

            @Override
            public Tool[] newArray(int size) {
                return new Tool[size];
            }
        };
    }

    /**
     * 维护设备
     */
    public static class MaintenanceEquipment implements Parcelable{
        public Long eqId;//计划工具 ID
        public String name;//设备名称
        public String code;//设备编号
        public Long sysType;//设备类型
        public String eqSystemName;//设备分类名称
        public String location;//位置

        protected MaintenanceEquipment(Parcel in) {
            eqId = (Long) in.readValue(Long.class.getClassLoader());
            name = in.readString();
            code = in.readString();
            sysType = (Long) in.readValue(Long.class.getClassLoader());
            eqSystemName = in.readString();
            location = in.readString();
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(eqId);
            dest.writeString(name);
            dest.writeString(code);
            dest.writeValue(sysType);
            dest.writeString(eqSystemName);
            dest.writeString(location);
        }

        public static final Creator<MaintenanceEquipment> CREATOR = new Creator<MaintenanceEquipment>() {
            @Override
            public MaintenanceEquipment createFromParcel(Parcel in) {
                return new MaintenanceEquipment(in);
            }

            @Override
            public MaintenanceEquipment[] newArray(int size) {
                return new MaintenanceEquipment[size];
            }
        };
    }

    /**
     * 空间位置
     */
    public static class Space implements Parcelable{
        public String location;//空间位置
        public Long pmsId;//位置id

        protected Space(Parcel in) {
            location = in.readString();
            pmsId = (Long) in.readValue(Long.class.getClassLoader());
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(location);
            dest.writeValue(pmsId);
        }

        public static final Creator<Space> CREATOR = new Creator<Space>() {
            @Override
            public Space createFromParcel(Parcel in) {
                return new Space(in);
            }

            @Override
            public Space[] newArray(int size) {
                return new Space[size];
            }
        };
    }

    public static class MaintenanceObject extends MaintenanceCalendarBean implements MultiItemEntity{
        public String title;
        public String name;//设备名称
        public String code;//设备编号
        public Long sysType;//设备类型
        public String eqSystemName;//设备分类名称
        public String location;//空间位置
        public int type;//类型

        @Override
        public int getItemType() {
            return type;
        }
    }
    /**
     * 关联工单
     */
    public static class MaintenanceWorkOrder implements Parcelable{
        public Long woId;//工单 ID
        public String code;//工单编号
        public Long priorityId;//优先级 ID
        public String applicantName;//联系人名名字
        public String applicantPhone;//联系人电话
        public String location;//位置
        public Integer status;//工单状态
        public Long createDateTime;//创建时间
        public Long actualCompletionDateTime;//完成时间
        public String woDescription;//工单描述
        public String workContent;//工作内容
        public String serviceTypeName;//服务类型名称
        public Integer currentLaborerStatus;//当前执行人的状态

        protected MaintenanceWorkOrder(Parcel in) {
            woId = (Long) in.readValue(Long.class.getClassLoader());
            code = in.readString();
            priorityId = (Long) in.readValue(Long.class.getClassLoader());
            applicantName = in.readString();
            applicantPhone = in.readString();
            location = in.readString();
            status = (Integer) in.readValue(Integer.class.getClassLoader());
            createDateTime = (Long) in.readValue(Long.class.getClassLoader());
            actualCompletionDateTime = (Long) in.readValue(Long.class.getClassLoader());
            woDescription = in.readString();
            workContent = in.readString();
            serviceTypeName = in.readString();
            currentLaborerStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(woId);
            dest.writeString(code);
            dest.writeValue(priorityId);
            dest.writeString(applicantName);
            dest.writeString(applicantPhone);
            dest.writeString(location);
            dest.writeValue(status);
            dest.writeValue(createDateTime);
            dest.writeValue(actualCompletionDateTime);
            dest.writeString(woDescription);
            dest.writeString(workContent);
            dest.writeString(serviceTypeName);
            dest.writeValue(currentLaborerStatus);
        }

        public static final Creator<MaintenanceWorkOrder> CREATOR = new Creator<MaintenanceWorkOrder>() {
            @Override
            public MaintenanceWorkOrder createFromParcel(Parcel in) {
                return new MaintenanceWorkOrder(in);
            }

            @Override
            public MaintenanceWorkOrder[] newArray(int size) {
                return new MaintenanceWorkOrder[size];
            }
        };
    }

}
