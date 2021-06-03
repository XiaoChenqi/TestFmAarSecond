package com.facilityone.wireless.workorder.module;

import java.util.List;

/**
 * Created by peter.peng on 2019/6/19.
 * 工单存储大数据传递使用
 */

public enum  WorkorderDataHolder {

    INSTANCE;

    private Object mDeviceData;

    public static boolean hasDeviceData() {
        return INSTANCE.mDeviceData != null;
    }

    public static void setDeviceData(final Object objectList) {
        INSTANCE.mDeviceData = null;
        INSTANCE.mDeviceData = objectList;
    }

    public static Object getDeviceData() {
        final Object retList = INSTANCE.mDeviceData;
        INSTANCE.mDeviceData = null;
        return retList;
    }

}
