package com.facilityone.wireless.a.arch.offline.model.service;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:下载进度监听
 * Date: 2018/10/18 11:30 AM
 */
public interface OnDownloadListener {
    void onDownload(int max, int progress);

    void onAllSuccess();
    
    void onError();
}
