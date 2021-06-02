package com.facilityone.wireless.a.arch.ec.audio;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:获取服务与控件通信
 * Date: 2018/6/28 下午2:26
 */
public class AudioPlayConnection implements ServiceConnection {

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        final AudioPlayService audioPlayService = ((AudioPlayService.PlayBinder) service).getService();
        LogUtils.d("--onServiceConnected--");
        AudioPlayManager.setAudioPlayService(audioPlayService);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
