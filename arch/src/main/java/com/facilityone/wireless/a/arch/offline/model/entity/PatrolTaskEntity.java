package com.facilityone.wireless.a.arch.offline.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检任务
 * Date: 2018/11/1 3:30 PM
 */
public class PatrolTaskEntity {

    private Long taskId;
    private Long planId;
    private String taskName;
    private Integer eqNumber;
    private Integer spotNumber;
    private Long dueStartDateTime;
    private Long dueEndDateTime;
    private Long startTime;
    private Long endTime;
    private Long updateTime;
    private Integer status;
    private Integer deleted;
    private int exception;
    private int needSync;
    private int completed;
    private List<PatrolSpotEntity> spots;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getTaskName() {
        return taskName == null ? "" : taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getEqNumber() {
        return eqNumber;
    }

    public void setEqNumber(Integer eqNumber) {
        this.eqNumber = eqNumber;
    }

    public Integer getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(Integer spotNumber) {
        this.spotNumber = spotNumber;
    }

    public Long getDueStartDateTime() {
        return dueStartDateTime;
    }

    public void setDueStartDateTime(Long dueStartDateTime) {
        this.dueStartDateTime = dueStartDateTime;
    }

    public Long getDueEndDateTime() {
        return dueEndDateTime;
    }

    public void setDueEndDateTime(Long dueEndDateTime) {
        this.dueEndDateTime = dueEndDateTime;
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

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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

    public List<PatrolSpotEntity> getSpots() {
        if (spots == null) {
            return new ArrayList<>();
        }
        return spots;
    }

    public void setSpots(List<PatrolSpotEntity> spots) {
        this.spots = spots;
    }
}
