package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facilityone.wireless.a.arch.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheetItemView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:底部是网格的菜单
 * Date: 2018/6/25 下午4:50
 */
public class FMBottomGridSheetBuilder implements View.OnClickListener {

    /**
     * item 出现在第一行
     */
    public static final int FIRST_LINE = 0;
    /**
     * item 出现在第二行
     */
    public static final int SECOND_LINE = 1;
    private Context mContext;
    private QMUIBottomSheet mDialog;
    private SparseArray<View> mFirstLineViews;
    private SparseArray<View> mSecondLineViews;
    private int mMiniItemWidth = -1;
    private OnSheetItemClickListener mOnSheetItemClickListener;
    private Typeface mItemTextTypeFace = null;
    private ViewGroup mBottomButtonContainer;
    private ViewGroup mTopTitleContainer;
    private TextView mBottomButton;
    private TextView mTopTitle;
    private Typeface mBottomButtonTypeFace = null;
    private boolean mIsShowButton = true;
    private boolean mIsShowTitle = false;
    private CharSequence mButtonText = null;
    private CharSequence mTitleText = null;
    private View.OnClickListener mButtonClickListener = null;

    public FMBottomGridSheetBuilder(Context context) {
        mContext = context;
        mFirstLineViews = new SparseArray<>();
        mSecondLineViews = new SparseArray<>();
    }

    public FMBottomGridSheetBuilder addItem(int imageRes, CharSequence textAndTag, @Style int style) {
        return addItem(imageRes, textAndTag, textAndTag, style, 0);
    }

    public FMBottomGridSheetBuilder addItem(int imageRes, CharSequence text, Object tag, @Style int style) {
        return addItem(imageRes, text, tag, style, 0);
    }

    public FMBottomGridSheetBuilder setIsShowButton(boolean isShowButton) {
        mIsShowButton = isShowButton;
        return this;
    }

    public FMBottomGridSheetBuilder setIsShowTitle(boolean isShowTitle) {
        mIsShowTitle = isShowTitle;
        return this;
    }

    public FMBottomGridSheetBuilder setButtonText(CharSequence buttonText) {
        mButtonText = buttonText;
        return this;
    }

    public FMBottomGridSheetBuilder setTitleText(CharSequence titleText) {
        mTitleText = titleText;
        return this;
    }

    public FMBottomGridSheetBuilder setButtonClickListener(View.OnClickListener buttonClickListener) {
        mButtonClickListener = buttonClickListener;
        return this;
    }

    public FMBottomGridSheetBuilder setItemTextTypeFace(Typeface itemTextTypeFace) {
        mItemTextTypeFace = itemTextTypeFace;
        return this;
    }

    public FMBottomGridSheetBuilder setBottomButtonTypeFace(Typeface bottomButtonTypeFace) {
        mBottomButtonTypeFace = bottomButtonTypeFace;
        return this;
    }

    public FMBottomGridSheetBuilder addItem(int imageRes, CharSequence text, Object tag, @Style int style, int subscriptRes) {
        QMUIBottomSheetItemView itemView = createItemView(AppCompatResources.getDrawable(mContext, imageRes), text, tag, subscriptRes);
        return addItem(itemView, style);
    }

    public FMBottomGridSheetBuilder addItem(View view, @Style int style) {
        switch (style) {
            case FIRST_LINE:
                mFirstLineViews.append(mFirstLineViews.size(), view);
                break;
            case SECOND_LINE:
                mSecondLineViews.append(mSecondLineViews.size(), view);
                break;
        }
        return this;
    }

    public QMUIBottomSheetItemView createItemView(Drawable drawable, CharSequence text, Object tag, int subscriptRes) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        QMUIBottomSheetItemView itemView = (QMUIBottomSheetItemView) inflater.inflate(getItemViewLayoutId(), null, false);
        TextView titleTV = (TextView) itemView.findViewById(R.id.grid_item_title);
        if (mItemTextTypeFace != null) {
            titleTV.setTypeface(mItemTextTypeFace);
        }
        titleTV.setText(text);

        itemView.setTag(tag);
        itemView.setOnClickListener(this);
        AppCompatImageView imageView = (AppCompatImageView) itemView.findViewById(R.id.grid_item_image);
        imageView.setImageDrawable(drawable);

