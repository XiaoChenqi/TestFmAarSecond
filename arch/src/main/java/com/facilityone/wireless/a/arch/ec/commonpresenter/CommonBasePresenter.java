package com.facilityone.wireless.a.arch.ec.commonpresenter;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.logon.LogonManager;
import com.facilityone.wireless.a.arch.ec.logon.UserUrl;
import com.facilityone.wireless.a.arch.ec.module.BulletinService;
import com.facilityone.wireless.a.arch.ec.module.CommonUrl;
import com.facilityone.wireless.a.arch.ec.module.ErrorCode;
import com.facilityone.wireless.a.arch.ec.module.FunctionService;
import com.facilityone.wireless.a.arch.ec.module.LogonResponse;
import com.facilityone.wireless.a.arch.ec.module.Page;
import com.facilityone.wireless.a.arch.ec.module.UserService;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.facilityone.wireless.a.arch.utils.FMFileUtils;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.PinyinUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.componentservice.app.AppService;
import com.fm.tool.network.callback.JsonCallback;
import com.fm.tool.network.model.BaseResponse;
import com.luck.picture.lib.entity.LocalMedia;
import com.luojilab.component.componentlib.router.Router;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import me.yokeyword.fragmentation.ISupportFragment;
import okhttp3.Cookie;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:公共被多个组件调用
 * Date: 2018/6/8 上午9:30
 */
public class CommonBasePresenter<V extends BaseFragment> extends BasePresenter<V> {

