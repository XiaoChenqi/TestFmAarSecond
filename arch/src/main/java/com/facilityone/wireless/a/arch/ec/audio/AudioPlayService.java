package com.facilityone.wireless.a.arch.ec.audio;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.a.arch.R;

import java.io.IOException;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:音频播放
 * Date: 2018/6/28 上午11:40
 */
public class AudioPlayService extends Service implements MediaPlayer.OnCompletionListener {
    private static final String TAG = "Service";

    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSE = 3;

    private MediaPlayer mPlayer = new MediaPlayer();

    private int mPlayState = STATE_IDLE;

    private String playUrl = "";


    ImageView voiceIconView;
    private AnimationDrawable voiceAnimation = null;
    public static boolean isPlaying = false;
    private int mPosition;


    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer.setOnCompletionListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("url")) {
            playUrl = intent.getStringExtra("url");
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        LogUtils.d("onCompletion-->", "onCompletion 100%");
        stopPlayVoiceAnimation();
        if (mOnMediaCompletionListener != null) {
            mOnMediaCompletionListener.onMediaCompletion(mp);
        }
        quit();
    }

    public void play(String url) {
        if (isPlaying) {
            if (playUrl != null) {
                stopPlayVoiceAnimation();
            }
        }
        try {
            mPlayer.reset();
            mPlayer.setDataSource(url);
            mPlayer.prepareAsync();
            mPlayState = STATE_PREPARING;
            mPlayer.setOnPreparedListener(mPreparedListener);
            mPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            isPlaying = true;
            playUrl = url;
            showAnimation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playNoAnimation(String url) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(url);
            mPlayer.prepareAsync();
            mPlayState = STATE_PREPARING;
            mPlayer.setOnPreparedListener(mPreparedListener);
            mPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            isPlaying = true;
            playUrl = url;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stopPlayVoiceAnimation() {
        if (voiceAnimation != null && voiceAnimation.isRunning()) {
            voiceAnimation.stop();
            voiceIconView.setImageResource(R.drawable.audio_play_03);
        }
        isPlaying = false;
        playUrl = null;
    }

    public void stopPlayVoiceAnimation2() {
        if (voiceAnimation != null) {
            voiceAnimation.stop();
            voiceIconView.setImageResource(R.drawable.audio_play_03);
        }
        if (isPlaying) {
            stopPlaying();
        }
        isPlaying = false;
        playUrl = null;
    }


    // show the voice playing animation
    private void showAnimation() {
        // play voice, and start animation
        voiceIconView.setImageResource(R.drawable.voice_to_icon);
        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
        voiceAnimation.start();
    }

    public void setImageView(ImageView imageView) {
        stopPlayVoiceAnimation();
        this.voiceIconView = imageView;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public int getPosition() {
        return this.mPosition;
    }


    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (isPreparing()) {
                start();
            }
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Log.e("onBufferingUpdate--->", percent + "%");
        }
    };


    void start() {
        if (!isPreparing() && !isPausing()) {
            return;
        }

        mPlayer.start();
        mPlayState = STATE_PLAYING;


    }

    public void stopPlaying() {
        // stop play voice
        if (mPlayer != null) {
            mPlayer.stop();
        }
    }

    public void stopPlayingNoAnimation() {
        // stop play voice
        if (mPlayer != null) {
            mPlayer.stop();
        }
        isPlaying = false;
        playUrl = null;
    }


    public void stop() {
        if (isIdle()) {
            return;
        }

        mPlayer.reset();
        mPlayState = STATE_IDLE;
    }


    public boolean isPlaying() {
        return mPlayState == STATE_PLAYING;
    }

    public boolean isPausing() {
        return mPlayState == STATE_PAUSE;
    }

    public boolean isPreparing() {
        return mPlayState == STATE_PREPARING;
    }

    public boolean isIdle() {
        return mPlayState == STATE_IDLE;
    }


    @Override
    public void onDestroy() {
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        voiceIconView = null;
        super.onDestroy();
        Log.i(TAG, "onDestroy: " + getClass().getSimpleName());
    }

    public void quit() {
        stop();
        stopSelf();
    }

    public class PlayBinder extends Binder {
        public AudioPlayService getService() {
            return AudioPlayService.this;
        }
    }

    public void setOnMediaCompletionListener(OnMediaCompletionListener onMediaCompletionListener) {
        mOnMediaCompletionListener = onMediaCompletionListener;
    }

    private OnMediaCompletionListener mOnMediaCompletionListener;

    public interface OnMediaCompletionListener {
        void onMediaCompletion(MediaPlayer mp);
    }
}