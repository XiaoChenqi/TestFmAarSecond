package com.facilityone.wireless.patrol.module;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检常量
 * Date: 2018/10/30 3:10 PM
 */
public interface PatrolConstant {
    //二级菜单
    int PATROL_TASK = 0;          //巡检任务
    int PATROL_QUERY = 1;         //巡检查询
    
    // need nfc
    int PATROL_NO_NFC = 0;          //不需要
    int PATROL_NEED_NFC = 1;        //需要

    int PATROL_STATUS_NOT_START = 0;//未开始
    int PATROL_STATUS_ING = 1;//进行中
    int PATROL_STATUS_COMPLETED = 2;//已完成
    int PATROL_STATUS_DELAY = 3;//延期完成
    int PATROL_STATUS_UNCOMPLETED = 4;//未完成
    int PATROL_STATUS_INSPECTION = 5;//补检
    int PATROL_STATUS_CANCEL = 6;//取消

    //巡检操作类型
    int PATROL_OPT_TYPE_SYNC = 1;//同步
    int PATROL_OPT_TYPE_UPLOAD = 2;//提交完成

    //设备状态
    int EQU_IDLE = 0;//闲置
    int EQU_STOP = 1;//停运
    int EQU_USE = 2;//在用
    int EQU_REPAIR = 3;//维修
    int EQU_BAD = 4;//报废

    //检查项状态
    int ITEM_STATUS_NORMAL = 0;//正常
    int ITEM_STATUS_EXCEPTION = 1;//异常
    int ITEM_STATUS_MISS = 2;//漏检
    int ITEM_STATUS_ADD = 3;//补检
    int ITEM_STATUS_EXCEPTION_DEL = 4;//异常已处理
}
