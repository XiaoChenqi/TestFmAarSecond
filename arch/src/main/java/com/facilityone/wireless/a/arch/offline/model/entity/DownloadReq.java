package com.facilityone.wireless.a.arch.offline.model.entity;

import com.facilityone.wireless.a.arch.ec.module.Page;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:离线下载分页请求体
 * Date: 2018/10/18 4:01 PM
 */
public class DownloadReq {
    public Page page;
    public Long preRequestDate;
    public Long lastRequestTime;

    public DownloadReq() {
        this.page = new Page();
        this.page.setPageSize(100);
    }
}
