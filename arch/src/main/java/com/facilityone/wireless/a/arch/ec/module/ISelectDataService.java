package com.facilityone.wireless.a.arch.ec.module;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:选择数据类型
 * Date: 2018/10/25 11:53 AM
 */
public interface ISelectDataService {
    
    String SELECT_OFFLINE_DATA_BACK = "select_offline_data_back";
    
    int DATA_TYPE_LOCATION = 0;
    int DATA_TYPE_DEP = 1;
    int DATA_TYPE_FLOW_PRIORITY = 2;
    int DATA_TYPE_PRIORITY = 3;
    int DATA_TYPE_WORKORDER_TYPE = 4;
    int DATA_TYPE_DEMAND_TYPE = 5;
    int DATA_TYPE_SERVICE_TYPE = 6;
    int DATA_TYPE_EQU = 7;
    int DATA_TYPE_EQU_TYPE = 8;
    int DATA_TYPE_PATROL_EXCEPTION = 9;
    int DATA_TYPE_KNOWLEDGE = 10;
    int DATA_TYPE_EQU_ALL = 11;
    int DATA_VISIT_PAY = 12;//拜访对象

    int LOCATION_CITY = 0;
    int LOCATION_SITE = 1;
    int LOCATION_BUILDING = 2;
    int LOCATION_FLOOR = 3;
    int LOCATION_ROOM = 4;
}
