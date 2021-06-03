package com.facilityone.wireless.patrol.module;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检任务状态请求
 * Date: 2018/11/6 5:54 PM
 */
public class PatrolStatusReq {
    public List<Long> task;

    public PatrolStatusReq(List<Long> task) {
        this.task = task;
    }
}
