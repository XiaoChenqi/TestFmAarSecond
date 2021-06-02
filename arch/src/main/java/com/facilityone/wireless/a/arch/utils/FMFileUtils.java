package com.facilityone.wireless.a.arch.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.basiclib.app.FM;

import java.io.File;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:文件操作
 * Date: 2018/7/3 上午11:03
 */
public class FMFileUtils {

    /**
     * 打开文件
     *
     * @param file
     * @param context
     */
    public static void openFile(File file, Context context) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            String extension = FileUtils.getFileExtension(file);
            String mimeType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(extension);
            //设置intent的data和Type属性。
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uriForFile;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String packageName = context.getPackageName();
                uriForFile = FileProvider.getUriForFile(context, packageName + ".fileprovider", file);
            } else {
                uriForFile = Uri.fromFile(file);
            }
            intent.setDataAndType(/*uri*/uriForFile, mimeType);
            //跳转
            context.startActivity(intent);     //这里最好try一下，有可能会报错。 //比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(R.string.arch_file_error);
        }
    }

    //缓存文件地址
    public static String getPath(String path) {
        String p = FM.getCachePath() + path + "/";
        File file = new File(p);
        if (!file.exists()) {
            file.mkdirs();
        }
        return p;
    }

    /**
     * 崩溃日志存储地址
     *
     * @return
     */
    public static String getCrashPath() {
        return getPath("crash");
    }

    /**
     * 图片缓存地址
     *
     * @return
     */
    public static String getPicPath() {
        return getPath("pic");
    }

    /**
     * 附件
     *
     * @return
     */
    public static String getAttachmentPath() {
        return getPath("attachment");
    }

    /**
     * audio
     *
     * @return
     */
    public static String getAudioPath() {
        return getPath("audio");
    }

    /**
     * video
     *
     * @return
     */
    public static String getVideoPath() {
        return getPath("video");
    }
}
