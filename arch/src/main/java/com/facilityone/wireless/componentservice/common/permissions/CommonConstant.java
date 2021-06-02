package com.facilityone.wireless.componentservice.common.permissions;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:app工程常量
 * Date: 2018/6/5 下午3:37
 */
public interface CommonConstant {
    /*************************消息item类型***********************/
    int MULTIPLE_ITEM_TITLE = 0;        //消息标题栏
    int MULTIPLE_ITEM_CONTENT = 1;      //消息内容栏
    int MULTIPLE_TYPE_ITEM_CONTENT = 2; //类型消息列表item

    /*************************消息类型/菜单类型***********************/
    int MESSAGE_ALL = 0;            //全部
    int MESSAGE_WORK_ORDER = 1;     //工单
    int MESSAGE_PATROL = 2;         //巡检
    int MESSAGE_MAINTANCE = 3;      //计划性维护
    int MESSAGE_ASSET = 4;          //资产
    int MESSAGE_DEMAND = 5;         //服务态（需求）
    int MESSAGE_INVENTORY = 6;      //库存
    int MESSAGE_CONTRACT = 7;       //合同
    int MESSAGE_MONITORING = 8;     //传感器 监测
    int MESSAGE_BULLETIN = 100;     //公告
    int MESSAGE_INSPECTION = 9;     //承接查验
    int MESSAGE_ENERGY = 10;          //能源
    int MESSAGE_SIGN = 11;        //在岗管理
    int MESSAGE_CHART = 12;       //报表
    int MESSAGE_PAYMENT = 13;     //缴费
    int MESSAGE_VISITOR = 14;     //访客
    int MESSAGE_KNOWLEDGE = 15;     //知识库

    int MESSAGE_UNREAD = 0;         //未读
    int MESSAGE_READED = 1;         //已读
    int MESSAGE_ALL_LIST = 2;       //全部

    //消息来源类型
    int MSG_FROM_TYPE = 0;//消息来源于消息类型界面(MessageFrgment)
    int MSG_FROM_LIST = 1;//消息来源于消息列表界面（MessageListFragment）
    int MSG_FROM_PUSH = 2;//消息来源于推送（通知栏）


}
