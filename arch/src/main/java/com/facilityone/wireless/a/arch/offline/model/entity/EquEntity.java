package com.facilityone.wireless.a.arch.offline.model.entity;

import com.facilityone.wireless.a.arch.ec.module.LocationBean;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:设备实体
 * Date: 2018/10/18 5:00 PM
 */
public class EquEntity {
    private Long eqId;
    private Boolean deleted;
    private String code;
    private String name;
    private String qrcode;
    private String equSystem;
    private Long projectId;
    private LocationBean position;

    public Long getEqId() {
        return eqId;
    }

    public void setEqId(Long eqId) {
        this.eqId = eqId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public String getQrcode() {
        return qrcode == null ? "" : qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getEquSystem() {
        return equSystem == null ? "" : equSystem;
    }

    public void setEquSystem(String equSystem) {
        this.equSystem = equSystem;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public LocationBean getPosition() {
        return position;
    }

    public void setPosition(LocationBean position) {
        this.position = position;
    }
}
