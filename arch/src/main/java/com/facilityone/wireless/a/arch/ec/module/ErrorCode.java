package com.facilityone.wireless.a.arch.ec.module;

/**
 * Created by Owen
 * <p/>
 * Email: owen.wang@facilityone.cn
 * <p/>
 * description:
 * <p/>
 * Date: 2017/8/3 11:20
 */

public class ErrorCode {

    public static final int SUCCESS=0;
    public static final int UNKONWN_ERROR=1;

    /**client_id 不存在 */
    public static final int INVALID_CLIENT=10001;

    /**client_id 对应的client_secret不正确 */
    public static final int INVALID_CLIENT_SECRET=10002;

    /**access_token 错误*/
    public static final int INVALID_AUTHORIZATION_CODE=10003;

    /**用户名密码错误 */
    public static final int INVALID_USERNAME_OR_PASSWORD=10004;

    /**app版本不正确*/
    public static final int INVALID_APP_VERSION=10005;

    /**app类型不正确 */
    public static final int INVALID_APP_TYPE=10006;


    /**项目不正确*/
    public static final int INVALID_PROJECT_ERROR=10007;

    /**项目到期错误*/
    public static final int INVALID_PROJECT_OVERDUE=10008;

    /**系统到期*/
    public static final int INVALID_SYSTEM_OVERDUE=10009;



    /**----------------------- 全局错误码 --------------------**/

    public static final int  LOGIN_SUCCESS = 200 ;// 请求成功

    /**
     * 全局错误 : 未授权的操作 : 未登陆
     */
    public static final int HTTP_401_NO_AUTHORIZED = 401;
    /**
     * 全局错误 : 未授权的操作 : 无权限
     */
    public static final int HTTP_403_FORBIDDEN = 403;
    /**
     * 系统错误
     */
    public static final int SYSTEM_ERROR = 500;
    /**
     * Common 参数错误
     */
    public static final int COMMON_ERROR_PARAM = 40001;
    /**
     * Common 数据不存在或已被删除
     * 比如修改某个数据，但是根据Id查询后，不存在此数据
     */
    public static final int COMMON_ITEM_NOT_EXIST = 40002;
    /**
     * Security 验证码错误
     */
    public static final int SECURITY_ERROR_CAPTCHA = 41001;
    /**
     * Security 账号或密码错误
     */
    public static final int SECURITY_ERROR_LOGIN_CODE_OR_PWD = 41002;
    /**
     * Security 密码错误次数达到上限
     */
    public static final int SECURITY_ERROR_PWD_COUNT_MAX = 41003;
    /**
     * Security 生成验证码错误
     */
    public static final int SECURITY_ERROR_GENERATE_CAPTCHA = 41004;
    /**
     * Security 登陆成功
     */
    public static final int SECURITY_LOGIN_SUCCESS = 41005;
    /**
     * Security 退出成功
     */
    public static final int SECURITY_LOGOUT_SUCCESS = 41006;
    /**
     * Security 密码强度不足，根据Regex来判断
     */
    public static final int SECURITY_PASSWORD_REGEX_FAIL = 41007;
    /**
     * Security 原密码不正确
     */
    public static final int SECURITY_OLD_PASSWORD_ERROR = 41008;
    /**
     * Security 未登录，需要登陆
     */
    public static final int SECURITY_NOT_LOGIN = 41009;
    /**
     * Security 对应的用户不存在
     */
    public static final int SECURITY_USER_NOT_EXIST = 41010;
    /**
     * Security 密码修改成功
     */
    public static final int SECURITY_PASSWORD_CHANGE_SUCCESS = 41011;
    /**
     * Security 第一次登陆，需要修改密码
     */
    public static final int SECURITY_FIRST_TIME_LOGIN = 41012;
    /**
     * Security 密码过期，需要修改密码 --> 具体过期时间，由配置文件决定
     */
    public static final int SECURITY_PASSWORD_EXPIRE = 41013;
    /**
     * Security 密码在周期内已经重复使用过，需要换一个密码 --> 一个周期的次数，由配置文件决定
     */
    public static final int SECURITY_PASSWORD_USED_IN_ONE_PERIOD = 41014;

    /**
     * Security IP被锁定（恶意攻击）
     */
    public static final int SECURITY_IP_BLOCKED = 41015;

    /**
     * LOGON_OTHER 被其他客户端挤掉
     */
    public static final int LOGON_OTHER = 41016;
}
