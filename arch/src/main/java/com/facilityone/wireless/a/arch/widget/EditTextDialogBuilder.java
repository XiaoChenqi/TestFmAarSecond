package com.facilityone.wireless.a.arch.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.method.TransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.utils.NoDoubleClickListener;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUILangHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/5/23 下午6:06
 */
public class EditTextDialogBuilder extends QMUIDialogBuilder<EditTextDialogBuilder> {

    protected String mPlaceholder;
    protected TransformationMethod mTransformationMethod;
    protected RelativeLayout mMainLayout;
    protected EditText mEditText;
    protected ImageView mRightImageView;
    private int mInputType = InputType.TYPE_CLASS_TEXT;
    private CharSequence mDefaultText = null;
    private Button mLeftBtn;
    private Button mRightBtn;
    private OnActionClickListener mLeftListener;
    private OnActionClickListener mRightListener;
    private String mLeftStr;
    private String mRightStr;
    private Activity mActivity;

    public EditTextDialogBuilder(Context context) {
        super(context);
        mActivity = (Activity) context;
    }

    /**
     * 设置输入框的 placeholder
     */
    public EditTextDialogBuilder setPlaceholder(String placeholder) {
        this.mPlaceholder = placeholder;
        return this;
    }

    /**
     * 设置输入框的 placeholder
     */
    public EditTextDialogBuilder setPlaceholder(int resId) {
        return setPlaceholder(getBaseContext().getResources().getString(resId));
    }

    public EditTextDialogBuilder setDefaultText(CharSequence defaultText) {
        mDefaultText = defaultText;
        return this;
    }

    /**
     * 设置 EditText 的 transformationMethod
     */
    public EditTextDialogBuilder setTransformationMethod(TransformationMethod transformationMethod) {
        mTransformationMethod = transformationMethod;
        return this;
    }

    /**
     * 设置 EditText 的 inputType
     */
    public EditTextDialogBuilder setInputType(int inputType) {
        mInputType = inputType;
        return this;
    }

    @Override
    protected void onCreateContent(QMUIDialog dialog, ViewGroup parent, Context context) {
        mEditText = new EditText(context);
        QMUIDialog.MessageDialogBuilder.assignMessageTvWithAttr(mEditText, hasTitle(), R.attr.qmui_dialog_edit_content_style);
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
        mEditText.setId(R.id.qmui_dialog_edit_input);

        if (!QMUILangHelper.isNullOrEmpty(mDefaultText)) {
            mEditText.setText(mDefaultText);
        }

        mRightImageView = new ImageView(context);
        mRightImageView.setId(R.id.qmui_dialog_edit_right_icon);
        mRightImageView.setVisibility(View.GONE);

        mMainLayout = new RelativeLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = mEditText.getPaddingTop();
        lp.leftMargin = mEditText.getPaddingLeft();
        lp.rightMargin = mEditText.getPaddingRight();
        lp.bottomMargin = mEditText.getPaddingBottom();
        mMainLayout.setBackgroundResource(R.drawable.dialog_edittext_bg_border_bottom);
        mMainLayout.setLayoutParams(lp);

        if (mTransformationMethod != null) {
            mEditText.setTransformationMethod(mTransformationMethod);
        } else {
            mEditText.setInputType(mInputType);
        }

        mEditText.setBackgroundResource(0);
        mEditText.setPadding(QMUIDisplayHelper.dpToPx(6), 0, QMUIDisplayHelper.dpToPx(6), QMUIDisplayHelper.dpToPx(6));
        RelativeLayout.LayoutParams editLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editLp.addRule(RelativeLayout.LEFT_OF, mRightImageView.getId());
        editLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        if (mPlaceholder != null) {
            mEditText.setHint(mPlaceholder);
        }
        mMainLayout.addView(mEditText, createEditTextLayoutParams());
        mMainLayout.addView(mRightImageView, createRightIconLayoutParams());

        parent.addView(mMainLayout);
    }

    protected RelativeLayout.LayoutParams createEditTextLayoutParams() {
        RelativeLayout.LayoutParams editLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editLp.addRule(RelativeLayout.LEFT_OF, mRightImageView.getId());
        editLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        return editLp;
    }

    protected RelativeLayout.LayoutParams createRightIconLayoutParams() {
        RelativeLayout.LayoutParams rightIconLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightIconLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rightIconLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        rightIconLp.leftMargin = QMUIDisplayHelper.dpToPx(5);
        return rightIconLp;
    }

    @Override
    protected void onAfter(QMUIDialog dialog, LinearLayout parent, Context context) {
        super.onAfter(dialog, parent, context);
        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                View currentFocus = mActivity.getCurrentFocus();
                if (currentFocus != null) {
                    inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }else{
                    inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                }
            }
        });

        mEditText.setSelection(mEditText.getText().length());
        mEditText.requestFocus();
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.showSoftInput(mEditText, 0);
            }
        }, 300);
    }

    public EditText getEditText() {
        return mEditText;
    }

    public ImageView getRightImageView() {
        return mRightImageView;
    }

    @Override
    protected void onCreateHandlerBar(final QMUIDialog dialog, ViewGroup parent, Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_two_button_menu, parent, false);
        mLeftBtn = (Button) view.findViewById(R.id.btn_left);
        if (!QMUILangHelper.isNullOrEmpty(mLeftStr)) {
            mLeftBtn.setText(mLeftStr);
        }
        mLeftBtn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (mLeftListener != null) {
                    mLeftListener.onClick(dialog, view);
                }
            }
        });
        mRightBtn = (Button) view.findViewById(R.id.btn_right);
        if (!QMUILangHelper.isNullOrEmpty(mRightStr)) {
            mRightBtn.setText(mRightStr);
        }
        mRightBtn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (mRightListener != null) {
                    mRightListener.onClick(dialog, view);
                }
            }
        });
        parent.addView(view);
    }

    public Button getLeftButton() {
        return mLeftBtn;
    }

    public Button getRightButton() {
        return mRightBtn;
    }

    public EditTextDialogBuilder addLeftClickListener(String str, OnActionClickListener listener) {
        mLeftListener = listener;
        mLeftStr = str;
        return this;
    }

    public EditTextDialogBuilder addRightClickListener(String str, OnActionClickListener listener) {
        mRightListener = listener;
        mRightStr = str;
        return this;
    }

    public interface OnActionClickListener {
        void onClick(QMUIDialog dialog, View v);
    }
}
