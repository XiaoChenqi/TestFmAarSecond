package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.ec.audio.AudioPlayManager;
import com.facilityone.wireless.a.arch.ec.audio.AudioPlayService;
import com.facilityone.wireless.a.arch.ec.audio.AudioRecordManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.widget.FMBottomSheet;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:底部录音
 * Date: 2018/6/26 下午7:36
 */
public class FMBottomAudioSheetBuilder implements View.OnClickListener, DialogInterface.OnCancelListener {

    private Context mContext;
    private FMBottomSheet mAudioDialog;
    private LineWaveVoiceView mLineView;
    private ImageButton mButton;
    private TextView mTip;
    private TextView mCancel;
    private TextView mSave;
    private LinearLayout mMenu;
    private View mViewLine;
    private CircleProgressView mProgressView;

    private static final int STATUS_READY = 0;//就绪状态
    private static final int STATUS_RECORDING = 1;//录制状态
    private static final int STATUS_STOP = 2;//暂停状态
    private static final int STATUS_PLAY = 3;//播放状态

    public static final long DEFAULT_MAX_RECORD_TIME = 600000;
    public static final long DEFAULT_MIN_RECORD_TIME = 2000;
    protected static final int DEFAULT_MIN_TIME_UPDATE_TIME = 1100;

    private int status;
    private Timer timer;
    private TimerTask timerTask;
    private long recordTotalTime;
    private String audioFileName;


    private long maxRecordTime = DEFAULT_MAX_RECORD_TIME;
    private long minRecordTime = DEFAULT_MIN_RECORD_TIME;
    private final AudioRecordManager audioRecordManager;
    private final MyRunnable mMyRunnable;
    private int mMax;
    private int mTempTotal;

    public FMBottomAudioSheetBuilder(Context context) {
        mContext = context;
        audioRecordManager = AudioRecordManager.getInstance();
        mMyRunnable = new MyRunnable();
    }

    public FMBottomAudioSheetBuilder setMaxRecordTime(long maxRecordTime) {
        this.maxRecordTime = maxRecordTime;
        return this;
    }

    public FMBottomSheet build() {
        mAudioDialog = new FMBottomSheet(mContext);
        View contentView = buildViews();
        mAudioDialog.setContentView(contentView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mAudioDialog.setOnCancelListener(this);
        return mAudioDialog;
    }

    private View buildViews() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(getItemViewLayoutId(), null, false);

        mLineView = (LineWaveVoiceView) itemView.findViewById(R.id.line);
        mButton = (ImageButton) itemView.findViewById(R.id.iv_start);
        mTip = (TextView) itemView.findViewById(R.id.tv_tip);
        mCancel = (TextView) itemView.findViewById(R.id.tv_cancel);
        mSave = (TextView) itemView.findViewById(R.id.tv_save);
        mMenu = (LinearLayout) itemView.findViewById(R.id.ll_menu);
        mViewLine = itemView.findViewById(R.id.view_line);
        mTip = (TextView) itemView.findViewById(R.id.tv_tip);
        mProgressView = (CircleProgressView) itemView.findViewById(R.id.cpv_play);

        mButton.setOnClickListener(this);
        mProgressView.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mSave.setOnClickListener(this);
        status = STATUS_READY;
        return itemView;
    }

    private int getItemViewLayoutId() {
        return R.layout.fm_bottom_sheet_audio_item;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_cancel) {
            mAudioDialog.cancel();
            return;
        }

        if (v.getId() == R.id.tv_save) {
            mAudioDialog.dismiss();
            if (mOnAudioFinishListener != null) {
                LocalMedia localMedia = new LocalMedia();
                localMedia.setPath(audioFileName);
                localMedia.setPictureType("audio/mpeg");
                localMedia.setMimeType(PictureMimeType.ofAudio());
                localMedia.setDuration(PictureMimeType.getLocalVideoDuration(audioFileName));
                mOnAudioFinishListener.onSave(localMedia);
            }
            return;
        }

        mMenu.setVisibility(View.INVISIBLE);
        mViewLine.setVisibility(View.INVISIBLE);

