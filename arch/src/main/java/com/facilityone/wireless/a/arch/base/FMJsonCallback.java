package com.facilityone.wireless.a.arch.base;

import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.ec.logon.LogonManager;
import com.facilityone.wireless.a.arch.ec.module.ErrorCode;
import com.facilityone.wireless.a.arch.ec.module.LogonResponse;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.componentservice.app.AppService;
import com.fm.tool.network.callback.JsonCallback;
import com.fm.tool.network.model.BaseResponse;
import com.fm.tool.network.model.SimpleResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.stream.JsonReader;
import com.luojilab.component.componentlib.router.Router;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.exception.StorageException;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:基于fm业务逻辑二次封装
 * Date: 2018/5/28 下午6:08
 */
public abstract class FMJsonCallback<T> extends AbsCallback<T> {
    public static final int FM_NET_ERROR_ORDER = 8000;  //工单相关错误
    public static final int FM_NET_ERROR_ORDER_DATA_ERROR = 8001;  //工单 -- 请求数据不正确
    public static final int FM_NET_ERROR_ORDER_STATUS_CHANGED = 8003;  //工单 -- 状态已经变更
    public final static int FM_NET_ERROR_INVENTORY_ERROR = 9001;

    private BaseResponse mBaseResponse;

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        //这里得到第二层泛型的所有类型: BaseResponse<Bean>
        Type type = params[0];

