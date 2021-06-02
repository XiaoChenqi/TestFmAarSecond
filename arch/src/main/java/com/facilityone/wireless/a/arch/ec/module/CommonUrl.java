package com.facilityone.wireless.a.arch.ec.module;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:多个模块用到的放在这里
 * Date: 2018/6/8 上午9:13
 */
public interface CommonUrl {
    //公告列表
    String BULLETIN_LIST_URL = "/m/v1/bulletin/query";
    String COMMON_UNDO_URL = "/m/v2/common/tasks/undo";
    //权限
    String COMMON_PERMISSIONS_URL = "/m/v1/function/permission";
    //上传文件
    String UPLOAD_IMAGE_URL = "/m/v1/files/upload/picture";
    String UPLOAD_VOICE_URL = "/m/v1/files/upload/voicemedia";
    String UPLOAD_VIDEO_URL = "/m/v1/files/upload/videomedia";
    String UPLOAD_ATTACH_URL = "/m/v1/files/upload/attachment";
    //工作组
    String WORK_TEAM_URL = "/m/v1/common/workTeam";
    //更新检查
    String UPDATE_CHECK = "/mobile/v3/version/neweast";
    // 下载
    String UPDATE_DOWNLOAD_URL = "/mobile/download/";
    //获取员工列表（附带部门和位置信息）
    String USER_LIST = "/m/v1/user/detaillist/";
}
