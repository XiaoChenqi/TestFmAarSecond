package com.facilityone.wireless.a.arch.ec.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/2 下午2:25
 */
public class ProgressWebViewFragment extends BaseFragment {

    private WebView mWebView;
    private ProgressBar mProgressBar;

    public static final String FILE_NAME = "file_name";
    public static final String FILE_URL = "file_url";

    private String mFileName;
    private String mUrl;

    @Override
    public Object createPresenter() {
        return null;
    }

    @Override
    public Object setLayout() {
        return R.layout.progress_webview;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (initData()) {
            return;
        }
        initView();
        initWebView();
    }

    private boolean initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mFileName = bundle.getString(FILE_NAME, getString(R.string.arch_attachement));
            setTitle(mFileName);
            mUrl = bundle.getString(FILE_URL);
            if (mUrl == null) {
                ToastUtils.showShort(R.string.arch_load_error);
                pop();
                return true;
            }
        }
        return false;
    }

    private void initView() {
        mWebView = findViewById(R.id.web_base);
        mProgressBar = findViewById(R.id.pb_web_base);
        mProgressBar.setMax(100);//设置加载进度最大值
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);

        // 设置setWebChromeClient对象
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });

        if (mFileName.endsWith(".jpg") || mFileName.endsWith(".png") || mFileName.endsWith(".jpeg")) {
            mWebView.loadDataWithBaseURL(null, "<img  style=\"width:100%\"  src=" + mUrl + ">", "text/html", "charset=UTF-8", null);
        } else {
            mWebView.loadUrl(mUrl);
        }

        //设置此方法可在WebView中打开链接，反之用浏览器打开
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtils.d(url);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }
            
        });

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(paramAnonymousString1));
                startActivity(intent);
            }
        });
    }

    public static ProgressWebViewFragment getInstance(String name, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(FILE_NAME, name);
        bundle.putString(FILE_URL, url);
        ProgressWebViewFragment fragment = new ProgressWebViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
