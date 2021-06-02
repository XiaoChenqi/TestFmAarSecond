package com.facilityone.wireless.a.arch.utils;

import android.app.Activity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewUtil {
    public static void showTextNormal(TextView tv, String text) {
        if ((null == tv) || (null == text)) {
            return;
        }

        tv.setText(text);
    }

    /**
     * @param tv
     * @param baseText
     * @param highlightText if the string of highlightText is a subset of the string of baseText,highlight the string of highlightText.
     */
    public static void showTextHighlight(TextView tv, String baseText, String highlightText) {
        if ((null == tv) || (null == baseText) || (null == highlightText)) {
            return;
        }

        int index = baseText.indexOf(highlightText);
        if (index < 0) {
            tv.setText(baseText);
            return;
        }

        int len = highlightText.length();
        /**
         *  "<u><font color=#FF8C00 >"+str+"</font></u>"; 	//with underline
         *  "<font color=#FF8C00 >"+str+"</font>";			//without underline
         *
         *  <color name="dark_orange">#FF8C00</color>
         */
        Spanned spanned = Html.fromHtml(baseText.substring(0, index) + "<font color=#FF8C00 >"
                + baseText.substring(index, index + len) + "</font>"
                + baseText.substring(index + len, baseText.length()));

        tv.setText(spanned);
    }

    public static int getViewVisibility(View view) {
        if (null == view) {
            return View.GONE;
        }

        return view.getVisibility();
    }

    public static void showView(View view) {
        if (null == view) {
            return;
        }

        if (View.VISIBLE != view.getVisibility()) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void invisibleView(View view) {
        if (null == view) {
            return;
        }
        if (View.INVISIBLE != view.getVisibility()) {
            view.setVisibility(View.INVISIBLE);
        }

        return;
    }

    public static void hideView(View view) {
        if (null == view) {
            return;
        }
        if (View.GONE != view.getVisibility()) {
            view.setVisibility(View.GONE);
        }

        return;
    }

    /**
     * hide soft keyboard on android after clicking outside EditText
     *
     * @param view
     */
    public static void setHideIme(final Activity activity, View view) {
        if (null == activity || null == view) {
            return;
        }

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ViewUtil.hideSoftKeyboard(activity);
                    return false;
                }

            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setHideIme(activity, innerView);
            }
        }
    }

    /**
     * hide soft keyboard
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (null == activity) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (null != inputMethodManager) {
            View view = activity.getCurrentFocus();
            if (null != view) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }
    }

    /**
     * 控制输入框输入的小数点位数
     *
     * @param editText 需要控制小数位数的输入框
     * @param decimal  允许输入的小数点位数
     */
    public static void setNumberPoint(final EditText editText, final int decimal) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > decimal) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + decimal + 1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }
    public static void setInputruler(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
           String beforeText ="";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String afterText = s.toString();
                if (!TextUtils.isEmpty(afterText)) {
                    if (!notChinese(afterText)) {
                        // 用户现在输入的字符数减去之前输入的字符数，等于新增的字符数
                        int differ = afterText.length() - beforeText.length();
                        // 如果用户的输入不符合规范，则显示之前输入的文本
                        editText.setText(beforeText);
                        // 光标移动到文本末尾
                        editText.setSelection(afterText.length() - differ);
                    }else {
                        editText.setSelection(afterText.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    public static boolean isMatches(String text, String format){
        Pattern p = Pattern.compile( format);
        Matcher m = p.matcher(text);
        return  m.matches();
    }

    public static boolean notChinese(String str){
        String regex ="[0-9A-Za-z_\\-]";
        return isMatches(str,regex);
    }
}
