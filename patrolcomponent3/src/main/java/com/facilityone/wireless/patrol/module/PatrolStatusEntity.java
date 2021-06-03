package com.facilityone.wireless.patrol.module;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检任务状态更新记录
 * Date: 2018/11/6 4:05 PM
 */
public class PatrolStatusEntity {
    private Long patrolTaskId;
    private Boolean deleted;
    private Integer status;
    private Boolean isHandler;
    private List<PatrolSpotsBean> patrolSpots;


    public Long getPatrolTaskId() {
        return patrolTaskId;
    }

    public void setPatrolTaskId(Long patrolTaskId) {
        this.patrolTaskId = patrolTaskId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getHandler() {
        return isHandler;
    }

    public void setHandler(Boolean handler) {
        isHandler = handler;
    }

    public List<PatrolSpotsBean> getPatrolSpots() {
        if (patrolSpots == null) {
            return new ArrayList<>();
        }
        return patrolSpots;
    }

    public void setPatrolSpots(List<PatrolSpotsBean> patrolSpots) {
        this.patrolSpots = patrolSpots;
    }

    public static class PatrolSpotsBean {
        private Long patrolSpotId;
        private Long spotId;
        private Boolean finished;
        private Long taskId;
        private String handler;
        private List<PatrolEquBean> equipments;

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

        public Boolean getFinished() {
            return finished;
        }

        public void setFinished(Boolean finished) {
            this.finished = finished;
        }

        public Long getTaskId() {
            return taskId;
        }

        public void setTaskId(Long taskId) {
            this.taskId = taskId;
        }

        public List<PatrolEquBean> getEquipments() {
            if (equipments == null) {
                return new ArrayList<>();
            }
            return equipments;
        }

        public void setEquipments(List<PatrolEquBean> equipments) {
            this.equipments = equipments;
        }
    }
    
    public static class PatrolEquBean {
        private Long eqId;
        private Boolean finished;
        private Long spotId;

        public Long getSpotId() {
            return spotId;
        }

        public void setSpotId(Long spotId) {
            this.spotId = spotId;
        }

        public Long getEqId() {
            return eqId;
        }

        public void setEqId(Long eqId) {
            this.eqId = eqId;
        }

        public Boolean getFinished() {
            return finished;
        }

        public void setFinished(Boolean finished) {
            this.finished = finished;
        }
    }
}
