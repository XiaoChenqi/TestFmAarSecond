package com.facilityone.wireless.a.arch.offline.model.entity;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:服务类型实体
 * Date: 2018/10/18 5:27 PM
 */
public class ServiceTypeEntity {

    private Long serviceTypeId;
    private Integer sort;
    private Boolean deleted;
    private String name;
    private String fullName;
    private String stypeDesc;
    private Long parentId;
    private Long projectId;

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName == null ? "" : fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStypeDesc() {
        return stypeDesc == null ? "" : stypeDesc;
    }

    public void setStypeDesc(String stypeDesc) {
        this.stypeDesc = stypeDesc;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
