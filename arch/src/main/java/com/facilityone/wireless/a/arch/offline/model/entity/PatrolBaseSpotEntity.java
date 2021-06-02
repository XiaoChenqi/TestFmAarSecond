package com.facilityone.wireless.a.arch.offline.model.entity;

import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.offline.util.PatrolQrcodeUtils;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检离线基础点位
 * Date: 2018/10/30 5:25 PM
 */
public class PatrolBaseSpotEntity {

    private Long spotId;
    private String name;
    private String qrCode;
    private String code;
    private String nfcTag;
    private String spotType;
    private String spotLocation;
    private LocationBean location;
    private Integer deleted;

    public String getCode() {
        if (code == null) {
            return PatrolQrcodeUtils.parseSpotCode(qrCode);
        } else {
            return code;
        }
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrCode() {
        return qrCode == null ? "" : qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getNfcTag() {
        return nfcTag == null ? "" : nfcTag;
    }

    public void setNfcTag(String nfcTag) {
        this.nfcTag = nfcTag;
    }

    public String getSpotType() {
        return spotType == null ? "" : spotType;
    }

    public void setSpotType(String spotType) {
        this.spotType = spotType;
    }

    public String getSpotLocation() {
        return spotLocation == null ? "" : spotLocation;
    }

    public void setSpotLocation(String spotLocation) {
        this.spotLocation = spotLocation;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