        if (!(type instanceof ParameterizedType)) {
            throw new IllegalStateException("没有填写泛型参数!");
        }
        //这里得到第二层数据的真是类型: BaseResponse
        Type rawType = ((ParameterizedType) type).getRawType();
        //这里得到第二层数据的泛型的真实类型: Bean
        Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];

        ResponseBody body = response.body();
        if (body == null) {
            throw new IllegalStateException("返回值为null,查看服务端");
        }

        Gson gson = new GsonBuilder()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                .registerTypeAdapter(long.class, new LongDefault0Adapter())
                .registerTypeAdapter(Float.class, new FloatDefault0Adapter())
                .registerTypeAdapter(long.class, new FloatDefault0Adapter())
                .create();
        JsonReader jsonReader = new JsonReader(body.charStream());
        //写入的参数类型不对应那么久按照原始解析
        if (rawType != BaseResponse.class) {
            T data = gson.fromJson(jsonReader, type);
            response.close();
            return data;
        } else {
            if (typeArgument == Void.class) {
                //无数据类型，new DialogCallBack<BaseResponse<Void>> 以这种形式传递的泛型
                SimpleResponse simpleResponse = gson.fromJson(jsonReader, type);
                response.close();
                return (T) simpleResponse.toBaseResponse();
            } else {
                //有数据类型，表示有data
                mBaseResponse = gson.fromJson(jsonReader, type);
                response.close();
                int fmcode = mBaseResponse.fmcode;
                int code = mBaseResponse.code;
                if (code != 0 && code != 200) {
                    throw new HttpException(code + "");
                }
                if (fmcode == 0) {
                    return (T) mBaseResponse;
                } else {
                    throw new IllegalStateException(fmcode + "");
                }
            }
        }
    }

    @Override
    public void onError(Response<T> response) {
        response.setBody((T) mBaseResponse);
        Throwable exception = response.getException();
        if (exception != null) {
            exception.printStackTrace();
        }

        if (exception instanceof UnknownHostException || exception instanceof ConnectException) {
            // 这种异常的原因就是你网络的问题
            // 确定你url地址写对了？
            // 你的网络环境是正常的？还是只能内网才能访问？
            // 是否开代理了，导致连不上？
//            ToastUtils.showShort(R.string.arch_network_error);
            System.out.println("网络连接失败，请连接网络!");
        } else if (exception instanceof SocketTimeoutException) {
            // 这很明显就是连接超时了嘛
            // ，我这里故意把超时时间设置为1ms，所以故意让他报的这个异常，解决办法就是
            // 确定网络环境良好
            // 超时时间设置是否正确
            System.out.println("网络请求超时");
        } else if (exception instanceof HttpException) {
            System.out.println("查看服务端响应码");
            try {
                int code = mBaseResponse.code;
                delError(code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (exception instanceof StorageException) {
            System.out.println("sd卡不存在或没有权限");
        } else if (exception instanceof IllegalStateException) {
            String message = exception.getMessage();
            System.out.println(message);
            try {
                int fmCode = mBaseResponse.fmcode;
                switch (fmCode) {
                    case FM_NET_ERROR_ORDER_DATA_ERROR:
                        ToastUtils.showShort(R.string.fm_net_error_order_data_error);
                        break;
                    case FM_NET_ERROR_ORDER_STATUS_CHANGED:
                        ToastUtils.showShort(R.string.fm_net_error_order_status_changed);
                        break;
                    case FM_NET_ERROR_INVENTORY_ERROR:
                        ToastUtils.showShort(R.string.fm_net_error_inventory_material_shortage);
                        break;
                    default:
                        ToastUtils.showShort(R.string.arch_operate_fail);
                        break;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

//        Headers headers = response.headers();
//        if (headers != null) {
//            String errorMsg = headers.get("WWW-Authenticate");
//            if (!TextUtils.isEmpty(errorMsg)) {
            int code = response.getRawResponse().code();
            if (code == ErrorCode.HTTP_401_NO_AUTHORIZED){
                //需要重新登录
                Call rawCall = response.getRawCall();
                Request request = rawCall.request();
                GSON_REQUEST_MAP.put(request, this);
                if (COUNT_TOKEN == 0) {
                    COUNT_TOKEN++;
                    this.solveToken(rawCall);
                } else {
                    GSON_REQUEST_LIST.add(request);
                }
//            }
        }
    }

    private void delError(int code) {
        try {
            final SupportActivity topActivity = (SupportActivity) ActivityUtils.getTopActivity();
            FMWarnDialogBuilder builder;
            switch (code) {
                case ErrorCode.SYSTEM_ERROR:
                    ToastUtils.showShort(R.string.code_error_system);
                    break;

                case ErrorCode.COMMON_ERROR_PARAM:
                    ToastUtils.showShort(R.string.login_error_param);
                    break;

                case ErrorCode.COMMON_ITEM_NOT_EXIST:
                    ToastUtils.showShort(R.string.code_error_item_not_exist);
                    break;

                case ErrorCode.SECURITY_ERROR_CAPTCHA:
                    ToastUtils.showShort(R.string.code_error_verification);
                    break;

                case ErrorCode.SECURITY_ERROR_LOGIN_CODE_OR_PWD:
                    ToastUtils.showShort(R.string.arch_login_username_passwd_error);
                    break;

                case ErrorCode.SECURITY_ERROR_PWD_COUNT_MAX:
                    ToastUtils.showShort(R.string.login_error_password_upper_limit);
                    break;

                case ErrorCode.SECURITY_ERROR_GENERATE_CAPTCHA:
                    ToastUtils.showShort(R.string.code_error_generate_verification);
                    break;
                case ErrorCode.SECURITY_LOGIN_SUCCESS:
                    ToastUtils.showShort(R.string.code_login_success);
                    break;
                case ErrorCode.SECURITY_LOGOUT_SUCCESS:
                    ToastUtils.showShort(R.string.code_logout_success);
                    break;
                case ErrorCode.SECURITY_PASSWORD_REGEX_FAIL:
                    ToastUtils.showShort(R.string.code_error_password_simple);
                    break;
                case ErrorCode.SECURITY_OLD_PASSWORD_ERROR:
                    ToastUtils.showShort(R.string.code_error_password_error);
                    break;
                case ErrorCode.SECURITY_NOT_LOGIN:
                    ToastUtils.showShort(R.string.code_error_password_not_login);
                    break;
                case ErrorCode.SECURITY_USER_NOT_EXIST:
                    ToastUtils.showShort(R.string.code_error_password_user_not_exist);
                    break;
                case ErrorCode.SECURITY_PASSWORD_CHANGE_SUCCESS:
                    ToastUtils.showShort(R.string.code_error_password_change_success);
                    break;
                case ErrorCode.SECURITY_FIRST_TIME_LOGIN:
                    builder = new FMWarnDialogBuilder(topActivity);
                    builder.setCancelVisiable(false);
                    builder.setTitle(R.string.arch_tips);
                    builder.setTip(R.string.code_error_password_first_login);
                    builder.setSure(R.string.login_change_password);
                    builder.setCancel(R.string.dialog_cancel_next_tip);
                    builder.addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, View view) {
                            dialog.dismiss();
                            try {
                                SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.HAVE_LOGON, false);
                                AppService appService = (AppService) Router.getInstance().getService(AppService.class.getSimpleName());
                                BaseFragment resetPasswordFragment = appService.getResetPasswordFragment();
                                SupportFragment topFragment = (SupportFragment) topActivity.getTopFragment();
                                topFragment.start(resetPasswordFragment, ISupportFragment.SINGLETASK);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.create(R.style.fmDefaultWarnDialog).show();
                    break;
                case ErrorCode.SECURITY_PASSWORD_EXPIRE:
                    builder = new FMWarnDialogBuilder(topActivity);
                    builder.setCancelVisiable(false);
                    builder.setTitle(R.string.arch_tips);
                    builder.setTip(R.string.login_error_password_expired);
                    builder.setSure(R.string.login_change_password);
                    builder.setCancelVisiable(false);
                    builder.addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, View view) {
                            dialog.dismiss();
                            try {
                                SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.HAVE_LOGON, false);
                                AppService appService = (AppService) Router.getInstance().getService(AppService.class.getSimpleName());
                                BaseFragment resetPasswordFragment = appService.getResetPasswordFragment();
                                SupportFragment topFragment = (SupportFragment) topActivity.getTopFragment();
                                topFragment.start(resetPasswordFragment, ISupportFragment.SINGLETASK);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.create(R.style.fmDefaultWarnDialog).show();
                    break;
                case ErrorCode.SECURITY_PASSWORD_USED_IN_ONE_PERIOD:
                    ToastUtils.showShort(R.string.code_error_password_repeat);
                    break;

                case ErrorCode.SECURITY_IP_BLOCKED:
                    ToastUtils.showShort(R.string.code_error_password_ip_locked);
                    break;
                case ErrorCode.LOGON_OTHER:
                    builder = new FMWarnDialogBuilder(topActivity);
                    builder.setCancelVisiable(false);
                    builder.setTitle(R.string.arch_tips);
                    builder.setTip(R.string.logon_other_tip);
                    builder.setSure(R.string.logon_be_again);
                    builder.setCancelVisiable(false);
                    builder.addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, View view) {
                            dialog.dismiss();
                            try {
                                SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.HAVE_LOGON, false);
                                AppService appService = (AppService) Router.getInstance().getService(AppService.class.getSimpleName());
                                BaseFragment logonFragment = appService.getLogonFragment();
                                SupportFragment topFragment = (SupportFragment) topActivity.getTopFragment();
                                topFragment.start(logonFragment, ISupportFragment.SINGLETASK);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.create(R.style.fmDefaultWarnDialog).show();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int COUNT_TOKEN = 0;
    private final static List<Request> GSON_REQUEST_LIST = new ArrayList<>();
    private final static Map<Request, FMJsonCallback> GSON_REQUEST_MAP = new HashMap<>();

    private void solveToken(final Call rawCall) {
        String username = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.USERNAME);
        String password = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.PASSWORD);
        LogonManager.getInstance().logon(username, password, new JsonCallback<BaseResponse<LogonResponse>>() {
            @Override
            public void onSuccess(Response<BaseResponse<LogonResponse>> response) {
                LogonManager.getInstance().saveToken(response.body().data);
                //重新发送原始请求
                Request request = rawCall.request();
                GSON_REQUEST_LIST.add(0, request);
                for (Request request1 : GSON_REQUEST_LIST) {
                    try {
                        FMJsonCallback fmJsonCallback = GSON_REQUEST_MAP.get(request1);
                        if (fmJsonCallback != null) {
                            OkGo.<T>post(request1.url().toString())
                                    .isSpliceUrl(true)
                                    .upRequestBody(request1.body())
                                    .tag(request1.tag())
                                    .execute(fmJsonCallback);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                GSON_REQUEST_MAP.clear();
                GSON_REQUEST_LIST.clear();
                COUNT_TOKEN = 0;
            }

            @Override
            public void onError(Response<BaseResponse<LogonResponse>> response) {
                super.onError(response);
                //重新登录失败跳转到登录页面
                try {
                    //清除所有请求
                    OkGo.cancelAll(OkGo.getInstance().getOkHttpClient());
                    ToastUtils.showShort(R.string.arch_logon_session_failed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
