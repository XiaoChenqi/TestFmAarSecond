package com.facilityone.wireless.a.arch.ec.update;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2019/1/2 2:12 PM
 */
public class AppUpdateFragment extends BaseFragment<AppUpdatePresenter> {
    private WebView mWebView;
    private ProgressBar mProgressBar;

    private static final String LOAD_URL = "load_url";
    private String mUrl;

    @Override
    public AppUpdatePresenter createPresenter() {
        return new AppUpdatePresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_app_update;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initWebView();
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUrl = arguments.getString(LOAD_URL, "");
        }
    }

    private void initView() {
        setTitle(R.string.arch_app_update);
        mWebView = findViewById(R.id.web_base);
        mProgressBar = findViewById(R.id.pb_web_base);
        mProgressBar.setMax(100);//设置加载进度最大值
        setSwipeBackEnable(false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);

        //其他细节操作
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口

        // 设置setWebChromeClient对象
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                if (newProgress >= 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        mWebView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        mWebView.loadUrl(mUrl);

    }

    public static AppUpdateFragment getInstance(String url) {
        AppUpdateFragment instance = new AppUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LOAD_URL, url);
        instance.setArguments(bundle);
        return instance;
    }
}
