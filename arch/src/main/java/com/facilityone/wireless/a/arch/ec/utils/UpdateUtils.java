package com.facilityone.wireless.a.arch.ec.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.facilityone.wireless.a.arch.BuildConfig;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.module.CommonUrl;
import com.facilityone.wireless.a.arch.ec.module.UpdateService;
import com.facilityone.wireless.a.arch.ec.ui.UpdateProgressBar;
import com.facilityone.wireless.a.arch.ec.update.AppUpdateFragment;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.app.FMChannel;
import com.facilityone.wireless.basiclib.utils.GsonUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:检查更新
 * Date: 2019/1/2 10:27 AM
 */
public class UpdateUtils {

    private static final Integer DOWNLOAD_WAY_BROWSER = 1;

    public static void updateCheck(final BaseFragment fragment, final OnUpdateListener onUpdateListener) {
        Map<Object, Object> params = new HashMap<>();
        params.put("appKey", FM.getConfiguration(FMChannel.CHANNEL_UPDATE_APP_KEY));
        params.put("channelCode", FM.getConfiguration(FMChannel.CHANNEL_UPDATE_CHANNEL));
        params.put("currentVersion", AppUtils.getAppVersionCode());

        String jsonParams = GsonUtils.toJson(params);
        System.out.println("update:" + jsonParams);
        OkGo.<BaseResponse<UpdateService.UpdateInfoBean>>post(BuildConfig.SERVER_UPDATE_URL + CommonUrl.UPDATE_CHECK)
                .isSpliceUrl(true)
                .tag(fragment)
                .upJson(jsonParams)
                .execute(new FMJsonCallback<BaseResponse<UpdateService.UpdateInfoBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<UpdateService.UpdateInfoBean>> response) {
                        UpdateService.UpdateInfoBean data = response.body().data;
                        if (data != null) {
                            System.out.println("update:" + data.toString());
                            Integer code = data.code;
                            Long fileId = data.fileId;
                            String downloadUrl = data.downloadUrl;
                            String fileName = data.fileName;
                            Integer suggestUpdateType = data.suggestUpdateType;
                            String log = data.log;
                            boolean update = check(code, fileId, downloadUrl, fileName, suggestUpdateType);
                            if (update) {
                                showTipDialog(fragment, suggestUpdateType, downloadUrl, fileName, log);
                            } else {
                                if (onUpdateListener != null) {
                                    onUpdateListener.noneUpdate();
                                }
                            }

                            //更新服务器地址
                            boolean userEditServe = SPUtils.getInstance(SPKey.SP_MODEL).getBoolean(SPKey.USER_EDIT_SERVER, false);
                            if (!userEditServe && !TextUtils.isEmpty(data.serverAddress)) {
                                //用户没有设置过并且返回需要修改的地址不为空
                                SPUtils.getInstance(SPKey.SP_MODEL).put(SPKey.APP_SERVER, data.serverAddress);
                                FM.getConfigurator().withApiHost(data.serverAddress);
                            }

                        } else {
                            if (onUpdateListener != null) {
                                onUpdateListener.noneUpdate();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<UpdateService.UpdateInfoBean>> response) {
                        super.onError(response);
                        if (onUpdateListener != null) {
                            onUpdateListener.noneUpdate();
                        }
                    }
                });
    }

    private static boolean check(Integer code,
                                 Long fileId,
                                 String downloadUrl,
                                 String fileName,
                                 Integer suggestUpdateType) {
        if (code == null) {
            return false;
        }

        if (code <= AppUtils.getAppVersionCode()) {
            return false;
        }

        if (fileId == null) {
            return false;
        }

        if (TextUtils.isEmpty(fileName)) {
            return false;
        }

        if (suggestUpdateType == null) {
            return false;
        }

        if (suggestUpdateType.equals(DOWNLOAD_WAY_BROWSER) && TextUtils.isEmpty(downloadUrl)) {
            return false;
        }

        return true;
    }

    private static void showTipDialog(final BaseFragment fragment
            , final Integer suggestUpdateType
            , final String downloadUrl, String fileName, String log) {
        Context context = fragment.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.arch_update_tip, null);
        final Button update = (Button) view.findViewById(R.id.update_sure_btn);
        final TextView mod_ip_title_tv = (TextView) view.findViewById(R.id.mod_ip_title_tv);
        final LinearLayout ll_btn_update = (LinearLayout) view.findViewById(R.id.ll_btn_update);
        final LinearLayout ll_downloading = (LinearLayout) view.findViewById(R.id.ll_downloading);
        final UpdateProgressBar progressBar = (UpdateProgressBar) view.findViewById(R.id.progressBar);
        TextView logTv = (TextView) view.findViewById(R.id.update_log_tv);
        TextView contentTv = (TextView) view.findViewById(R.id.update_content_et);

        if (!TextUtils.isEmpty(log)) {
            logTv.setText(StringUtils.formatString(log));
            logTv.setVisibility(View.VISIBLE);
            contentTv.setVisibility(View.GONE);
        } else {
            logTv.setVisibility(View.GONE);
            contentTv.setVisibility(View.VISIBLE);
        }

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        final String webViewUrl = BuildConfig.SERVER_UPDATE_URL + downloadUrl;
        final String downloadAppUrl = BuildConfig.SERVER_UPDATE_URL + CommonUrl.UPDATE_DOWNLOAD_URL + fileName;

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (suggestUpdateType.equals(DOWNLOAD_WAY_BROWSER)) {
                    //浏览器下载
                    alertDialog.dismiss();
                    fragment.start(AppUpdateFragment.getInstance(webViewUrl));
                } else {
                    //app内下载
                    ll_btn_update.setVisibility(View.GONE);
                    mod_ip_title_tv.setText(R.string.arch_soft_updating);
                    ll_downloading.setVisibility(View.VISIBLE);
                    download(fragment, progressBar, downloadAppUrl);

                }
            }
        });
    }

    private static void download(BaseFragment fragment, final UpdateProgressBar progressBar, String downloadAppUrl) {
        OkGo.<File>get(downloadAppUrl)
                .tag(fragment)
                .execute(new FileCallback() {
                    @Override
                    public void onSuccess(Response<File> response) {
                        File body = response.body();
                        if (body.exists()) {
                            AppUtils.installApp(body);
                        }
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        int p = (int) (progress.fraction * 100);
                        progressBar.setProgress(p);
                    }
                });
    }

    public interface OnUpdateListener {
        void onUpdate();

        void noneUpdate();
    }
}
