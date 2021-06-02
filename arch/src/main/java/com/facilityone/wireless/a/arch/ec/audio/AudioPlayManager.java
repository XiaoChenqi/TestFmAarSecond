package com.facilityone.wireless.a.arch.ec.audio;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:管理播放类
 * Date: 2018/6/28 下午2:21
 */
public class AudioPlayManager {

    private AudioPlayService mAudioPlayService;

    private AudioPlayManager() {
    }

    private static class AudioPlayHolder {
        private static final AudioPlayManager INSTANCE = new AudioPlayManager();
    }

    private static AudioPlayManager getInstance() {
        return AudioPlayHolder.INSTANCE;
    }

    public static AudioPlayService getAudioPlayService() {
        return getInstance().mAudioPlayService;
    }

    public static void setAudioPlayService(AudioPlayService service) {
        getInstance().mAudioPlayService = service;
    }
}
