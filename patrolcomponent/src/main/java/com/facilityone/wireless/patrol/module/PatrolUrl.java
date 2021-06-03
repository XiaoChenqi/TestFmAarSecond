package com.facilityone.wireless.patrol.module;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检uil
 * Date: 2018/11/6 4:03 PM
 */
public interface PatrolUrl {
    //巡检任务状态更新记录
    String PATROL_TASK_STATUS = "/m/v3/patrol/status";
    //同步保存
    String PATROL_TASK_UPLOAD = "/m/v4/patrol/tasks/save";
    //巡检查询
    String PATROL_TASK_QUERY = "/m/v1/patrol/tasks/query";
    //巡检查询点位
    String PATROL_TASK_QUERY_SPOT = "/m/v2/patrol/tasks/info";
    //巡检查询设备检查项
    String PATROL_TASK_QUERY_EQU_ITEM = "/m/v1/patrol/tasks/equipment";
    //标记巡检项为异常已处理
    String PATROL_TASK_QUERY_EQU_ITEM_MARK_DEL = "/m/v1/patrol/tasks/mark";
    //need nfc
    String PATROL_NEED_NFC_DEL = "/m/v1/project/nfc/setting";
}
