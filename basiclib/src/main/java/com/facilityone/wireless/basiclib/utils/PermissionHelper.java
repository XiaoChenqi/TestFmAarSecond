package com.facilityone.wireless.basiclib.utils;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:android 6.0 权限申请
 * Date: 2018/6/27 上午11:55
 */
public class PermissionHelper {

    public static void requestAll(final OnPermissionGrantedListener listener, OnPermissionDeniedListener deniedListener) {
        request(listener, deniedListener,
                PermissionConstants.CAMERA,
                PermissionConstants.STORAGE,
                PermissionConstants.PHONE,
                PermissionConstants.SMS,
                PermissionConstants.MICROPHONE,
                PermissionConstants.LOCATION);
    }

    public static void requestCamera(final OnPermissionGrantedListener listener) {
        request(listener, PermissionConstants.CAMERA);
    }

    public static void requestStorage(final OnPermissionGrantedListener listener) {
        request(listener, PermissionConstants.STORAGE);
    }

    public static void requestPhone(final OnPermissionGrantedListener listener) {
        request(listener, PermissionConstants.PHONE);
    }

    public static void requestPhone(final OnPermissionGrantedListener grantedListener,
                                    final OnPermissionDeniedListener deniedListener) {
        request(grantedListener, deniedListener, PermissionConstants.PHONE);
    }

    public static void requestSms(final OnPermissionGrantedListener listener) {
        request(listener, PermissionConstants.SMS);
    }

    public static void requestMicrophone(final OnPermissionGrantedListener listener) {
        request(listener, PermissionConstants.MICROPHONE);
    }

    public static void requestMicrophone(final OnPermissionGrantedListener listener,
                                         final OnPermissionDeniedListener deniedListener) {
        request(listener, deniedListener, PermissionConstants.MICROPHONE, PermissionConstants.STORAGE);
    }

    public static void requestLocation(final OnPermissionGrantedListener listener) {
        request(listener, PermissionConstants.LOCATION);
    }

    public static void requestLocation(final OnPermissionDeniedListener listener) {
        request(null, listener, PermissionConstants.LOCATION, PermissionConstants.PHONE, PermissionConstants.MICROPHONE, PermissionConstants.STORAGE);
    }

    private static void request(final OnPermissionGrantedListener grantedListener,
                                final @PermissionConstants.Permission String... permissions) {
        request(grantedListener, null, permissions);
    }

    private static void request(final OnPermissionGrantedListener grantedListener,
                                final OnPermissionDeniedListener deniedListener,
                                final @PermissionConstants.Permission String... permissions) {
        PermissionUtils.permission(permissions)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(ShouldRequest shouldRequest) {
//                        DialogHelper.showRationaleDialog(shouldRequest);
                        shouldRequest.again(true);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        if (grantedListener != null) {
                            grantedListener.onPermissionGranted();
                        }
                        LogUtils.d(permissionsGranted);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            DialogHelper.showOpenAppSettingDialog();
                        }
                        if (deniedListener != null) {
                            deniedListener.onPermissionDenied();
                        }
                        LogUtils.d(permissionsDeniedForever, permissionsDenied);
                    }
                })
                .request();
    }

    public interface OnPermissionGrantedListener {
        void onPermissionGranted();
    }

    public interface OnPermissionDeniedListener {
        void onPermissionDenied();
    }
}
