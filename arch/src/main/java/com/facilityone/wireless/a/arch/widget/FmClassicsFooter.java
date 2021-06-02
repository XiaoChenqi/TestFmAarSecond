package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.ArrowDrawable;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:自定义加载更多底部
 * Date: 2019/4/28 9:19 AM
 */
public class FmClassicsFooter extends LinearLayout implements RefreshFooter {

    //    private TextView mHeaderText;//标题文本
    private ImageView mArrowView;//下拉箭头
    private ImageView mProgressView;//刷新动画视图
    private ProgressDrawable mProgressDrawable;//刷新动画
    private ArrowDrawable mArrowDrawable;

    protected boolean mNoMoreData = false;

    public FmClassicsFooter(Context context) {
        super(context);
        this.initView(context);
    }

    public FmClassicsFooter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public FmClassicsFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER);
//        mHeaderText = new TextView(context);
        mProgressDrawable = new ProgressDrawable();
        mArrowView = new ImageView(context);
        mArrowDrawable = new ArrowDrawable();
        mArrowDrawable.setColor(0xff666666);
        mArrowView.setImageDrawable(mArrowDrawable);
        mProgressView = new ImageView(context);
        mProgressView.setVisibility(GONE);
        mProgressView.animate().rotation(180);
        mProgressView.setImageDrawable(mProgressDrawable);
        addView(mProgressView, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
        addView(mArrowView, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
        addView(new View(context), DensityUtil.dp2px(20), DensityUtil.dp2px(20));
//        addView(mHeaderText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setMinimumHeight(DensityUtil.dp2px(60));
    }

    @NonNull
    @Override
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        onStartAnimator(refreshLayout, height, maxDragHeight);
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        if (!mNoMoreData) {
            final View progressView = mProgressView;
            if (progressView.getVisibility() != VISIBLE) {
                progressView.setVisibility(VISIBLE);
                Drawable drawable = mProgressView.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                } else {
                    progressView.animate().rotation(36000).setDuration(100000);
                }
            }
        }
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        if (!mNoMoreData) {
            final View progressView = mProgressView;
            Drawable drawable = mProgressView.getDrawable();
            if (drawable instanceof Animatable) {
                if (((Animatable) drawable).isRunning()) {
                    ((Animatable) drawable).stop();
                }
            } else {
                progressView.animate().rotation(0).setDuration(0);
            }
            progressView.setVisibility(GONE);
        }

        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        final View arrowView = mArrowView;
        final View progressView = mProgressView;
        if (!mNoMoreData) {
            switch (newState) {
                case None:
                    arrowView.setVisibility(VISIBLE);
                case PullUpToLoad:
                    arrowView.animate().rotation(180);
                    progressView.setVisibility(GONE);//隐藏动画
                    break;
                case Loading:
                case LoadReleased:
                    arrowView.setVisibility(GONE);
                    break;
                case ReleaseToLoad:
                    arrowView.animate().rotation(0);
                    break;
                case Refreshing:
                    arrowView.setVisibility(GONE);
                    break;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        final View arrowView = mArrowView;
        final View progressView = mProgressView;
        arrowView.animate().cancel();
        progressView.animate().cancel();
        final Drawable drawable = mProgressView.getDrawable();
        if (drawable instanceof Animatable) {
            if (((Animatable) drawable).isRunning()) {
                ((Animatable) drawable).stop();
            }
        }
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData;
            final View arrowView = mArrowView;
            if (noMoreData) {
                arrowView.setVisibility(GONE);
            } else {
                arrowView.setVisibility(VISIBLE);
            }
        }
        return true;
    }
}
