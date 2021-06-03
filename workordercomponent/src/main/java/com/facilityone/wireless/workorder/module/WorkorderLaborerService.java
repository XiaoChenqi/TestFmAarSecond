package com.facilityone.wireless.workorder.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/17 上午11:00
 */
public class WorkorderLaborerService {

    public static class WorkorderLaborerGroupBean {
        public Long wtId;
        public String name;
        public List<WorkorderLaborerBean> members;
    }

    public static class WorkorderLaborerBean implements Parcelable {
        public Long laborerId; // 执行人ID
        public Long woLaborerId; //该工单执行人ID
        public String laborer; // 执行人名称
        public String positionName; // 岗位名称
        public String phone; // 执行人电话
        public Long actualArrivalDateTime; // 实际到场时间
        public Long actualCompletionDateTime; // 实际完成时间
        public String actualWorkingTime; // 实际工作时间
        public Integer status; // 该执行人该工单的状态(添加执行人列表中的意思 执行人在岗状态0 — 离岗 1 — 在岗 2 — 没有参与考勤 )
        public Boolean responsible; // 是否是负责人
        public boolean canOpt;

        public String name; // 执行人名称
        public String number;
        public Integer woNumber;//该执行人未完成工单数量
        public Long emId; // 执行人ID
        public Long approverId; // 审批人 ID
        public boolean checked;
        public boolean leader;
        public transient String namePinyin;
        public transient String nameFirstLetters;
        public transient int start;//搜索匹配的开始位置(name 中的位置顺序)
        public transient int end;//搜索匹配的结束位置(name 中的位置顺序)


        public WorkorderLaborerBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.laborerId);
            dest.writeValue(this.woLaborerId);
            dest.writeString(this.laborer);
            dest.writeString(this.positionName);
            dest.writeString(this.phone);
            dest.writeValue(this.actualArrivalDateTime);
            dest.writeValue(this.actualCompletionDateTime);
            dest.writeString(this.actualWorkingTime);
            dest.writeValue(this.status);
            dest.writeValue(this.responsible);
            dest.writeByte(this.canOpt ? (byte) 1 : (byte) 0);
            dest.writeString(this.name);
            dest.writeString(this.number);
            dest.writeValue(this.woNumber);
            dest.writeValue(this.emId);
            dest.writeValue(this.approverId);
            dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
            dest.writeByte(this.leader ? (byte) 1 : (byte) 0);
            dest.writeString(this.namePinyin);
            dest.writeString(this.nameFirstLetters);
            dest.writeInt(this.start);
            dest.writeInt(this.end);
        }

        protected WorkorderLaborerBean(Parcel in) {
            this.laborerId = (Long) in.readValue(Long.class.getClassLoader());
            this.woLaborerId = (Long) in.readValue(Long.class.getClassLoader());
            this.laborer = in.readString();
            this.positionName = in.readString();
            this.phone = in.readString();
            this.actualArrivalDateTime = (Long) in.readValue(Long.class.getClassLoader());
            this.actualCompletionDateTime = (Long) in.readValue(Long.class.getClassLoader());
            this.actualWorkingTime = in.readString();
            this.status = (Integer) in.readValue(Integer.class.getClassLoader());
            this.responsible = (Boolean) in.readValue(Boolean.class.getClassLoader());
            this.canOpt = in.readByte() != 0;
            this.name = in.readString();
            this.number = in.readString();
            this.woNumber = (Integer) in.readValue(Integer.class.getClassLoader());
            this.emId = (Long) in.readValue(Long.class.getClassLoader());
            this.approverId = (Long) in.readValue(Long.class.getClassLoader());
            this.checked = in.readByte() != 0;
            this.leader = in.readByte() != 0;
            this.namePinyin = in.readString();
            this.nameFirstLetters = in.readString();
            this.start = in.readInt();
            this.end = in.readInt();
        }

        public static final Creator<WorkorderLaborerBean> CREATOR = new Creator<WorkorderLaborerBean>() {
            @Override
            public WorkorderLaborerBean createFromParcel(Parcel source) {
                return new WorkorderLaborerBean(source);
            }

            @Override
            public WorkorderLaborerBean[] newArray(int size) {
                return new WorkorderLaborerBean[size];
            }
        };
    }
}
