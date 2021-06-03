package com.facilityone.wireless.maintenance.model;

/**
 * Created by peter.peng on 2018/11/16.
 */

public interface MaintenanceUrl {

    //获取维护日历url
    String MAINTENANCE_CALENDAR_URL = "/m/v1/preventive/todos/query";
    //获取计划性维护详情
    String MAINTENANCE_INFO_URL = "/m/v2/preventive/info";
}
