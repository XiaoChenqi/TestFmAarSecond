package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.utils.ViewUtil;

/**
 * Created by: owen.
 * Date: on 2018/6/4 下午12:26.
 * Description: 带删除按钮的搜索框
 * email:
 */

public class SearchBox extends LinearLayout {
    private Context mContext;
    /* start: search box */
    private View mSearchBox;

    private EditText mSearchEt;

    private ImageView mDeleteIv;

    private LinearLayout mSearchMap;
    private LinearLayout mSearchImage;


    /* end: search box */
    private OnSearchBox mOnSearchBox;


    private OnBoxClickListener mBoxClickListener;

    public interface OnSearchBox {
        void onSearchTextChanged(String curCharacter);
    }

    public interface OnBoxClickListener {
        void onBoxClick();
    }

    public SearchBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
        initListener();
    }


    public OnSearchBox getOnSearchBox() {
        return mOnSearchBox;
    }

    public void setOnSearchBox(OnSearchBox onSearchBox) {
        this.mOnSearchBox = onSearchBox;
    }

    public OnBoxClickListener getOnSeachBoxClick() {
        return mBoxClickListener;
    }

    public void setBoxClickListener(OnBoxClickListener onBoxClickListener) {
        this.mBoxClickListener = onBoxClickListener;
    }

    public EditText getSearchEt() {
        return mSearchEt;
    }

    public void setSearchEt(EditText searchEt) {
        mSearchEt = searchEt;
    }

    public String getSearchEtInput() {
        return mSearchEt.getText().toString();
    }


    private void init(AttributeSet attrs) {
        LayoutInflater.from(mContext).inflate(R.layout.search_box, this);
        mSearchBox = findViewById(R.id.search_box_ll);

        mSearchEt = (EditText) findViewById(R.id.search_edit_text);
        mDeleteIv = (ImageView) findViewById(R.id.delete_image_view);
        mSearchMap = (LinearLayout) findViewById(R.id.ll_search_map);
        mSearchImage = (LinearLayout) findViewById(R.id.ll_imv_search);

        if (attrs != null) {
            TypedArray attrsArray = getContext().obtainStyledAttributes(attrs,
                    R.styleable.SearchBox);
            mSearchEt.setHint(attrsArray.getString(R.styleable.SearchBox_android_hint));

            int cbDrawable = attrsArray.getResourceId(R.styleable.SearchBox_sb_clrBackGround, 0);
            if (cbDrawable > 0) {
                mDeleteIv.setBackgroundResource(cbDrawable);
            }

            attrsArray.recycle();
        }
    }

    private void initListener() {
        mSearchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null != mOnSearchBox) {
                    String inputStr = s.toString();
                    mOnSearchBox.onSearchTextChanged(inputStr);
                    if (TextUtils.isEmpty(inputStr)) {
                        ViewUtil.hideView(mDeleteIv);
                    } else {
                        ViewUtil.showView(mDeleteIv);
                    }
                }

            }
        });
        mSearchEt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBoxClickListener != null) {
                    mBoxClickListener.onBoxClick();
                }
            }
        });
        mDeleteIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    private void delete() {
        mSearchEt.setText("");
        ViewUtil.hideView(mDeleteIv);
    }

    public void clearSearchBox() {
        delete();
    }

    public LinearLayout getSearchMap() {
        return mSearchMap;
    }

    public LinearLayout getSearchImage() {
        return mSearchImage;
    }

    public void setSearchMapVisible() {
        if (mSearchMap != null) {
            mSearchMap.setVisibility(VISIBLE);
        }

    }
}
