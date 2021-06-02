package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.facilityone.wireless.a.arch.R;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.listener.RecordSoundSizeListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:语音录制的动画效果
 * Date: 2018/6/26 下午10:42
 */
public class LineWaveVoiceView extends View {
    private static final String TAG = LineWaveVoiceView.class.getSimpleName();

    private Paint paint;
    //矩形波纹颜色
    private int lineColor;
    //矩形波纹宽度
    private float lineWidth;
    private float textSize;
    private static final String DEFAULT_TEXT = " 00:00 ";
    private String text = DEFAULT_TEXT;
    private int textColor;
    private boolean isStart = false;
    private Runnable mRunnable;

    private int LINE_W = 9;//默认矩形波纹的宽度，9像素, 原则上从layout的attr获得
    private int MIN_WAVE_H = 2;//最小的矩形线高，是线宽的2倍，线宽从lineWidth获得
    private int MAX_WAVE_H = 7;//最高波峰，是线宽的4倍

    //默认矩形波纹的高度，总共10个矩形，左右各有10个
    private int[] DEFAULT_WAVE_HEIGHT = { 2, 3, 4, 3, 2, 2, 2, 2, 2, 2 };
    private LinkedList<Integer> mWaveList = new LinkedList<>();

    private RectF rectRight = new RectF();//右边波纹矩形的数据，10个矩形复用一个rectF
    private RectF rectLeft = new RectF();//左边波纹矩形的数据

    LinkedList<Integer> list = new LinkedList<>();

    private static final int UPDATE_INTERVAL_TIME = 100;//100ms更新一次

    public LineWaveVoiceView(Context context) {
        super(context);
    }

    public LineWaveVoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineWaveVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        resetList(list, DEFAULT_WAVE_HEIGHT);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                while (isStart) {
                    refreshElement();
                    try {
                        Thread.sleep(UPDATE_INTERVAL_TIME);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }
            }
        };
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.LineWaveVoiceView);
        lineColor = mTypedArray.getColor(R.styleable.LineWaveVoiceView_voiceLineColor, Color.parseColor("#ff9c00"));
        lineWidth = mTypedArray.getDimension(R.styleable.LineWaveVoiceView_voiceLineWidth, LINE_W);
        textSize = mTypedArray.getDimension(R.styleable.LineWaveVoiceView_voiceTextSize, 42);
        textColor = mTypedArray.getColor(R.styleable.LineWaveVoiceView_voiceTextColor, Color.parseColor("#666666"));
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int widthCentre = getWidth() / 2;
        int heightCentre = getHeight() / 2;

        //更新时间
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        float textWidth = paint.measureText(text);
        canvas.drawText(text, widthCentre - textWidth / 2, heightCentre - (paint.ascent() + paint.descent()) / 2, paint);

        //更新左右两边的波纹矩形
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(lineWidth);
        paint.setAntiAlias(true);
        for (int i = 0; i < 10; i++) {
            //右边矩形
            rectRight.left = widthCentre + 2 * i * lineWidth + textWidth / 2 + lineWidth;
            rectRight.top = heightCentre - list.get(i) * lineWidth / 2;
            rectRight.right = widthCentre + 2 * i * lineWidth + 2 * lineWidth + textWidth / 2;
            rectRight.bottom = heightCentre + list.get(i) * lineWidth / 2;

            //左边矩形
            rectLeft.left = widthCentre - (2 * i * lineWidth + textWidth / 2 + 2 * lineWidth);
            rectLeft.top = heightCentre - list.get(i) * lineWidth / 2;
            rectLeft.right = widthCentre - (2 * i * lineWidth + textWidth / 2 + lineWidth);
            rectLeft.bottom = heightCentre + list.get(i) * lineWidth / 2;

            canvas.drawRoundRect(rectRight, 6, 6, paint);
            canvas.drawRoundRect(rectLeft, 6, 6, paint);
        }
    }

    private synchronized void refreshElement() {
        RecordManager.getInstance().setRecordSoundSizeListener(new RecordSoundSizeListener() {
            @Override
            public void onSoundSize(int soundSize) {
                LogUtils.i(TAG, "refreshElement, maxAmp " + soundSize);
                int waveH = MIN_WAVE_H + Math.round(((float) soundSize / 100) * (MAX_WAVE_H - 2));//wave 在 2 ~ 7 之间
                LogUtils.i(waveH+"---");
                list.add(0, waveH);
                list.removeLast();
            }
        });
    }

    public synchronized void setText(String text) {
        this.text = text;
        postInvalidate();
    }

    public synchronized void startRecord() {
        isStart = true;
        ThreadUtils.getCachedPool().execute(mRunnable);
    }

    public synchronized void stopRecord() {
        isStart = false;
        mWaveList.clear();
        resetList(list, DEFAULT_WAVE_HEIGHT);
        text = DEFAULT_TEXT;
        postInvalidate();
    }

    private void resetList(List list, int[] array) {
        list.clear();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
    }
}
