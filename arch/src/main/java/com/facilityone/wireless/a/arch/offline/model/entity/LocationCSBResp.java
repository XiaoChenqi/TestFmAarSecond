package com.facilityone.wireless.a.arch.offline.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:位置中城市区域楼区返回实体
 * Date: 2018/10/19 12:18 PM
 */
public class LocationCSBResp {
    private List<LocationCityEntity> city;
    private List<LocationSiteEntity> site;
    private List<LocationBuildingEntity> building;

    public List<LocationCityEntity> getCity() {
        if (city == null) {
            return new ArrayList<>();
        }
        return city;
    }

    public void setCity(List<LocationCityEntity> city) {
        this.city = city;
    }

    public List<LocationSiteEntity> getSite() {
        if (site == null) {
            return new ArrayList<>();
        }
        return site;
    }

    public void setSite(List<LocationSiteEntity> site) {
        this.site = site;
    }

    public List<LocationBuildingEntity> getBuilding() {
        if (building == null) {
            return new ArrayList<>();
        }
        return building;
    }

    public void setBuilding(List<LocationBuildingEntity> building) {
        this.building = building;
    }
}
