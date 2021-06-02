package com.facilityone.wireless.a.arch.ec.audio;

import com.zlw.main.recorderlib.RecordManager;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:录制音频的控制器
 * Date: 2018/6/26 下午10:39
 */
public class AudioRecordManager {

    private volatile static AudioRecordManager INSTANCE;

    private AudioRecordManager() {
    }

    public static AudioRecordManager getInstance() {
        if (INSTANCE == null) {
            synchronized (AudioRecordManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AudioRecordManager();
                }
            }
        }
        return INSTANCE;
    }

    public void startRecord() {
        RecordManager.getInstance().start();
    }

    public void stopRecord() {
        RecordManager.getInstance().stop();
    }
}