package com.facilityone.wireless.a.arch.ec.collect;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.facilityone.wireless.a.arch.BuildConfig;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import java.io.File;

/**
 * Author：gary.xu
 * <p/>
 * Email: xuhaozv@163.com
 * <p/>
 * description:上传崩溃信息
 * <p/>
 * Date: 2017/7/25 11:39
 */
public class UploadCrashUtils {

    private static final String APP_FILE_URL = "/file/newFile";

    /**
     * 上传崩溃信息
     *
     * @param object
     */
    public static void uploadFile(Object object) {
        String crash = SPUtils.getInstance(SPKey.SP_MODEL_CRASH).getString(SPKey.CRASH_INFO);
        if (!TextUtils.isEmpty(crash)) {
            File file = new File(crash);
            if (file.exists()) {
                PostRequest<String> request = OkGo.<String>post(
                        BuildConfig.SERVER_UPDATE_URL + APP_FILE_URL)
                        .tag(object)
                        .isSpliceUrl(true);
                request.params("file-" + file.getName(), file);

                request.execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.d("上传崩溃信息成功");
                        if (response != null && response.body() != null && response.body().contains("code")) {
                            SPUtils.getInstance(SPKey.SP_MODEL_CRASH).clear();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtils.d("上传崩溃信息失败");
                    }
                });
            }
        }
    }
}
