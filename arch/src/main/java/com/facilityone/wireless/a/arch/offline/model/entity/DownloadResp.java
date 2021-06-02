package com.facilityone.wireless.a.arch.offline.model.entity;

import com.facilityone.wireless.a.arch.ec.module.Page;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:下载实体类
 * Date: 2018/10/18 4:27 PM
 */
public class DownloadResp<T> {
    public Page page;
    public List<T> contents;
    public Long requestTime;
}
