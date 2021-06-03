package com.facilityone.wireless.demand.module;

import android.content.Context;

import com.facilityone.wireless.demand.R;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by: owen.
 * Date: on 2018/6/22 下午4:57.
 * Description:
 * email:
 */

public class DemandHelper {

    public static Map<Integer, String> getDemandStatusMap(Context context) {
        Map<Integer, String> v = new LinkedHashMap<Integer, String>();
        String[] stat = context.getResources().getStringArray(R.array.service_demand_complete_stat);
        v.put(DemandConstant.DEMAND_STATUS_CREATED, stat[0]);
        v.put(DemandConstant.DEMAND_STATUS_PROGRESS, stat[1]);
        v.put(DemandConstant.DEMAND_STATUS_COMPLETED, stat[2]);
        v.put(DemandConstant.DEMAND_STATUS_EVALUATED, stat[3]);
        v.put(DemandConstant.DEMAND_STATUS_CANCEL, stat[4]);
        return v;
    }

    public static Map<Integer, String> getDemandOriginMap(Context context) {
        Map<Integer, String> v = new LinkedHashMap<Integer, String>();
        String[] stat = context.getResources().getStringArray(R.array.service_demand_origin_stat);
        v.put(DemandConstant.DEMAND_ORIGIN_WEB, stat[0]);
        v.put(DemandConstant.DEMAND_ORIGIN_APP, stat[1]);
        v.put(DemandConstant.DEMAND_ORIGIN_WECHAT, stat[2]);
        v.put(DemandConstant.DEMAND_ORIGIN_EMAIL, stat[3]);
        return v;
    }

    public static Map<Integer,String> getWorkOrderStatusMap(Context context){
        Map<Integer,String> v = new LinkedHashMap<>();
        String[] stauts = context.getResources().getStringArray(R.array.demand_workorder_status);
        v.put(DemandConstant.WORK_STATUS_CREATED,stauts[0]);
        v.put(DemandConstant.WORK_STATUS_PUBLISHED,stauts[1]);
        v.put(DemandConstant.WORK_STATUS_PROCESS,stauts[2]);
        v.put(DemandConstant.WORK_STATUS_SUSPENDED_GO,stauts[3]);
        v.put(DemandConstant.WORK_STATUS_TERMINATED,stauts[4]);
        v.put(DemandConstant.WORK_STATUS_COMPLETED,stauts[5]);
        v.put(DemandConstant.WORK_STATUS_VERIFIED,stauts[6]);
        v.put(DemandConstant.WORK_STATUS_ARCHIVED,stauts[7]);
        v.put(DemandConstant.WORK_STATUS_APPROVAL,stauts[8]);
        v.put(DemandConstant.WORK_STATUS_SUSPENDED_NO,stauts[9]);
        return v;
    }
}