        int bgId = R.drawable.record_audio_start_selector;
        switch (status) {
            case STATUS_READY:
                status = STATUS_RECORDING;
                bgId = R.drawable.record_audio_recording_selector;
                mTip.setVisibility(View.GONE);
                mLineView.setVisibility(View.VISIBLE);
                startRecordAudio();
                break;
            case STATUS_RECORDING:
                status = STATUS_STOP;
                bgId = R.drawable.icon_record_audio_stop;
                stopRecordAudio();
                break;
            case STATUS_STOP:
                status = STATUS_PLAY;
                bgId = R.drawable.icon_record_audio_play;
                mAudioDialog.setCanceledOnTouchOutside(false);
                startPlayAudio();
                break;
            case STATUS_PLAY:
                if (AudioPlayManager.getAudioPlayService() != null) {
                    AudioPlayManager.getAudioPlayService().setOnMediaCompletionListener(null);
                    AudioPlayManager.getAudioPlayService().stopPlayingNoAnimation();
                }
                status = STATUS_STOP;
                bgId = R.drawable.icon_record_audio_stop;
                stopPlayAudio();
                break;
        }
        mButton.setBackgroundDrawable(mContext.getResources().getDrawable(bgId));
    }

    private void startPlayAudio() {
        mButton.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
        mMax = (int) recordTotalTime;
        mTempTotal = 0;
        mProgressView.setMax(mMax);

        final AudioPlayService audioPlayService = AudioPlayManager.getAudioPlayService();
        if (audioPlayService != null) {
            audioPlayService.setOnMediaCompletionListener(new AudioPlayService.OnMediaCompletionListener() {
                @Override
                public void onMediaCompletion(MediaPlayer mp) {
                    status = STATUS_STOP;
                    stopPlayAudio();
                    mButton.setBackgroundDrawable(mContext.getResources()
                            .getDrawable(R.drawable.icon_record_audio_stop));
                    audioPlayService.setOnMediaCompletionListener(null);
                }
            });
            audioPlayService.playNoAnimation(audioFileName);
            FM.getHandler().post(mMyRunnable);
        }
    }

    private void startRecordAudio() {
        recordTotalTime = 0;

        RecordManager.getInstance().setRecordResultListener(new RecordResultListener() {
            @Override
            public void onResult(File result) {
                audioFileName = result.getAbsolutePath();
            }
        });

        mLineView.startRecord();
        audioRecordManager.startRecord();
        initTimer();
        timer.schedule(timerTask, 0, DEFAULT_MIN_TIME_UPDATE_TIME);
    }

    private void stopRecordAudio() {
        closeAll();
        mMenu.setVisibility(View.VISIBLE);
        mTip.setText(DateUtils.formatRecordTime((int) recordTotalTime));
        mTip.setVisibility(View.VISIBLE);
        mLineView.setVisibility(View.INVISIBLE);
        mViewLine.setVisibility(View.VISIBLE);
        mButton.setBackgroundDrawable(mContext.getResources()
                .getDrawable(R.drawable.icon_record_audio_stop));
    }

    private void stopPlayAudio() {
        FM.getHandler().removeCallbacks(mMyRunnable);
        mButton.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.GONE);
        mProgressView.setProgress(0);
        mAudioDialog.setCanceledOnTouchOutside(true);
        mViewLine.setVisibility(View.VISIBLE);
        mMenu.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化计时器用来更新倒计时
     */
    private void initTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                FM.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        //每隔1000毫秒更新一次ui
                        recordTotalTime += 1000;
                        updateTimerUI();
                    }
                });
            }
        };
    }

    private void updateTimerUI() {
        if (recordTotalTime >= maxRecordTime) {
            stopRecordAudio();
        } else {
            String string = DateUtils.formatRecordTime((int) recordTotalTime);
            mLineView.setText(string);
        }
    }


    private String createAudioName() {
        return System.currentTimeMillis() + ".amr";
    }

    public void closeAll() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        FM.getHandler().removeCallbacks(mMyRunnable);
        mLineView.stopRecord();
        audioRecordManager.stopRecord();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        closeAll();
        FileUtils.deleteFile(audioFileName);
    }


    public void setOnAudioFinishListener(OnAudioFinishListener onAudioFinishListener) {
        mOnAudioFinishListener = onAudioFinishListener;
    }

    private OnAudioFinishListener mOnAudioFinishListener;


    public interface OnAudioFinishListener {
        void onSave(LocalMedia media);
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            if (mTempTotal > mMax) {
                FM.getHandler().removeCallbacks(this);
                return;
            }
            if (mProgressView != null && mTempTotal > 0) {
                mProgressView.setProgress(mTempTotal);
            }

            if (mTip != null) {
                mTip.setText(DateUtils.formatRecordTime(mTempTotal));
            }
            mTempTotal += 1000;
            FM.getHandler().postDelayed(this, 1000);
        }
    }
}
