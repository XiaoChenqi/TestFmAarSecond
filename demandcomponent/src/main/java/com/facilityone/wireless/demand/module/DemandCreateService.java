package com.facilityone.wireless.demand.module;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:需求创建
 * Date: 2018/6/22 下午12:05
 */
public class DemandCreateService {

    public static class DemandCreateReq {
        public String requester;
        public String contact;
        public String desc;
        public Long typeId;
        public List<String> photoIds;
        public List<String> audioIds;
        public List<String> videoIds;
    }

}
