package com.facilityone.wireless.a.arch.ec.logon;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/5/29 下午3:17
 */
public interface FMCode {

    //  0 --- 请求成功
    //  1 --- 未知错误
    //  10001 --- client_id 不存在
    //  10002 --- client_secret 不正确
    //  10003 --- access_token 错误
    //  10004 --- 用户名或密码错误
    //  10005 --- 版本不正确（app 版本，服务器端通过配置控制）
    //  10006 --- 类型不正确（app 类型，正确的值为 Android 或 iOS）
    //  10007 --- 项目不正确（不在当前项目内）
    //  10008 --- 项目已到期
    //  10009 --- 系统已到期
    int SUCCESS = 0;
    int UNKONN_ERROR = 1;
    int CLIENT_ID_NONE = 10001;
    int CLIENT_SECRET_ERROR = 10002;
    int ACCESS_TOKEN_ERROR = 10003;
    int USERNAME_PASSWORD_ERROE = 10004;
    int VERAION_ERROR = 10005;
    int TYPE_ERROR = 10006;
    int PROJECT_ERROR = 10007;
    int PROJECT_OVERDUE = 10008;
    int SYSTEM_OVERDUE = 10009;

}
