package com.facilityone.wireless.a.arch.base;

import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.utils.NoDoubleClickListener;
import com.gyf.barlibrary.ImmersionBar;
import com.joanzapata.iconify.widget.IconTextView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:基础Fragment（delegate）
 * Date: 2018/3/29 下午6:02
 */
public abstract class FMFragment extends SwipeBackFragment {

    private View mRootView;
    protected ImmersionBar mImmersionBar;
    protected QMUITopBarLayout mTopBarLayout;
    protected QMUITopBar mTopBar;
    private IconTextView mMoreView;

    public FMFragment() {
        super();
    }

    /**
     * 设置fragment的布局id或view
     */
    public abstract Object setLayout();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (setLayout() instanceof Integer) {
            mRootView = inflater.inflate((int) setLayout(), container, false);
        } else if (setLayout() instanceof View) {
            mRootView = (View) setLayout();
        } else {
            throw new ClassCastException("type of setLayout() must be layout resId or view");
        }
        //设置可侧滑返回的区域大小
        getSwipeBackLayout().setEdgeLevel(SwipeBackLayout.EdgeLevel.MIN);

        return attachToSwipeBack(mRootView);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            View titleBar = view.findViewById(setTitleBar());
            if (titleBar != null) {
                ImmersionBar.setTitleBar(_mActivity, titleBar);
            }
            View statusBarView = view.findViewById(setStatusBarView());
            if (statusBarView != null) {
                ImmersionBar.setStatusBarView(_mActivity, statusBarView);
            }
        }
        initToolbar(view);
    }

    protected int setTitleBar() {
        return 0;
    }

    protected int setStatusBarView() {
        return 0;
    }

    protected void initToolbar(View view) {
        View titleBar = view.findViewById(setTitleBar());
        if (titleBar != null) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            if (titleBar instanceof QMUITopBarLayout) {
                mTopBarLayout = (QMUITopBarLayout) titleBar;
                if (leftBackEnabled()) {
                    mTopBarLayout.addLeftView(getLeftBackView(), R.id.topbar_left_back_id, lp);
                }
            } else if (titleBar instanceof QMUITopBar) {
                mTopBar = (QMUITopBar) titleBar;
                if (leftBackEnabled()) {
                    mTopBar.addLeftView(getLeftBackView(), R.id.topbar_left_back_id, lp);
                }
            }
        }
    }

    private View getLeftBackView() {
        IconTextView view = (IconTextView) LayoutInflater.from(getContext()).inflate(R.layout.fm_topbar_back, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftBackListener();
            }
        });
        return view;
    }

    public void leftBackListener() {
        pop();
    }

    public TextView setTitle(int resId) {
        return setTitle(getContext().getString(resId));
    }

    public TextView setTitle(String title) {
        TextView textView = null;
        if (mTopBarLayout != null) {
            textView = mTopBarLayout.setTitle(title);
            return textView;
        } else if (mTopBar != null) {
            textView = mTopBar.setTitle(title);
        }
        return textView;
    }

    public void setMoreMenu() {
        if (mMoreView == null) {
            mMoreView = (IconTextView) LayoutInflater.from(getContext()).inflate(R.layout.fm_topbar_more, null);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            mMoreView.setLayoutParams(lp);
            if (mTopBarLayout != null) {
                mTopBarLayout.addRightView(mMoreView, R.id.topbar_right_more_id);
            } else if (mTopBar != null) {
                mTopBar.addRightView(mMoreView, R.id.topbar_right_more_id);
            }

            mMoreView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    onMoreMenuClick(view);
                }
            });
        }
    }

    public void hideShowMoreMenu(boolean show) {
        if (mMoreView != null) {
            mMoreView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }


    public void setMoreMenuVisible(boolean visible) {
        if (mMoreView != null) {
            mMoreView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public void removeRightView() {
        if (mTopBarLayout != null) {
            mTopBarLayout.removeAllRightViews();
        } else if (mTopBar != null) {
            mTopBar.removeAllRightViews();
        }
    }

    public void setRightIcon(@StringRes int textId, @IdRes int id, NoDoubleClickListener listener) {
        setRightIcon(textId, id, R.dimen.topbar_back_size, listener);
    }

    public void setRightIcon(@StringRes int textId
            , @IdRes int id
            , @DimenRes int textSize
            , NoDoubleClickListener listener) {
        IconTextView icon = (IconTextView) LayoutInflater.from(getContext()).inflate(R.layout.fm_topbar_more, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        icon.setLayoutParams(lp);
        icon.setPadding(SizeUtils.dp2px(8), 0, SizeUtils.dp2px(8), 0);
        icon.setText(textId);
        icon.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(textSize));
        if (mTopBarLayout != null) {
            mTopBarLayout.addRightView(icon, id);
        } else if (mTopBar != null) {
            mTopBar.addRightView(icon, id);
        }

        icon.setOnClickListener(listener);
    }

    public void setRightImageButton(@DrawableRes int drawableResId, @IdRes int viewId) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(drawableResId);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(SizeUtils.dp2px(20), SizeUtils.dp2px(20));
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.setMargins(SizeUtils.dp2px(10), 0, SizeUtils.dp2px(10), 0);
        imageView.setLayoutParams(layoutParams);
        if (mTopBarLayout != null) {
            mTopBarLayout.addRightView(imageView, viewId);
        } else if (mTopBar != null) {
            mTopBar.addRightView(imageView, viewId);
        }

        imageView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                onRightImageMenuClick(view);
            }
        });
    }


    /**
     * 添加右边文本菜单
     *
     * @param text
     * @param id
     */
    public void setRightTextButton(String text, @IdRes int id) {
        Button button = null;
        if (mTopBarLayout != null) {
            button = mTopBarLayout.addRightTextButton(text, id);
        } else if (mTopBar != null) {
            button = mTopBar.addRightTextButton(text, id);
        }
        if (button == null) {
            return;
        }
        button.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                onRightTextMenuClick(view);
            }
        });
    }

    public void setRightTextButton(@StringRes int textId, @IdRes int id) {
        String text = getContext().getResources().getString(textId);
        setRightTextButton(text, id);
    }

    public void onMoreMenuClick(View view) {

    }

    public void onRightTextMenuClick(View view) {
    }

    public void onRightImageMenuClick(View view) {

    }

    public void setSubTitle(int resId) {
        setSubTitle(getContext().getString(resId));
    }

    public void setSubTitle(String title) {
        if (mTopBarLayout != null) {
            mTopBarLayout.setSubTitle(title);
        } else if (mTopBar != null) {
            mTopBar.setSubTitle(title);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //如果要在Fragment单独使用沉浸式，请在onSupportVisible实现沉浸式
        if (isImmersionBarEnabled()) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar.navigationBarWithKitkatEnable(false).init();
        }
    }

    protected boolean leftBackEnabled() {
        return true;
    }

    protected boolean isImmersionBarEnabled() {
        return false;
    }


    public <T extends View> T findViewById(@IdRes int resId) {
        if (mRootView != null) {
            return (T) mRootView.findViewById(resId);
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
}