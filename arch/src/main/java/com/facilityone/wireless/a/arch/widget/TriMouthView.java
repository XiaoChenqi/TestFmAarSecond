package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.facilityone.wireless.a.arch.R;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:自定义带有三角嘴的View
 * Date: 2018/7/1 上午12:53
 */
public class TriMouthView extends RelativeLayout {

    private TextView mContentIv;
    private Paint paint;
    private Path path;
    private PathEffect effects;
    private float leftHeight;

    private final static float START_X = 0;
    private final static float ANGLE_LENGTH = SizeUtils.dp2px(6);
    private final static float CORN_WIDTH = SizeUtils.dp2px(2);
    private final static float LEFT_LENGTH = SizeUtils.dp2px(12);
    private final static double SIN_VALUE = Math.PI / 6;
    private final static float VALUE_X = (float) (ANGLE_LENGTH * Math.cos(SIN_VALUE));
    private final static float VALUE_Y = (float) (ANGLE_LENGTH * Math.sin(SIN_VALUE));
    private final static float START_Y = CORN_WIDTH + VALUE_Y + LEFT_LENGTH;

    public TriMouthView(Context context) {
        super(context);
        init(null);
    }

    public TriMouthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TriMouthView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
        // 抗锯齿
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(SizeUtils.dp2px(0.4f));
        paint.setColor(getResources().getColor(R.color.grey_b2));
        path = new Path();
        effects = new DashPathEffect(new float[]{ 8, 3, 8, 3 }, 1);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_view_trimouth_item_layout, this, true);
        mContentIv = (TextView) findViewById(R.id.trimouth_item_content_iv);
        TypedArray attrsArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.TriMouthView);
        if (attrs != null) {
            mContentIv.setText(attrsArray
                    .getString(R.styleable.TriMouthView_triitem_contentText));
            float top = attrsArray
                    .getDimension(R.styleable.TriMouthView_triitem_ctPaddingTop, 0);
            float bottom = attrsArray
                    .getDimension(R.styleable.TriMouthView_triitem_ctPaddingBottom, 0);
            float left = attrsArray
                    .getDimension(R.styleable.TriMouthView_triitem_ctPaddingLeft, 0);
            float right = attrsArray
                    .getDimension(R.styleable.TriMouthView_triitem_ctPaddingRight, 0);

            mContentIv.setPadding((int) (START_X + VALUE_Y + left), (int) top, (int) right, (int) bottom);

            leftHeight = attrsArray
                    .getDimension(R.styleable.TriMouthView_triitem_leftHeight, LEFT_LENGTH);
        }
        invalidate();
        attrsArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getMeasuredWidth();
        float height = getMeasuredHeight();

        path.reset();
        /** --- 1 --- */
        path.moveTo(START_X, START_Y);
        /** --- 1~2 --- */
        path.lineTo(
                START_X + VALUE_X,
                START_Y - VALUE_Y);
        /** --- 2~3 --- */
        path.lineTo(
                START_X + VALUE_X,
                START_Y - VALUE_Y - leftHeight + CORN_WIDTH);
        /** --- 3~4  --- */
        path.quadTo(
                START_X + VALUE_X,
                START_Y - VALUE_Y - leftHeight,
                START_X + VALUE_X + CORN_WIDTH,
                START_Y - VALUE_Y - leftHeight
        );
        /** --- 4~5 --- */
        path.lineTo(
                width - 2 * CORN_WIDTH,
                START_Y - VALUE_Y - leftHeight);
        /** --- 5~6 --- */
        path.quadTo(
                width - CORN_WIDTH,
                START_Y - VALUE_Y - leftHeight,
                width - CORN_WIDTH,
                START_Y - VALUE_Y - leftHeight + CORN_WIDTH
        );
        /** --- 6~7 --- */
        path.lineTo(
                width - CORN_WIDTH,
                height - 2 * CORN_WIDTH);
        /** --- 7~8 --- */
        path.quadTo(
                width - CORN_WIDTH,
                height - CORN_WIDTH,
                width - 2 * CORN_WIDTH,
                height - CORN_WIDTH
        );
        /** --- 8~9 --- */
        path.lineTo(
                START_X + VALUE_X + CORN_WIDTH,
                height - CORN_WIDTH);
        /** --- 9~10 --- */
        path.quadTo(
                START_X + VALUE_X,
                height - CORN_WIDTH,
                START_X + VALUE_X,
                height - 2 * CORN_WIDTH
        );
        /** --- 10~11 --- */
        path.lineTo(
                START_X + VALUE_X,
                START_Y + VALUE_Y);

        path.close();

        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }

    public TextView getmContentIv() {
        return mContentIv;
    }

    public void setmContentIv(TextView mContentIv) {
        this.mContentIv = mContentIv;
    }
}