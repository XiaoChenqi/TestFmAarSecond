package com.facilityone.wireless.maintenance.model;

import android.content.Context;

import com.facilityone.wireless.maintenance.R;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by peter.peng on 2018/11/16.
 */

public class MaintenanceHelper {

    public static Map<Integer, String> getMaintenanceStatusMap(Context context) {
        Map<Integer, String> v = new LinkedHashMap<Integer, String>();
        String[] array = context.getResources().getStringArray(R.array.maintenance_Status);
        v.put(MaintenanceConstant.MAINTENANCE_WORKORDER_UNDO, array[0]);
        v.put(MaintenanceConstant.MAINTENANCE_WORKORDER_DOING, array[1]);
        v.put(MaintenanceConstant.MAINTENANCE_WORKORDER_FINISHED, array[2]);
        v.put(MaintenanceConstant.MAINTENANCE_WORKORDER_MISS, array[3]);
        return v;
    }

    public static Map<Integer, String> getMaintenanceWorkorderStatusMap(Context context) {
        Map<Integer, String> v = new LinkedHashMap<Integer, String>();
        String[] stat = context.getResources().getStringArray(R.array.maintenance_workorder_status);
        v.put(MaintenanceConstant.WORKORDER_STATUS_CREATED, stat[0]);
        v.put(MaintenanceConstant.WORKORDER_STATUS_PUBLISHED, stat[1]);
        v.put(MaintenanceConstant.WORKORDER_STATUS_PROCESS, stat[2]);
        v.put(MaintenanceConstant.WORKORDER_STATUS_SUSPENDED_GO, stat[3]);
        v.put(MaintenanceConstant.WORKORDER_STATUS_TERMINATED, stat[4]);
        v.put(MaintenanceConstant.WORKORDER_STATUS_COMPLETED, stat[5]);
        v.put(MaintenanceConstant.WORKORDER_STATUS_VERIFIED, stat[6]);
        v.put(MaintenanceConstant.WORKORDER_STATUS_ARCHIVED, stat[7]);
        v.put(MaintenanceConstant.WORKORDER_STATUS_APPROVAL, stat[8]);
        v.put(MaintenanceConstant.WORKORDER_STATUS_SUSPENDED_NO, stat[9]);
        return v;
    }


    /**
     * 月份转为应为简写
     * 不需要翻译
     *
     */
    public static Map<Long, String> getMonthMap(Context context) {
        Map<Long, String> v = new LinkedHashMap<Long, String>();
        String[] stat = context.getResources().getStringArray(R.array.month_trans);
        v.put(MaintenanceConstant.JANUARY, stat[0]);
        v.put(MaintenanceConstant.FEBRUARY, stat[1]);
        v.put(MaintenanceConstant.MARCH, stat[2]);
        v.put(MaintenanceConstant.APRIL, stat[3]);
        v.put(MaintenanceConstant.MAY, stat[4]);
        v.put(MaintenanceConstant.JUNE, stat[5]);
        v.put(MaintenanceConstant.JULY, stat[6]);
        v.put(MaintenanceConstant.AUGUST, stat[7]);
        v.put(MaintenanceConstant.SEPTEMBER, stat[8]);
        v.put(MaintenanceConstant.OCTOBER, stat[9]);
        v.put(MaintenanceConstant.NOVEMBER, stat[10]);
        v.put(MaintenanceConstant.DECEMBER, stat[11]);
        return v;
    }

}
