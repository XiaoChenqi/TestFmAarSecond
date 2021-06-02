package com.facilityone.wireless.a.arch.offline.model.entity;

import com.facilityone.wireless.a.arch.ec.module.LocationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检点位
 * Date: 2018/11/1 3:30 PM
 */
public class PatrolSpotEntity {
    private Long patrolSpotId;
    private Long spotId;
    private Integer sort;
    private Long taskId;
    private Long startTime;
    private Long endTime;
    private Integer compNumber;
    private Integer equNumber;
    private int exception;
    private int needSync;
    private int completed;
    private int remoteCompleted;
    private Integer deleted;
    private String handler;
    private String name;
    private String code;
    private String locationName;
    private LocationBean location;
    private String taskName;
    private List<PatrolItemEntity> contents;
    private List<PatrolEquEntity> equipments;

    private Long taskDueStartDateTime;
    private Long taskDueEndDateTime;
    private Long taskPlanId;

    public Long getTaskPlanId() {
        return taskPlanId;
    }

    public void setTaskPlanId(Long taskPlanId) {
        this.taskPlanId = taskPlanId;
    }

    public String getTaskName() {
        return taskName == null ? "" : taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getTaskDueStartDateTime() {
        return taskDueStartDateTime;
    }

    public void setTaskDueStartDateTime(Long taskDueStartDateTime) {
        this.taskDueStartDateTime = taskDueStartDateTime;
    }

    public Long getTaskDueEndDateTime() {
        return taskDueEndDateTime;
    }

    public void setTaskDueEndDateTime(Long taskDueEndDateTime) {
        this.taskDueEndDateTime = taskDueEndDateTime;
    }

    public int getRemoteCompleted() {
        return remoteCompleted;
    }

    public void setRemoteCompleted(int remoteCompleted) {
        this.remoteCompleted = remoteCompleted;
    }

    public String getLocationName() {
        return locationName == null ? "" : locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHandler() {
        return handler == null ? "" : handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public Long getPatrolSpotId() {
        return patrolSpotId;
    }

    public void setPatrolSpotId(Long patrolSpotId) {
        this.patrolSpotId = patrolSpotId;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getCompNumber() {
        return compNumber;
    }

    public void setCompNumber(Integer compNumber) {
        this.compNumber = compNumber;
    }

    public Integer getEquNumber() {
        return equNumber;
    }

    public void setEquNumber(Integer equNumber) {
        this.equNumber = equNumber;
    }

    public int getException() {
        return exception;
    }

    public void setException(int exception) {
        this.exception = exception;
    }

    public int getNeedSync() {
        return needSync;
    }

    public void setNeedSync(int needSync) {
        this.needSync = needSync;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
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

    public List<PatrolEquEntity> getEquipments() {
        if (equipments == null) {
            return new ArrayList<>();
        }
        return equipments;
    }

    public void setEquipments(List<PatrolEquEntity> equipments) {
        this.equipments = equipments;
    }
}
