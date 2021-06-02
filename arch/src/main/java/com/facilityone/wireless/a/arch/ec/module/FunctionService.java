package com.facilityone.wireless.a.arch.ec.module;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:功能相关的服务
 * Date: 2018/6/8 下午5:04
 */
public class FunctionService {

    public static final int COUNT = 3;//一行几个功能模块(默认值)
    public static final int COUNT_2 = 2;//一行几个功能模块

    public static final int UNDO_TYPE_ALL = 0;            //获取所有类型的待处理任务
    public static final int UNDO_TYPE_WORK_ORDER = 1;     //获取工单类型（包括待处理，待派工，待审核，待验证工单）
    public static final int UNDO_TYPE_PATROL = 2;         //获取巡检类型
    public static final int UNDO_TYPE_DEMAND = 3;         //获取需求类型（获取待审核，待处理，待评价需求）
    public static final int UNDO_TYPE_INVENTORY = 4;      //获取库存类型（待审核的预定单数量）
    public static final int UNDO_TYPE_BULLETIN = 5;       //公告
    public static final int UNDO_TYPE_PAY = 6;            //获取缴费管理类型(待缴费单，已缴费单，退款管理单)
    public static final int UNDO_TYPE_INSPECTION = 7;     //承接查验类型(待处理，待验证)

    public static class FunctionBean implements Serializable, Comparable<FunctionBean> {
        public String name;
        public int type;
        public int imageId;
        public int undoNum;
        public int index;
        public boolean clickable;
        public Map<String, FunctionBean> childMenu;//子菜单
        public ArrayList<FunctionBean> sortChildMenu;//按照下标排序
        public Set<String> undoKey;//子孙未做数量
        public IService service;//可以获取需要跳转的fragment

        public FunctionBean() {
            this.childMenu = new HashMap<>();
            this.undoKey = new HashSet<>();
        }



        @Override
        public int compareTo(@NonNull FunctionBean o) {
            //比较此对象与指定对象的顺序。如果该对象小于、等于或大于指定对象，则分别返回负整数、零或正整数。
            return compare(this.index, o.index);
        }

        @Override
        public String toString() {
            return "FunctionBean{" +
                    "name='" + name + '\'' +
                    ", index=" + index +
                    ", childMenu=" + childMenu +
                    ", service=" + service +
                    '}';
        }

        private int compare(int index, int index2) {
            return (index > index2 ? 1 :
                    (index == index2 ? 0 : -1));
        }
    }

    //未做消息个数
    public static class FunctionUndoBean {
        public int undoOrderNumber;
        public int unArrangeOrderNumber;
        public int unApprovalOrderNumber;
        public int unArchivedOrderNumber;
        public int patrolTaskNumber;
        public int unApprovalRequirementNumber;
        public int undoRequirementNumber;
        public int unEvaluateRequirementNumber;
        public int toBeOutInventoryNumber;
        public int unApprovalInventoryNumber;
        public int unReadBulletinNumber;
        public int unPaidPaymentNumber;
        public int paidPaymentNumber;
        public int refundPaymentNumber;
        public int undoInspectionNumber;
        public int unArchivedInspectionNumber;

    }
}
