package com.facilityone.wireless.patrol.module;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检数据保存
 * Date: 2018/11/14 4:49 PM
 */
public class PatrolSaveReq {
    public Long userId;
    public Integer operateType;
    public PatrolTaskReq patrolTask;

    public static class PatrolTaskReq {
        public Long patrolTaskId;
        public Long startDateTime;
        public Long endDateTime;
        public List<PatrolSpotReq> spots;
    }

    public static class PatrolSpotReq {
        public Long patrolSpotId;
        public Long startDateTime;
        public Long endDateTime;
        public Boolean finished;
        public List<PatrolEquReq> exceptionEquipment;
        public List<PatrolItemReq> contents;
    }

    public static class PatrolEquReq {
        public Long eqId;
        public Integer status;
        public String desc;
    }

    public static class PatrolItemReq {
        public Long patrolTaskSpotResultId;
        public String resultSelect;
        public Double resultInput;
        public String comment;
        public List<String> photoIds;
    }
}