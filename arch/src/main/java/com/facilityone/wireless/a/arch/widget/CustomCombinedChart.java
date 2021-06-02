package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.CombinedChart;

/**
 * Created by: owen.
 * Date: on 2018/11/14 下午12:18.
 * Description:自定义组合图表
 * email:
 */

public class CustomCombinedChart  extends CombinedChart {

    public CustomCombinedChart(Context context) {
        super(context);
    }

    public CustomCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
