package com.facilityone.wireless.a.arch.offline.model.entity;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:设备类型
 * Date: 2018/10/18 12:34 PM
 */
public class EquTypeEntity {
    private Long equSysId;
    private Boolean deleted;
    private String equSysCode;
    private String equSysName;
    private String equSysDescription;
    private String equSysFullName;
    private Integer level;
    private Long equSysParentSystemId;
    private Long projectId;

    public Long getEquSysId() {
        return equSysId;
    }

    public void setEquSysId(Long equSysId) {
        this.equSysId = equSysId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getEquSysCode() {
        return equSysCode == null ? "" : equSysCode;
    }

    public void setEquSysCode(String equSysCode) {
        this.equSysCode = equSysCode;
    }

    public String getEquSysName() {
        return equSysName == null ? "" : equSysName;
    }

    public void setEquSysName(String equSysName) {
        this.equSysName = equSysName;
    }

    public String getEquSysDescription() {
        return equSysDescription == null ? "" : equSysDescription;
    }

    public void setEquSysDescription(String equSysDescription) {
        this.equSysDescription = equSysDescription;
    }

    public String getEquSysFullName() {
        return equSysFullName == null ? "" : equSysFullName;
    }

    public void setEquSysFullName(String equSysFullName) {
        this.equSysFullName = equSysFullName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getEquSysParentSystemId() {
        return equSysParentSystemId;
    }

    public void setEquSysParentSystemId(Long equSysParentSystemId) {
        this.equSysParentSystemId = equSysParentSystemId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
