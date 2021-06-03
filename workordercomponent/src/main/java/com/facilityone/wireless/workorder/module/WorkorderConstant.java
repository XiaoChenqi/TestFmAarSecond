package com.facilityone.wireless.workorder.module;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单常量
 * Date: 2018/7/3 下午5:11
 */
public interface WorkorderConstant {
    //二级菜单
    int WORKORER_CREATE = 0;          //创建工单
    int WORKORER_PROCESS = 1;         //待处理工单
    int WORKORER_DISPATCHING = 2;     //待派工工单
    int WORKORER_AUDIT = 3;           //待审批工单
    int WORKORER_VALIDATION = 4;      //待验证工单
    int WORKORER_ARCHIVE = 5;         //待存档工单
    int WORKORER_QUERY = 6;           //工单查询

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

    //工单类型
    int WORK_TYPE_CM = 0;               // cm
    int WORK_TYPE_ZM = 1;               // zm
    int WORK_TYPE_PM = 2;               // pm
    /**
     * 历史记录
     **/
    int WORKORDER_HIS_CREATE = 0;                   // 创建
    int WORKORDER_HIS_DISPATCH = 1;                 // 派工
    int WORKORDER_HIS_RECEIVE = 2;                  //接单
    int WORKORDER_HIS_UPDATE = 3;                   //更新
    int WORKORDER_HIS_STOP = 4;                     //暂停
    int WORKORDER_HIS_TERMINATE = 5;                //终止
    int WORKORDER_HIS_FINISH = 6;                   //完成
    int WORKORDER_HIS_VALIDATE = 7;                 //验证
    int WORKORDER_HIS_CLOSE = 8;                    //关闭
    int WORKORDER_HIS_APPROVAL_REQUEST = 9;         //申请审批
    int WORKORDER_HIS_APPROVE = 10;                 //批准申请
    int WORKORDER_HIS_ESCALATION = 11;              //工单升级
    int WORKORDER_HIS_CONTINUE = 12;                //继续
    int WORKORDER_HIS_REJECT_ORDER = 13;            //退单

    /**
     * 执行人状态
     */
    int WORKORDER_STATUS_PERSONAL_UN_ACCEPT = 0;//未接单
    int WORKORDER_STATUS_PERSONAL_ACCEPT = 1;   //已接单
    int WORKORDER_STATUS_PERSONAL_BACK = 2;     //已退单
    int WORKORDER_STATUS_PERSONAL_SUBMIT = 3;   //已提交

    /**
     * 执行人岗位状态
     */
    int WORKORDER_STATUS_PERSONAL_NO = 0;       //离岗
    int WORKORDER_STATUS_PERSONAL_ON = 1;       //在岗
    int WORKORDER_STATUS_PERSONAL_NONE = 2;     //没有参与考勤

    /**
     * 工单操作
     */
    int WORKORDER_OPT_TYPE_SINGLE_BACK = 1;            // 1 — 退单
    int WORKORDER_OPT_TYPE_SUSPENSION_CONTINUED = 2;   // 2 — 暂停（继续工作）
    int WORKORDER_OPT_TYPE_SUSPENSION_NO_FURTHER = 3;  // 3 — 暂停（不继续工作）
    int WORKORDER_OPT_TYPE_TERMINATE = 4;              // 4 — 终止
    int WORKORDER_OPT_TYPE_COMPLETION = 5;             // 5 — 处理完成
    int WORKORDER_OPT_TYPE_VERIFY_PASS = 6;            // 6 — 验证（通过）
    int WORKORDER_OPT_TYPE_VERIFY_FAIL = 7;            // 7 — 验证（不通过）
    int WORKORDER_OPT_TYPE_ARCHIVE = 8;                // 8 — 存档
    int WORKORDER_OPT_TYPE_ORDER = 9;                  // 9 — 接单
    int WORKORDER_OPT_TYPE_CONTINUE = 10;              // 10 — 继续工作

    /**
     * 审批类型
     */
    int WORKORDER_APPROVAL_TYPE_DEFAULT = 1;
    int WORKORDER_APPROVAL_PASS = 1;            //审批通过
    int WORKORDER_APPROVAL_FAIL = 2;            //审批拒绝

    /**
     * 故障设备操作类型
     */
    int WORKORDER_DEVICE_ADD_OPT_TYPE = 0;   //add
    int WORKORDER_DEVICE_UPDATE_OPT_TYPE = 1;//update
    int WORKORDER_DEVICE_DEL_OPT_TYPE = 2;   //del 

    /**
     * 工具操作类型
     */
    int WORKORDER_TOOL_ADD_OPT_TYPE = 0;   //add
    int WORKORDER_TOOL_UPDATE_OPT_TYPE = 1;//update
    int WORKORDER_TOOL_DEL_OPT_TYPE = 2;   //del

    /**
     * 收取明细
     */
    int WORKORDER_CHARGE_ADD_OPT_TYPE = 0;   //add
    int WORKORDER_CHARGE_UPDATE_OPT_TYPE = 1;//update
    int WORKORDER_CHARGE_DEL_OPT_TYPE = 2;   //del

    /**
     * 空间位置
     */
    int WORKORDER_SPACE_ADD_OPT_TYPE = 0;   //add
    int WORKORDER_SPACE_UPDATE_OPT_TYPE = 1;//update
    int WORKORDER_SPACE_DEL_OPT_TYPE = 2;   //del

    /**
     * 缴费单状态
     */
    int WORKORDER_PAYMENT_UNPAY = 0;
    int WORKORDER_PAYMENT_PAIED = 1;
    int WORKORDER_PAYMENT_INVOICE = 2;
    int WORKORDER_PAYMENT_CLOSE = 3;
    int WORKORDER_PAYMENT_INVALID = 4;

    //预定物资库存预定状态
    int RESERVE_STATUS_VERIFY_WAIT = 0;// 未审核
    int RESERVE_STATUS_VERIFY_PASS = 1;// 通过（待出库）
    int RESERVE_STATUS_VERIFY_BACK = 2;// 取消（已驳回）
    int RESERVE_STATUS_DELIVERIED = 3;// 已出库
    int RESERVE_STATUS_CANCEL = 4;// 取消出库（仓库管理员取消）
    int RESERVE_STATUS_CANCEL_BOOK = 5;//取消预定（预定人取消）
    
    //关联设备完成状态
    int WO_EQU_STAT_UNFINISH = 0; // 0 --- 未完成
    int WO_EQU_STAT_FINISHED = 1; // 1 --- 已完成
}