        return itemView;
    }

    public void setItemVisibility(Object tag, int visibility) {
        View foundView = null;
        for (int i = 0; i < mFirstLineViews.size(); i++) {
            View view = mFirstLineViews.get(i);
            if (view != null && view.getTag().equals(tag)) {
                foundView = view;
            }
        }
        for (int i = 0; i < mSecondLineViews.size(); i++) {
            View view = mSecondLineViews.get(i);
            if (view != null && view.getTag().equals(tag)) {
                foundView = view;
            }
        }
        if (foundView != null) {
            foundView.setVisibility(visibility);
        }
    }

    public FMBottomGridSheetBuilder setOnSheetItemClickListener(OnSheetItemClickListener onSheetItemClickListener) {
        mOnSheetItemClickListener = onSheetItemClickListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (mOnSheetItemClickListener != null) {
            mOnSheetItemClickListener.onClick(mDialog, v);
        }
    }

    public QMUIBottomSheet build() {
        mDialog = new QMUIBottomSheet(mContext);
        View contentView = buildViews();
        mDialog.setContentView(contentView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return mDialog;
    }

    private View buildViews() {
        LinearLayout baseLinearLayout;
        baseLinearLayout = (LinearLayout) View.inflate(mContext, getContentViewLayoutId(), null);
        LinearLayout firstLine = (LinearLayout) baseLinearLayout.findViewById(R.id.bottom_sheet_first_linear_layout);
        LinearLayout secondLine = (LinearLayout) baseLinearLayout.findViewById(R.id.bottom_sheet_second_linear_layout);
        mBottomButtonContainer = (ViewGroup) baseLinearLayout.findViewById(R.id.bottom_sheet_button_container);
        mTopTitleContainer = (ViewGroup) baseLinearLayout.findViewById(R.id.top_sheet_button_container);
        mBottomButton = (TextView) baseLinearLayout.findViewById(R.id.bottom_sheet_close_button);
        mTopTitle = (TextView) baseLinearLayout.findViewById(R.id.top_sheet_title_button);

        int maxItemCountEachLine = Math.max(mFirstLineViews.size(), mSecondLineViews.size());
        int screenWidth = QMUIDisplayHelper.getScreenWidth(mContext);
        int screenHeight = QMUIDisplayHelper.getScreenHeight(mContext);
        int width = screenWidth < screenHeight ? screenWidth : screenHeight;
        int itemWidth = calculateItemWidth(width, maxItemCountEachLine, firstLine.getPaddingLeft(), firstLine.getPaddingRight());

        addViewsInSection(mFirstLineViews, firstLine, itemWidth);
        addViewsInSection(mSecondLineViews, secondLine, itemWidth);

        boolean hasFirstLine = mFirstLineViews.size() > 0;
        boolean hasSecondLine = mSecondLineViews.size() > 0;
        if (!hasFirstLine) {
            firstLine.setVisibility(View.GONE);
        }
        if (!hasSecondLine) {
            if (hasFirstLine) {
                firstLine.setPadding(
                        firstLine.getPaddingLeft(),
                        firstLine.getPaddingTop(),
                        firstLine.getPaddingRight(),
                        0);
            }
            secondLine.setVisibility(View.GONE);
        }

        // button 在用户自定义了contentView的情况下可能不存在
        if (mBottomButtonContainer != null) {
            if (mIsShowButton) {
                mBottomButtonContainer.setVisibility(View.VISIBLE);
                int dimen = QMUIResHelper.getAttrDimen(mContext, R.attr.qmui_bottom_sheet_grid_padding_vertical);
                baseLinearLayout.setPadding(0, dimen, 0, 0);
            } else {
                mBottomButtonContainer.setVisibility(View.GONE);
            }
            if (mBottomButtonTypeFace != null) {
                mBottomButton.setTypeface(mBottomButtonTypeFace);
            }
            if (mButtonText != null) {
                mBottomButton.setText(mButtonText);
            }

            if (mButtonClickListener != null) {
                mBottomButton.setOnClickListener(mButtonClickListener);
            } else {
                mBottomButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
            }
        }

        if(mTopTitleContainer != null) {
            if(mIsShowTitle) {
                mTopTitleContainer.setVisibility(View.VISIBLE);
            }else {
                mTopTitleContainer.setVisibility(View.GONE);
            }

            if(mTitleText != null) {
                mTopTitle.setText(mTitleText);
            }
        }

        return baseLinearLayout;
    }

    protected int getContentViewLayoutId() {
        return R.layout.fm_bottom_sheet_grid;
    }

    protected int getItemViewLayoutId() {
        return R.layout.fm_bottom_sheet_grid_item;
    }

    /**
     * 拿个数最多的一行，去决策item的平铺/拉伸策略
     *
     * @return item 宽度
     */
    private int calculateItemWidth(int width, int maxItemCountInEachLine, int paddingLeft, int paddingRight) {
        if (mMiniItemWidth == -1) {
            mMiniItemWidth = QMUIResHelper.getAttrDimen(mContext, R.attr.qmui_bottom_sheet_grid_item_mini_width);
        }

        final int parentSpacing = width - paddingLeft - paddingRight;
        int itemWidth = mMiniItemWidth;
        // 看是否需要把 Item 拉伸平分 parentSpacing
        if (maxItemCountInEachLine >= 3
                && parentSpacing - maxItemCountInEachLine * itemWidth > 0
                && parentSpacing - maxItemCountInEachLine * itemWidth < itemWidth) {
            int count = parentSpacing / itemWidth;
            itemWidth = parentSpacing / count;
        }
        // 看是否需要露出半个在屏幕边缘
        if (itemWidth * maxItemCountInEachLine > parentSpacing) {
            int count = (width - paddingLeft) / itemWidth;
            itemWidth = (int) ((width - paddingLeft) / (count + .5f));
        }
        return itemWidth;
    }

    protected void addViewsInSection(SparseArray<View> items, LinearLayout parent, int itemWidth) {

        for (int i = 0; i < items.size(); i++) {
            View itemView = items.get(i);
            setItemWidth(itemView, itemWidth);
            parent.addView(itemView);
        }
    }

    private void setItemWidth(View itemView, int itemWidth) {
        LinearLayout.LayoutParams itemLp;
        if (itemView.getLayoutParams() != null) {
            itemLp = (LinearLayout.LayoutParams) itemView.getLayoutParams();
            itemLp.width = itemWidth;
        } else {
            itemLp = new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(itemLp);
        }
        itemLp.gravity = Gravity.CENTER_VERTICAL;
    }

    public interface OnSheetItemClickListener {
        void onClick(QMUIBottomSheet dialog, View itemView);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FIRST_LINE, SECOND_LINE})
    public @interface Style {
    }
}
