package com.facilityone.wireless.a.arch.offline.util;

import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.tencent.wcdb.Cursor;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:把默认值0修改为null
 * Date: 2018/11/22 10:00 AM
 */
public class LocationNullUtils {

    /**
     * 把默认值0修改为null
     *
     * @param cursor
     * @return
     */
    public static LocationBean getNullLocation(Cursor cursor, String city, String site, String building, String floor, String room) {
        LocationBean location = new LocationBean();
        long city_id = cursor.getLong(cursor.getColumnIndex(city));
        long site_id = cursor.getLong(cursor.getColumnIndex(site));
        long building_id = cursor.getLong(cursor.getColumnIndex(building));
        long floor_id = cursor.getLong(cursor.getColumnIndex(floor));
        long room_id = cursor.getLong(cursor.getColumnIndex(room));
        location.cityId = city_id == 0L ? null : city_id;
        location.siteId = site_id == 0L ? null : site_id;
        location.buildingId = building_id == 0L ? null : building_id;
        location.floorId = floor_id == 0L ? null : floor_id;
        location.roomId = room_id == 0L ? null : room_id;
        return location;
    }

    public static LocationBean getNullLocation(Cursor cursor) {
        return getNullLocation(cursor, "CITY_ID", "SITE_ID", "BUILDING_ID", "FLOOR_ID", "ROOM_ID");
    }


}
