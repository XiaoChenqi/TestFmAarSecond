package com.facilityone.wireless.a.arch.ec.module;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:app更新信息
 * Date: 2019/1/2 10:34 AM
 */
public class UpdateService {
    public static class UpdateInfoBean {
        public Integer code;
        public String desc;
        public String log;
        public Long fileId;
        public String serverAddress;
        public String fileName;
        public String filePath;
        public String downloadUrl;
        public Long releaseTime;
        public Integer updateType;
        public Integer receiveUpdateType;
        public Integer suggestUpdateType;

        @Override
        public String toString() {
            return "UpdateInfoBean{" +
                    "code=" + code +
                    ", desc='" + desc + '\'' +
                    ", log='" + log + '\'' +
                    ", fileId=" + fileId +
                    ", serverAddress='" + serverAddress + '\'' +
                    ", fileName='" + fileName + '\'' +
                    ", filePath='" + filePath + '\'' +
                    ", downloadUrl='" + downloadUrl + '\'' +
                    ", releaseTime=" + releaseTime +
                    ", updateType=" + updateType +
                    ", receiveUpdateType=" + receiveUpdateType +
                    ", suggestUpdateType=" + suggestUpdateType +
                    '}';
        }
    }
}
