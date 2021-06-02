package com.facilityone.wireless.a.arch.ec.collect;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.a.arch.utils.FMFileUtils;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.app.FMChannel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author：gary.xu
 * <p/>
 * Email: xuhaozv@163.com
 * <p/>
 * description:
 * <p/>
 * Date: 2017/7/20 18:49
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler instance;
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context.getApplicationContext();
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            //退出程序
            // 以下用来捕获程序崩溃异常            I
//            Intent intent = new Intent();
//            // 参数1：包名，参数2：程序入口的activity
//            String packageName = mContext.getPackageName();
//            intent.setClassName(packageName, "com.facilityone.wireless.a.business.others.WelcomeActivity");
//            PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0,
//                    intent, Intent.FLAG_ACTIVITY_NEW_TASK);
//            AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
//            BaseActivity.clearActivity();
//            BaseFragmentActivity.clearActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCatchInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        String versionName = AppUtils.getAppVersionName();
        String versionCode = AppUtils.getAppVersionCode() + "";
        String username = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.USERNAME, "");
        String projectName = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.PROJECT_NAME, "无项目名");
        infos.put("应用名", (String) FM.getConfiguration(FMChannel.CHANNEL_UPDATE_APP_KEY));
        infos.put("应用渠道", (String) FM.getConfiguration(FMChannel.CHANNEL_UPDATE_CHANNEL));
        infos.put("应用版本", versionName);
        infos.put("应用code", versionCode);
        infos.put("项目名", projectName);
        infos.put("项目id", FM.getProjectId() + "");
        infos.put("用户名", username);
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCatchInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        Map<String, String> stringMap = sortMapByKey(infos);
        if (stringMap != null) {
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "：" + value + "\n");
            }
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            Throwable c = ex.getCause();
            String message;
            if (c != null) {
                message = c.toString();
            } else {
                message = ex.toString();
            }
            String exceptionName = "未知";
            if (!TextUtils.isEmpty(message) && message.contains(":")) {
                int indexOf = message.indexOf(":");
                if (indexOf > 0) {
                    exceptionName = message.substring(0, indexOf);
                }
            } else if (!TextUtils.isEmpty(message)) {
                exceptionName = message;
            }

            String fileName = "crash-" + FM.getConfiguration(FMChannel.CHANNEL_UPDATE_CHANNEL) + "-" + exceptionName + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String crashPath = FMFileUtils.getCrashPath();
                String path = crashPath + "/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
                SPUtils.getInstance(SPKey.SP_MODEL_CRASH).put(SPKey.CRASH_INFO, path + fileName);
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    private Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }


    //比较器类
    private class MapKeyComparator implements Comparator<String> {
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }
}
