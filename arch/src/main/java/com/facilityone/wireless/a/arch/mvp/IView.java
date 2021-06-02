package com.facilityone.wireless.a.arch.mvp;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:View页面需要实现此接口传递Presenter
 * Date: 2018/3/13 下午4:52
 */
public interface IView<P> {

    /**获取Presenter*/
    P getPresenter();

    /**创建Presenter*/
    P createPresenter();

    /**显示隐藏Loading*/
    QMUITipDialog showLoading();
    QMUITipDialog dismissLoading();
}
