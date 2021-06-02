package com.facilityone.wireless.a.arch.ec.module;

import com.facilityone.wireless.a.arch.offline.dao.BuildingDao;
import com.facilityone.wireless.a.arch.offline.dao.CityDao;
import com.facilityone.wireless.a.arch.offline.dao.FloorDao;
import com.facilityone.wireless.a.arch.offline.dao.RoomDao;
import com.facilityone.wireless.a.arch.offline.dao.SiteDao;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:根据location对象获取全名
 * Date: 2019/3/21 2:27 PM
 */
public class LocationUtils {

    public static String getStrLocation(LocationBean position) {
        String locationName = "";
        if (position != null) {
            if (position.roomId != null) {
                RoomDao dao = new RoomDao();
                locationName = dao.queryLocationName(position.roomId);
            } else if (position.floorId != null) {
                FloorDao dao = new FloorDao();
                locationName = dao.queryLocationName(position.floorId);
            } else if (position.buildingId != null) {
                BuildingDao dao = new BuildingDao();
                locationName = dao.queryLocationName(position.buildingId);
            } else if (position.siteId != null) {
                SiteDao dao = new SiteDao();
                locationName = dao.queryLocationName(position.siteId);
            } else if (position.cityId != null) {
                CityDao dao = new CityDao();
                locationName = dao.queryLocationName(position.cityId);
            }
        }
        return locationName;
    }
}
