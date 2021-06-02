package com.facilityone.wireless.a.arch.ec.logon;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/5/28 下午2:44
 */
public interface UserUrl {
    //登录地址
    String LOGON_URL = "/m/security/login";
    //用户信息
    String USER_INFO_URL = "/m/v1/user/info";
    //用户设置头像
    String UPLOAD_PROFILE_URL = "/m/v1/user/photo";
    //绑定手机
    String USER_BIND_PHONE_URL = "/m/v1/user/bind";
    //重置密码
    String USER_RESET_PASSWORD_URL ="/m/v1/user/repwd";
    //查询指定客户App下载二维码地址
    String GET_DOWNLOAD_QRCODE_URL ="/quickdownload/customer/qrcode";
    //获取当前项目下的员工列表
    String USER_LIST_URL ="/m/v1/user/list";

}
