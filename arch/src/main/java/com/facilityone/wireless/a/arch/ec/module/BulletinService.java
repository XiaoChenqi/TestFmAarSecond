package com.facilityone.wireless.a.arch.ec.module;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:公告网络相关
 * Date: 2018/6/8 上午9:11
 */
public class BulletinService {
    public static final int BULLETIN_TOP = 0;    //查询置顶公告
    public static final int BULLETIN_UNREAD = 1; //查询未读公告
    public static final int BULLETIN_READ = 2;   //查询已读公告


    //公告列表
    public static class BulletinReq {
        public Integer type;
        public Page page;
    }


    public static class BulletinResp {
        public Page page;
        public List<BulletinBean> contents;
    }


    public static class BulletinBean {
        public Long bulletinId;
        public String title;
        public String creator;
        public Long time;
        public String imageId;
        public Boolean top;
        public Integer type;
    }

    /**
     * 公告详情
     */
    public static class BulletinInfoBean {
        public Long bulletinId;
        public String title;
        public Integer type;
        public String creator;
        public Long createTime;
        public Long startTime;
        public Long endTime;
        public String imageId;
        public Boolean top;
        public String content;
        public Long read;
        public Long unRead;
        public List<Attachment> attachment;
    }

    public static class Attachment {
        public String src;
        public String name;
    }


}