    /****************登录***************/
    public void logon(final String username, final String password) {
        //....
        LogonManager.getInstance().logon(username, password, new JsonCallback<BaseResponse<LogonResponse>>() {

            @Override
            public void onStart(Request<BaseResponse<LogonResponse>, ? extends Request> request) {
                getV().showLoading();
            }

            @Override
            public void onSuccess(Response<BaseResponse<LogonResponse>> response) {
                BaseResponse<LogonResponse> body = response.body();
                if(body != null) {
                    int fmcode = body.fmcode;
                    int code = body.code;
                    if (code != 0 && code != 200) {
                        response.setException(new HttpException(code + ""));
                        onError(response);
                        return;
                    }
                    if (fmcode != 0) {
                        response.setException(new IllegalStateException(fmcode + ""));
                        onError(response);
                        return;
                    }

                    if (body.data == null) {
                        ToastUtils.showShort(R.string.arch_login_error);
                        return;
                    }
                    LogonManager.getInstance().saveToken(body.data);
                    SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.USERNAME, username);
                    SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.PASSWORD, password);
                    ToastUtils.showShort(R.string.arch_login_success);
                    onLogonSuccess();
                }
            }

            @Override
            public void onFinish() {
                getV().dismissLoading();
            }

            @Override
            public void onError(Response<BaseResponse<LogonResponse>> response) {
                super.onError(response);
                solveLogonError(response);
                onLogonError();
            }
        });
    }

    public void onLogonSuccess() {
    }

    public void onLogonError() {
    }

    private void solveLogonError(Response<BaseResponse<LogonResponse>> response) {
        try {
            BaseResponse<LogonResponse> body = response.body();
            if (body != null) {
                int code = body.code;

                FMWarnDialogBuilder builder;
                switch (code) {
                    case ErrorCode.HTTP_401_NO_AUTHORIZED:
                        ToastUtils.showShort("未登陆");
                        break;
                    case ErrorCode.HTTP_403_FORBIDDEN:
                        ToastUtils.showShort("无权限");
                        break;
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
                        builder = new FMWarnDialogBuilder(getV().getContext());
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
                                    getV().start(resetPasswordFragment, ISupportFragment.SINGLETASK);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.create(R.style.fmDefaultWarnDialog).show();
                        break;
                    case ErrorCode.SECURITY_PASSWORD_EXPIRE:
                        builder = new FMWarnDialogBuilder(getV().getContext());
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
                                    getV().start(resetPasswordFragment, ISupportFragment.SINGLETASK);
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
                }
            }else {
                ToastUtils.showShort("登录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort("登录失败");
        }


    }


    /*********************公告相关****************************/

    public void getBulletinList(int type, Page page, final boolean refresh) {
        BulletinService.BulletinReq req = new BulletinService.BulletinReq();
        req.type = type;
        req.page = page;
        OkGo.<BaseResponse<BulletinService.BulletinResp>>post(FM.getApiHost() + CommonUrl.BULLETIN_LIST_URL)
                .isSpliceUrl(true)
                .tag(getV())
                .upJson(toJson(req))
                .execute(new FMJsonCallback<BaseResponse<BulletinService.BulletinResp>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<BulletinService.BulletinResp>> response) {
                        BulletinService.BulletinResp data = response.body().data;
                        getBulletinListSuccess(data, refresh);
                    }

                    @Override
                    public void onError(Response<BaseResponse<BulletinService.BulletinResp>> response) {
                        super.onError(response);
                        getBulletinListError(response);
                    }

                    @Override
                    public void onFinish() {
                        getBulletinListFinish();
                    }
                });
    }

    public void getBulletinListSuccess(BulletinService.BulletinResp data, boolean refresh) {
    }

    public void getBulletinListError(Response<BaseResponse<BulletinService.BulletinResp>> response) {
    }

    public void getBulletinListFinish() {
    }

    /*********************未读个数****************************/

    public void getUndoNumber(int type) {
        String typeJson = "{\"type\":" + type + "}";

        String url = FM.getApiHost() + CommonUrl.COMMON_UNDO_URL;
        String url2 = url+"?app_type=android&app_version=0.0.0&current_project=10&i18n=zh_CN";


        OkGo.<BaseResponse<FunctionService.FunctionUndoBean>>post(url2)
                .isSpliceUrl(true)
                .upJson(typeJson)
                .tag(getV())
                .execute(new FMJsonCallback<BaseResponse<FunctionService.FunctionUndoBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<FunctionService.FunctionUndoBean>> response) {
                        FunctionService.FunctionUndoBean data = response.body().data;
                        if (data == null) {
                            return;
                        }
                        String json = toJson(data);
                        if (TextUtils.isEmpty(json)) {
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            getUndoNumberSuccess(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    public void getUndoNumberSuccess(JSONObject data) {
    }

    /*********************权限****************************/

    public void getPermissions() {
        String permissionUrl = FM.getApiHost() + CommonUrl.COMMON_PERMISSIONS_URL;
        OkGo.<BaseResponse<List<String>>>post(FM.getApiHost() + CommonUrl.COMMON_PERMISSIONS_URL)
                .isSpliceUrl(true)
                .upJson("{}")
                .tag(getV())
                .execute(new FMJsonCallback<BaseResponse<List<String>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<String>>> response) {
                        List<String> data = response.body().data;
                        if (data != null && data.size() > 0) {
                            String s = toJson(data);
                            SPUtils.getInstance(SPKey.SP_MODEL_PERMISSIONS)
                                    .put(SPKey.FUNCTION_PERMISSIONS, s);
                            getPermissionsSuccess(s);
                        } else {
                            getPermissionsSuccess(null);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<List<String>>> response) {
                        super.onError(response);
                        getPermissionsError(response);
                    }
                });
    }

    public void getPermissionsSuccess(String data) {
    }

    public void getPermissionsError(Response<BaseResponse<List<String>>> response) {
    }

    /*********************用户信息****************************/

    public void getUserInfo() {
        OkGo.<BaseResponse<UserService.UserInfoBean>>post(FM.getApiHost() + UserUrl.USER_INFO_URL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson("{}")
                .execute(new FMJsonCallback<BaseResponse<UserService.UserInfoBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<UserService.UserInfoBean>> response) {
                        UserService.UserInfoBean data = response.body().data;
                        if (data != null) {
                            FM.getConfigurator().withEmId(data.emId);
                            FM.getConfigurator().withEmName(data.name);
                            if (data.emId != null) {
                                SPUtils.getInstance(SPKey.SP_MODEL_USER).put(SPKey.EM_ID, data.emId);
                            }
                            SPUtils.getInstance(SPKey.SP_MODEL_USER).put(SPKey.EM_NAME, StringUtils.formatString(data.name));
                            String toJson = toJson(data);
                            SPUtils.getInstance(SPKey.SP_MODEL_USER).put(SPKey.USER_INFO, toJson);
                            getUserInfoSuccess(toJson);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<UserService.UserInfoBean>> response) {
                        super.onError(response);
                        String userInfo = SPUtils.getInstance(SPKey.SP_MODEL_USER).getString(SPKey.USER_INFO);
                        getUserInfoSuccess(userInfo);
                    }
                });
    }

    public void getUserInfoSuccess(String toJson) {
    }

    /*********************上传文件****************************/

    public void uploadFile(List<LocalMedia> paths) {
        this.uploadFile(paths, -1);
    }


    public void uploadFile(List<LocalMedia> paths, final int type) {
        this.uploadFile(paths, null, type);
    }

    public void uploadFile(final List<LocalMedia> paths, final String url, final int type) {

        final String userName = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.USERNAME);
        final String password = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.PASSWORD);
        LogonManager.getInstance().logon(userName, password, new JsonCallback<BaseResponse<LogonResponse>>() {

            @Override
            public void onStart(Request<BaseResponse<LogonResponse>, ? extends Request> request) {
                getV().showLoading();
            }

            @Override
            public void onSuccess(Response<BaseResponse<LogonResponse>> response) {
                BaseResponse<LogonResponse> body = response.body();
                if (body.data != null) {
                    LogonManager.getInstance().saveToken(body.data);
                    SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.USERNAME, userName);
                    SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.PASSWORD, password);
                }

                uploadFiles(paths, url, type);
            }


            @Override
            public void onError(Response<BaseResponse<LogonResponse>> response) {
                super.onError(response);
                uploadFiles(paths, url, type);
            }
        });
    }


    private void uploadFiles(List<LocalMedia> paths, String url, final int type) {
        if (paths == null || paths.size() == 0) {
            return;
        }

        getV().showLoading();

        if (url == null) {
            url = CommonUrl.UPLOAD_IMAGE_URL;
        }

        PostRequest<BaseResponse<List<String>>> request = OkGo.<BaseResponse<List<String>>>post(FM.getApiHost() + url)
                .tag(getV())
                .isSpliceUrl(true);

        for (LocalMedia item : paths) {
            String path = "";
            if (item.isCut() && !item.isCompressed()) {
                path = item.getCutPath();
            } else if (item.isCompressed() || (item.isCut() && item.isCompressed())) {
                path = item.getCompressPath();
            } else {
                path = item.getPath();
            }
            if (!"".equals(path)) {
                File file = new File(path);
                if (file.exists()) {
                    request.params("file-" + file.getName(), file);
                }
            }

        }

        request.getParams().urlParamsMap.clear();
        request.execute(new FMJsonCallback<BaseResponse<List<String>>>() {
            @Override
            public void onSuccess(Response<BaseResponse<List<String>>> response) {
                uploadFileSuccess(response.body().data, type);
            }

            @Override
            public void onError(Response<BaseResponse<List<String>>> response) {
                super.onError(response);
                uploadFileError(response, type);
            }

            @Override
            public void onFinish() {
                uploadFileFinish(type);
            }
        });
    }


    public void uploadFileSuccess(List<String> ids, int type) {
    }

    public void uploadFileError(Response<BaseResponse<List<String>>> response, int type) {

    }

    public void uploadFileFinish(int type) {
    }

    /*********************下载附近并打开****************************/

    public void openAttachment(String url, String fileName, final Context context) {
        String cachePath = FMFileUtils.getAttachmentPath();
        String newName = System.currentTimeMillis() + "_" + fileName;
        getV().showLoading();
        OkGo.<File>get(url)
                .tag(this)
                .execute(new FileCallback(cachePath, newName) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        FMFileUtils.openFile(response.body(), context);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getV().dismissLoading();
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        ToastUtils.showShort(R.string.arch_attachment_error);
                    }
                });
    }

    public void clearAttachment() {
        String cachePath = FMFileUtils.getAttachmentPath();
        com.blankj.utilcode.util.FileUtils.deleteAllInDir(cachePath);
    }


    /**
     * 将拼音按照"空格"转换成字符串数组
     */
    public String[] pinyinToStrArr(String pinyin) {
        if (TextUtils.isEmpty(pinyin)) {
            return null;
        }
        pinyin = pinyin.toLowerCase();
        return pinyin.split(PinyinUtils.PINYIN_SPLIT);
    }

    /**
     * 判断字符串是否符合指定过滤条件
     * 1.模糊匹配  2.拼音首字母匹配 3.全拼匹配 4.忽略大小写
     *
     * @param strArr
     * @param key
     * @return 返回匹配到的开始位置的下标
     */
    public int isMatch(String[] strArr, String key) {
        int startIndex = -1;
        if (strArr == null) {
            return startIndex;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = strArr.length - 1; i >= 0; i--) {
            stringBuilder.insert(0, strArr[i]);
            String str = stringBuilder.toString();
            if (str.startsWith(key)) {
                startIndex = i;
                break;
            }
        }
        return startIndex;
    }

    public int endIndex(String[] strArr, String key, int index) {
        String str = strArr[index];
        if (str.length() >= key.length() && str.startsWith(key)) {
            return index + 1;
        } else if (key.startsWith(str)) {
            String tempKey = key.substring(str.length());
            return endIndex(strArr, tempKey, index + 1);
        }
        return -1;
    }

    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.removeAllCookie();//移除
        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        List<Cookie> cookieList = cookieStore.getAllCookie();
        if (cookieList != null) {
            for (Cookie cookie : cookieList) {
                //Tips:有多个cookie的时候必须设置多次，否则webView只读第一个cookie
                cookieManager.setCookie(url, cookie.toString());
            }
        }

        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }

}
