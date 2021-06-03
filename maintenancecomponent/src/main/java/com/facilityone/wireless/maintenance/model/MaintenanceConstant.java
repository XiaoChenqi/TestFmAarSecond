package com.facilityone.wireless.maintenance.model;

/**
 * Created by peter.peng on 2018/11/16.
 * 计划性维护常量
 */

public interface MaintenanceConstant {

    //计划性维护工单状态
    int MAINTENANCE_WORKORDER_UNDO = 1;
    int MAINTENANCE_WORKORDER_DOING = 2;
    int MAINTENANCE_WORKORDER_FINISHED = 3;
    int MAINTENANCE_WORKORDER_MISS = 4;

    int TAG_MENU_MAINTENANCE_CONTENT = 0;//维护内容
    int TAG_MENU_MAINTENANCE_OBJECT = 1;//对象
    int TAG_MENU_MAINTENANCE_WORK_ORDER = 2;//维护工单


    long JANUARY = 1; //一月
    long FEBRUARY = 2; // 二月
    long MARCH = 3; // 三月
    long APRIL = 4; // 四月
    long MAY = 5; // 五月
    long JUNE = 6; // 六月
    long JULY = 7; // 七月
    long AUGUST = 8; // 八月
    long SEPTEMBER = 9; // 九月
    long OCTOBER = 10; // 十月
    long NOVEMBER = 11; // 十一月
    long DECEMBER = 12; // 十二月

    //维护工单状态
    int WORKORDER_STATUS_NONE = -1;                      // 无
    int WORKORDER_STATUS_CREATED = 0;                    // 已创建
    int WORKORDER_STATUS_PUBLISHED = 1;                  // 已发布
    int WORKORDER_STATUS_PROCESS = 2;                    // 处理中
    int WORKORDER_STATUS_SUSPENDED_GO = 3;               // 已暂停(继续工作)
    int WORKORDER_STATUS_TERMINATED = 4;                 // 已终止
    int WORKORDER_STATUS_COMPLETED = 5;                  // 已完成
    int WORKORDER_STATUS_VERIFIED = 6;                   // 已验证
    int WORKORDER_STATUS_ARCHIVED = 7;                   // 已存档
    int WORKORDER_STATUS_APPROVAL = 8;                   // 已待审批
    int WORKORDER_STATUS_SUSPENDED_NO = 9;               // 已暂停(不继续工作)

    //日历切换状态
    int CALENDAR_STATUS_SELECT_DAY = 0;//选中某一天
    int CALENDAR_STATUS_SWITCH_MONTH = 1;//切换月份
    int CALENDAR_STATUS_SWITCH_LAST_SELECT_DAY = 2;//切换到上次选中的日期

    //recyclerview的显示类型
    int TYPE_TITLE = 0;
    int TYPE_CONTENT = 1;
    int TYPE_EQUIPEMNT = 2;
    int TYPE_SPACE = 3;
}
