package com.facilityone.wireless.a.arch.offline.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.facilityone.wireless.a.arch.ec.module.LocationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检设备
 * Date: 2018/11/1 3:30 PM
 */
public class PatrolEquEntity implements Parcelable {
    private Long eqId;
    private Long taskId;
    private Long spotId;
    private Integer itemNumber;
    private Integer itemUseNumber;
    private Integer itemStopNumber;
    private int exception;
    private int completed;
    private int remoteCompleted;
    private Integer sort;
    private Integer deleted;
    private String name;
    private String code;
    private LocationBean location;
    private boolean deviceStatus;//true 在用 false 停运
    private boolean originalDeviceStatus;
    private boolean miss;
    private List<PatrolItemEntity> contents;

    public int getRemoteCompleted() {
        return remoteCompleted;
    }

    public void setRemoteCompleted(int remoteCompleted) {
        this.remoteCompleted = remoteCompleted;
    }

    public boolean isMiss() {
        return miss;
    }

    public void setMiss(boolean miss) {
        this.miss = miss;
    }

    public void setOriginalDeviceStatus(boolean originalDeviceStatus) {
        this.originalDeviceStatus = originalDeviceStatus;
    }

    public boolean isOriginalDeviceStatus() {
        return originalDeviceStatus;
    }

    /**
     * true  在用
     * false 停用 
     * @return
     */
    public boolean isDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(boolean deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public Long getEqId() {
        return eqId;
    }

    public void setEqId(Long eqId) {
        this.eqId = eqId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getItemUseNumber() {
        return itemUseNumber;
    }

    public void setItemUseNumber(Integer itemUseNumber) {
        this.itemUseNumber = itemUseNumber;
    }

    public Integer getItemStopNumber() {
        return itemStopNumber;
    }

    public void setItemStopNumber(Integer itemStopNumber) {
        this.itemStopNumber = itemStopNumber;
    }

    public int getException() {
        return exception;
    }

    public void setException(int exception) {
        this.exception = exception;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public List<PatrolItemEntity> getContents() {
        if (contents == null) {
            return new ArrayList<>();
        }
        return contents;
    }

    public void setContents(List<PatrolItemEntity> contents) {
        this.contents = contents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.eqId);
        dest.writeValue(this.taskId);
        dest.writeValue(this.spotId);
        dest.writeValue(this.itemNumber);
        dest.writeValue(this.itemUseNumber);
        dest.writeValue(this.itemStopNumber);
        dest.writeInt(this.exception);
        dest.writeInt(this.completed);
        dest.writeValue(this.sort);
        dest.writeValue(this.deleted);
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeParcelable(this.location, flags);
        dest.writeByte(this.deviceStatus ? (byte) 1 : (byte) 0);
        dest.writeList(this.contents);
    }

    public PatrolEquEntity() {
    }

    protected PatrolEquEntity(Parcel in) {
        this.eqId = (Long) in.readValue(Long.class.getClassLoader());
        this.taskId = (Long) in.readValue(Long.class.getClassLoader());
        this.spotId = (Long) in.readValue(Long.class.getClassLoader());
        this.itemNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.itemUseNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.itemStopNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.exception = in.readInt();
        this.completed = in.readInt();
        this.sort = (Integer) in.readValue(Integer.class.getClassLoader());
        this.deleted = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.code = in.readString();
        this.location = in.readParcelable(LocationBean.class.getClassLoader());
        this.deviceStatus = in.readByte() != 0;
        this.contents = new ArrayList<PatrolItemEntity>();
        in.readList(this.contents, PatrolItemEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<PatrolEquEntity> CREATOR = new Parcelable.Creator<PatrolEquEntity>() {
        @Override
        public PatrolEquEntity createFromParcel(Parcel source) {
            return new PatrolEquEntity(source);
        }

        @Override
        public PatrolEquEntity[] newArray(int size) {
            return new PatrolEquEntity[size];
        }
    };
}
