package com.facilityone.wireless.a.arch.offline.net;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:离线下载网址
 * Date: 2018/10/11 4:59 PM
 */
public interface OfflineUri {
    String PRIORITY_URL = "/m/v1/priority";
    String DEP_URL = "/m/v1/organizations/list";
    String EQU_TYPE_URL = "/m/v1/equipments/systems";
    String EQU_URL = "/m/v1/equipments/list";
    String SERVICE_TYPE_URL = "/m/v1/servicetype";
    String DEMAND_TYPE_URL = "/m/v1/requirementtype";
    String FLOW_URL = "/m/v1/process/list";
    String LOCATION_CSB_URL = "/m/v1/place/city/site/building";
    String LOCATION_FLOOR_URL = "/m/v1/place/floors";
    String LOCATION_ROOM_URL = "/m/v1/place/rooms";
    String PATROL_BASE_SPOT_URL = "/m/v3/patrol/spotlist";
    String PATROL_BASE_ITEM_URL = "/m/v3/patrol/contentlist";
    String PATROL_TASK_URL = "/m/v3/patrol/tasklist";
}
