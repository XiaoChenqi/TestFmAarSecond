package com.facilityone.wireless.a.arch.ec.module;

import android.os.Bundle;

import com.facilityone.wireless.a.arch.mvp.BaseFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:基础跳转
 * Date: 2018/6/21 上午11:03
 */
public interface IService {
    String FRAGMENT_CHILD_KEY = "fragment_child_key";
    String COMPONENT_RUNALONE = "component_runalone";
    BaseFragment getFragment(Bundle bundle);
}
