package com.facilityone.wireless.demand.module;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:需求需要的常量
 * Date: 2018/6/21 下午3:59
 */
public interface DemandConstant {
    //菜单基础下标
    int DEMAND_CREATE = 0;   //创建
    int DEMAND_UNCHECK = 1;  //待审批
    int DEMAND_UNFINISH = 2; //待处理
    int DEMAND_FINISH = 3;   //待评价
    int DEMAND_QUERY = 4;    //查询

    //网络请求
    int DEMAND_REQUEST_UNCHECK = 0;  //待审批
    int DEMAND_REQUES_UNFINISH = 1; //待处理
    int DEMAND_REQUES_FINISH = 2;   //待评价
    int DEMAND_REQUES_MY = 3;   //我的需求
    int DEMAND_REQUES_QUERY = 4;    //查询

    //需求状态
    int DEMAND_STATUS_CREATED = 0; //已创建
    int DEMAND_STATUS_PROGRESS = 1; //处理中
    int DEMAND_STATUS_COMPLETED = 2; //已完成
    int DEMAND_STATUS_EVALUATED = 3; //已评价
    int DEMAND_STATUS_CANCEL = 4; //已取消

    //需求来源
    int DEMAND_ORIGIN_WEB = 0;//WEB
    int DEMAND_ORIGIN_APP = 1;//移动端
    int DEMAND_ORIGIN_WECHAT = 2;//微信
    int DEMAND_ORIGIN_EMAIL = 3;//邮箱
    int DEMAND_ORIGIN_DINGDING = 4;//钉钉

    //需求操作类型
    String DEMANDOPTTYPE = "demand_opt_type";
    int DEMAND_OPT_TYPE_PASS = 0;    //审核通过
    int DEMAND_OPT_TYPE_REFUSE = 1;  //审核拒绝
    int DEMAND_OPT_TYPE_SAVE = 2;    //保存
    int DEMAND_OPT_TYPE_FINISH = 3;  //处理完成
    int DEMAND_OPT_TYPE_PLEASED = 4; //满意度操作

    //需求记录
    int DEMAND_DETAIL_RECORD_DEMAND_CREATED = 0; // 创建需求
    int DEMAND_DETAIL_RECORD_DEMAND_APPROVAL = 1; // 审核
    int DEMAND_DETAIL_RECORD_WORKORDER_CREATE = 2; // 创建工单
    int DEMAND_DETAIL_RECORD_DEMAND_PROGRESS = 3; // 跟进
    int DEMAND_DETAIL_RECORD_DEMAND_EVALUATED = 4; // 回访
    int DEMAND_DETAIL_RECORD_DEMAND_COMPLETED = 5; // 结束

    //工单状态
    int WORK_STATUS_NONE = -1;                      // 无
    int WORK_STATUS_CREATED = 0;                    // 已创建
    int WORK_STATUS_PUBLISHED = 1;                  // 已发布
    int WORK_STATUS_PROCESS = 2;                    // 处理中
    int WORK_STATUS_SUSPENDED_GO = 3;               // 已暂停(继续工作)
    int WORK_STATUS_TERMINATED = 4;                 // 已终止
    int WORK_STATUS_COMPLETED = 5;                  // 已完成
    int WORK_STATUS_VERIFIED = 6;                   // 已验证
    int WORK_STATUS_ARCHIVED = 7;                   // 已存档
    int WORK_STATUS_APPROVAL = 8;                   // 已待审批
    int WORK_STATUS_SUSPENDED_NO = 9;               // 已暂停(不继续工作)
    
    
    int DEMAND_RULES_DEL = 1;               // 1—处理权限
    int DEMAND_RULES_SP = 2;                // 2—审批权限
    int DEMAND_RULES_PJ = 3;                // 3—评价权限

}
