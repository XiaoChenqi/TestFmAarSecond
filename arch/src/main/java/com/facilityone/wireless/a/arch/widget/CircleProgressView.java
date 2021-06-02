package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.facilityone.wireless.a.arch.R;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:圆形进度
 * Date: 2018/6/28 下午4:12
 */
public class CircleProgressView extends View {

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 中间完成的字符串的字体
     */
    private float downTvSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;
    /**
     * 进度画的方向
     *
     * @true:顺时针
     * @false:逆时针
     */
    private boolean proOrient;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CircleProgressView);
        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.CircleProgressView_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.CircleProgressView_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.CircleProgressView_tvColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.CircleProgressView_tvSize, 15);
        downTvSize = mTypedArray.getDimension(R.styleable.CircleProgressView_down_tvSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.CircleProgressView_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.CircleProgressView_max, 0);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.CircleProgressView_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.CircleProgressView_style, 0);
        proOrient = mTypedArray.getBoolean(R.styleable.CircleProgressView_proOrient, true);
        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /** 去锯齿 */
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));

        /**
         * 画最外层的大圆环
         */
        int centre = getWidth() / 2; //获取圆心的x坐标
        int radius = (int) (centre - roundWidth / 2); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环

        /**
         * 画进度百分比
         */

        //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        int percent = (int) (((float) progress / (float) (max = getMax())) * 100);
        paint.setStrokeWidth(0);
        if (percent == 0) {
            paint.setColor(roundColor);
        } else {
            paint.setColor(textColor);
        }
        paint.setTextSize(textSize);
        //设置字体
        paint.setTypeface(Typeface.DEFAULT);

        //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        if (textIsDisplayable && style == STROKE) {
            float textWidth1 = paint.measureText(progress + "");
            paint.setTextSize(textSize / 2);
            float textWidth2 = paint.measureText("/" + max);
            paint.setTextSize(textSize);
            canvas.drawText(progress + "", centre - (textWidth1 + textWidth2) / 2, centre + textSize / 8, paint);
            paint.setTextSize(textSize / 2);
            canvas.drawText("/" + max, centre - (textWidth1 + textWidth2) / 2 + textWidth1, centre - textSize / 4, paint);
            float textWidth3 = paint.measureText("完成");
            paint.setTextSize(downTvSize);
            paint.setColor(getResources().getColor(R.color.grey_6));
            canvas.drawText("完成",
                    centre - textWidth3 / 2, centre + +textSize * 7 / 8, paint);
        }

        /**
         * 画圆弧 ，画圆环的进度
         */
        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                if (max == 0) max = 1;
                if (proOrient) {
                    canvas.drawArc(oval, 135, 360 * progress / max, false, paint);  //根据进度画圆弧
                } else {
                    canvas.drawArc(oval, -45, -360 * progress / max, false, paint);  //根据进度画圆弧
                }
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (max == 0) max = 1;
                if (progress != 0)
                    canvas.drawArc(oval, -45, -360 * progress / max, true, paint);  //根据进度画圆弧
                break;
            }
        }
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public boolean isProOrient() {
        return proOrient;
    }

    public void setProOrient(boolean proOrient) {
        this.proOrient = proOrient;
    }
}
