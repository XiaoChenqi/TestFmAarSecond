package com.facilityone.wireless.maintenance.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * 演示一个变态需求的周视图
 * Created by peter on 2018/11/16.
 */

public class CustomWeekView extends WeekView {


    private int mRadius;

    /**
     * 自定义文本画笔
     */
    private Paint mTextPaint = new Paint();


    public CustomWeekView(Context context) {
        super(context);

        mTextPaint.setTextSize(DensityUtil.dp2px(9));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);



    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 3 ;
    }


    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 3;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);

        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {

        int cx = x + mItemWidth / 2;
        mTextPaint.setColor(calendar.getSchemeColor());
        canvas.drawText(calendar.getScheme(),cx,mTextBaseLine  + mItemHeight / 5, mTextPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = - mItemHeight / 6;


        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);

        }

    }

}
