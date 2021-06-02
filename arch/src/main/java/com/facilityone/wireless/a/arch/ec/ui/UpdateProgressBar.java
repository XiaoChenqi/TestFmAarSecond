package com.facilityone.wireless.a.arch.ec.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.SizeUtils;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:更新app进度条
 * Date: 2019/1/2 3:19 PM
 */
public class UpdateProgressBar extends ProgressBar {
    String text;
    Paint mPaint;

    public UpdateProgressBar(Context context) {
        super(context);
        initText();
    }

    public UpdateProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initText();
    }


    public UpdateProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initText();
    }

    @Override
    public synchronized void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //this.setText();
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text, x, y, this.mPaint);
    }

    //初始化，画笔
    private void initText() {
        this.mPaint = new Paint();
        mPaint.setTextSize(SizeUtils.dp2px(10));
        this.mPaint.setColor(Color.BLACK);
    }

    private void setText() {
        setText(this.getProgress());
    }

    //设置文字内容
    private void setText(int progress) {
        int i = (progress * 100) / this.getMax();
        this.text = String.valueOf(i) + "%";
    }
}
