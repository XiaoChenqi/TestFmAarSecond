package com.facilityone.wireless.a.arch.widget;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facilityone.wireless.a.arch.R;


/**
 * EditText clear through X-shaped button drawn at end
 */
public class ClearEyeEditText extends RelativeLayout implements TextWatcher {

    /**
     * TextView component
     */
    private TextView textView;

    /**
     * EditText component
     */
    private EditText editText;

    /**
     * Button that clears the EditText contents
     */
    private ImageView clearButton;

    /**
     * Button that hide or show the EditText contents
     */
    private ImageView eyeButton;
    private int eyeHideDrawable;
    private int eyeShowDrawable;

    private View lineView;
    private int heightLightColor;
    private int defaultLineColor;

    private boolean isHidden=true;

    /**
     * Additional listener fired when cleared
     */
    private OnClickListener onClickClearListener;

    public ClearEyeEditText(Context context) {
        super(context);
        init(null);
    }

    public ClearEyeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ClearEyeEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * Initialize view
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        LayoutInflater inflater
            = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_view_line_eye_edittext_layout, this, true);

        textView = (TextView) findViewById(R.id.textview);
        editText = (EditText) findViewById(R.id.edittext);
        lineView = findViewById(R.id.bottom_line_view);
        clearButton = (ImageView) findViewById(R.id.button_clear);
        eyeButton = (ImageView) findViewById(R.id.button_eye);
        editText.addTextChangedListener(this);

        boolean enabled = true;
        if (attrs != null){
            TypedArray attrsArray = 
                getContext().obtainStyledAttributes(attrs, R.styleable.ClearEyeEditText);

            editText.setInputType(
                    attrsArray.getInt(
                            R.styleable.ClearEyeEditText_android_inputType, InputType.TYPE_CLASS_TEXT));
            editText.setHint(attrsArray.getString(R.styleable.ClearEyeEditText_android_hint));
            enabled = attrsArray.getBoolean(R.styleable.ClearEyeEditText_android_enabled, true);
            textView.setText(attrsArray.getString(R.styleable.ClearEyeEditText_android_text));

            int drawable = attrsArray.getResourceId(R.styleable.ClearEyeEditText_clreet_etBackGround,0);
            if(drawable > 0){
                editText.setBackgroundResource(drawable);
            }

            heightLightColor = attrsArray.getColor(R.styleable.ClearEyeEditText_clreet_bottomLineColor
                    ,getResources().getColor(R.color.floating_edit_text_highlighted_color));
            defaultLineColor = getResources().getColor(R.color.grey_d6);
            lineView.setBackgroundColor(defaultLineColor);

            int cbDrawable = attrsArray.getResourceId(R.styleable.ClearEyeEditText_clreet_clrBackGround,0);
            if(cbDrawable > 0){
                clearButton.setBackgroundResource(cbDrawable);
            }

            eyeShowDrawable = attrsArray.getResourceId(R.styleable.ClearEyeEditText_clreet_eyeShowBackGround,0);
            eyeHideDrawable = attrsArray.getResourceId(R.styleable.ClearEyeEditText_clreet_eyeHideBackGround,0);
            if(eyeHideDrawable > 0){
                eyeButton.setBackgroundResource(eyeHideDrawable);
            }

            attrsArray.recycle();
        }
        if (enabled) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        clearButton.setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        clearButton.setVisibility(RelativeLayout.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            editText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        lineView.setBackgroundColor(heightLightColor);
                        if (editText.getText().toString().isEmpty()) {
                            clearButton.setVisibility(RelativeLayout.GONE);
                        } else {
                            clearButton.setVisibility(RelativeLayout.VISIBLE);
                        }
                    } else {
                        lineView.setBackgroundColor(defaultLineColor);
                        clearButton.setVisibility(RelativeLayout.GONE);
                    }
                }
            });
        } else {
            editText.setEnabled(false);
        }

        clearButton.setVisibility(RelativeLayout.INVISIBLE);
        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                if (onClickClearListener != null) {
                    onClickClearListener.onClick(v);
                }
                editText.requestFocus();
            }
        });

        eyeButton.setVisibility(RelativeLayout.VISIBLE);
        eyeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHidden) {
                    //设置EditText文本为可见的
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eyeButton.setBackgroundResource(eyeShowDrawable);
                } else {
                    //设置EditText文本为隐藏的
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyeButton.setBackgroundResource(eyeHideDrawable);
                }
                isHidden = !isHidden;
                editText.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = editText.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });


    }
    
    public EditText getEditText() {
    	return editText;
    }

    /**
     * Get value
     * @return text
     */
    public Editable getText() {
        return editText.getText();
    }

    /**
     * Set value
     * @param text
     */
    public void setText(String text) {
        editText.setText(text);
        CharSequence content = editText.getText();
        if (content instanceof Spannable) {
            Spannable spanText = (Spannable) content;
            Selection.setSelection(spanText, text.length());
        }
        clearButton.setVisibility(View.GONE);
    }

    /**
     * Set OnClickListener, making EditText unfocusable
     * @param listener
     */
    @Override
    public void setOnClickListener(OnClickListener listener) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setOnClickListener(listener);
    }

    /**
     * Set listener to be fired after EditText is cleared
     * @param listener
     */
    public void setOnClearListener(OnClickListener listener) {
        onClickClearListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            if (onClickClearListener != null
                    && clearButton != null) {
                onClickClearListener.onClick(clearButton);
            }
        }
    }
}