package com.facilityone.wireless.a.arch.offline.model.entity;

import com.facilityone.wireless.a.arch.ec.module.LocationBean;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工作流程
 * Date: 2018/10/18 5:58 PM
 */
public class FlowEntity {
    private Long wopId;
    private Boolean deleted;
    private Long organizationId;
    private Long serviceTypeId;
    private Long priorityId;
    private String type;
    private LocationBean position;
    private Long projectId;

    public Long getWopId() {
        return wopId;
    }

    public void setWopId(Long wopId) {
        this.wopId = wopId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Long getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Long priorityId) {
        this.priorityId = priorityId;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocationBean getPosition() {
        return position;
    }

    public void setPosition(LocationBean position) {
        this.position = position;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
