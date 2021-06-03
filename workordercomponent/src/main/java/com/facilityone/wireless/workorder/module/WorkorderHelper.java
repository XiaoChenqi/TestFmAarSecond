package com.facilityone.wireless.workorder.module;

import android.content.Context;

import com.facilityone.wireless.workorder.R;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/4 下午5:17
 */
public class WorkorderHelper {

    public static Map<Integer, String> getWorkStatusMap(Context context) {
        Map<Integer, String> v = new LinkedHashMap<Integer, String>();
        String[] stat = context.getResources().getStringArray(R.array.workorder_status);
        v.put(WorkorderConstant.WORK_STATUS_CREATED, stat[0]);
        v.put(WorkorderConstant.WORK_STATUS_PUBLISHED, stat[1]);
        v.put(WorkorderConstant.WORK_STATUS_PROCESS, stat[2]);
        v.put(WorkorderConstant.WORK_STATUS_SUSPENDED_GO, stat[3]);
        v.put(WorkorderConstant.WORK_STATUS_TERMINATED, stat[4]);
        v.put(WorkorderConstant.WORK_STATUS_COMPLETED, stat[5]);
        v.put(WorkorderConstant.WORK_STATUS_VERIFIED, stat[6]);
        v.put(WorkorderConstant.WORK_STATUS_ARCHIVED, stat[7]);
        v.put(WorkorderConstant.WORK_STATUS_APPROVAL, stat[8]);
        v.put(WorkorderConstant.WORK_STATUS_SUSPENDED_NO, stat[9]);
        return v;
    }

    public static Map<Integer, String> getHistoryRecorderMap(Context context) {
        Map<Integer, String> v = new LinkedHashMap<>();
        String[] workerHistory = context.getResources()
                .getStringArray(R.array.workorder_info_history_step);
        v.put(WorkorderConstant.WORKORDER_HIS_CREATE, workerHistory[0]);
        v.put(WorkorderConstant.WORKORDER_HIS_DISPATCH, workerHistory[1]);
        v.put(WorkorderConstant.WORKORDER_HIS_RECEIVE, workerHistory[2]);
        v.put(WorkorderConstant.WORKORDER_HIS_UPDATE, workerHistory[3]);
        v.put(WorkorderConstant.WORKORDER_HIS_STOP, workerHistory[4]);
        v.put(WorkorderConstant.WORKORDER_HIS_TERMINATE, workerHistory[5]);
        v.put(WorkorderConstant.WORKORDER_HIS_FINISH, workerHistory[6]);
        v.put(WorkorderConstant.WORKORDER_HIS_VALIDATE, workerHistory[7]);
        v.put(WorkorderConstant.WORKORDER_HIS_CLOSE, workerHistory[8]);
        v.put(WorkorderConstant.WORKORDER_HIS_APPROVAL_REQUEST, workerHistory[9]);
        v.put(WorkorderConstant.WORKORDER_HIS_APPROVE, workerHistory[10]);
        v.put(WorkorderConstant.WORKORDER_HIS_ESCALATION, workerHistory[11]);
        v.put(WorkorderConstant.WORKORDER_HIS_CONTINUE, workerHistory[12]);
        v.put(WorkorderConstant.WORKORDER_HIS_REJECT_ORDER, workerHistory[13]);
        return v;
    }

    public static Map<Integer, String> getLaborerStatusMap(Context context) {
        Map<Integer, String> v = new LinkedHashMap<>();
        String[] workerStat = context.getResources().getStringArray(R.array.workorder_net_get_worker_stat);
        v.put(WorkorderConstant.WORKORDER_STATUS_PERSONAL_UN_ACCEPT, workerStat[0]);
        v.put(WorkorderConstant.WORKORDER_STATUS_PERSONAL_ACCEPT, workerStat[1]);
        v.put(WorkorderConstant.WORKORDER_STATUS_PERSONAL_BACK, workerStat[2]);
        v.put(WorkorderConstant.WORKORDER_STATUS_PERSONAL_SUBMIT, workerStat[3]);
        return v;
    }

    public static Map<Integer, String> getPaymentStatus(Context context) {
        Map<Integer, String> v = new LinkedHashMap<>();
        String[] paymentStat = context.getResources().getStringArray(R.array.workorder_payment_stat);
        v.put(WorkorderConstant.WORKORDER_PAYMENT_UNPAY, paymentStat[0]);
        v.put(WorkorderConstant.WORKORDER_PAYMENT_PAIED, paymentStat[1]);
        v.put(WorkorderConstant.WORKORDER_PAYMENT_INVOICE, paymentStat[2]);
        v.put(WorkorderConstant.WORKORDER_PAYMENT_CLOSE, paymentStat[3]);
        v.put(WorkorderConstant.WORKORDER_PAYMENT_INVALID, paymentStat[4]);
        return v;
    }

    /**
     * 获取工单预定物资库存状态
     *
     * @param context
     * @return
     */
    public static Map<Integer, String> getInventoryStatusMap(Context context) {
        Map<Integer, String> v = new LinkedHashMap<Integer, String>();
        String[] workerStat = context.getResources().getStringArray(R.array.inventory_reserve_record_status);
        v.put(WorkorderConstant.RESERVE_STATUS_VERIFY_WAIT, workerStat[0]);
        v.put(WorkorderConstant.RESERVE_STATUS_VERIFY_PASS, workerStat[1]);
        v.put(WorkorderConstant.RESERVE_STATUS_VERIFY_BACK, workerStat[2]);
        v.put(WorkorderConstant.RESERVE_STATUS_DELIVERIED, workerStat[3]);
        v.put(WorkorderConstant.RESERVE_STATUS_CANCEL, workerStat[4]);
        v.put(WorkorderConstant.RESERVE_STATUS_CANCEL_BOOK, workerStat[5]);
        return v;
    }

    public static Map<Integer, String> getWoEquipStatMap(Context context) {
        Map<Integer, String> v = new LinkedHashMap<Integer, String>();
        String[] stat = context.getResources().getStringArray(R.array.workorder_equip_stat);
        v.put(WorkorderConstant.WO_EQU_STAT_UNFINISH, stat[0]);
        v.put(WorkorderConstant.WO_EQU_STAT_FINISHED, stat[1]);
        return v;
    }
}
