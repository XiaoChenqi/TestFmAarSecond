package com.facilityone.wireless.a.arch.utils;

import android.view.View;

/**
 * Author：xhh
 * <p/>
 * Email: xuhaozv@163.com
 * <p/>
 * description:
 * <p/>
 * Date: 2019/7/23 17:49
 */
public class DotPasswordTransformationMethod extends android.text.method.PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;
        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }
        public char charAt(int index) {
            return '•'; // This is the important part
        }
        public int length() {
            return mSource.length(); // Return default
        }
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}
