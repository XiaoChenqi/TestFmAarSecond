package com.facilityone.wireless.a.arch.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SPUtils;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.ec.logon.LogonManager;
import com.facilityone.wireless.a.arch.ec.module.LogonResponse;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.fm.tool.network.callback.JsonCallback;
import com.fm.tool.network.model.BaseResponse;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.WebChromeClient;
import com.lzy.okgo.model.Response;

import java.util.Map;

/**
 * Author：gary
 * <p/>
 * Email: xuhaozv@163.com
 * <p/>
 * description:
 * <p/>
 * Date: 2020-07-15 12:29
 */
public class AgentWebFragment extends BaseFragment<BasePresenter> {

    protected AgentWeb mAgentWeb;

    private static final String GO_TO_URL = "go_to_url";
    private String mUrl;
    private LinearLayout mUrlContent;
    private Map<String, String> mHeaders;

    @Override
    public Object setLayout() {
        return R.layout.agent_web_fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();

        mUrlContent = (LinearLayout) view.findViewById(R.id.ll_my_content);

        //添加请求头部
        //需要登录
        String username = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.USERNAME);
        String password = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.PASSWORD);
        LogonManager.getInstance().logon(username, password, new JsonCallback<BaseResponse<LogonResponse>>() {
            @Override
            public void onSuccess(Response<BaseResponse<LogonResponse>> response) {
                LogonResponse data = response.body().data;
                LogonManager.getInstance().saveToken(data);
                if (data != null) {
                    initWeb();
                } else {
                    pop();
                }
            }

            @Override
            public void onError(Response<BaseResponse<LogonResponse>> response) {
                super.onError(response);
                pop();
            }
        });

    }

    private void initWeb() {
        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(mUrlContent, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                .setWebChromeClient(mWebChromeClient) //WebChromeClient
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                .additionalHttpHeader(getUrl(), mHeaders)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(getUrl()); //WebView载入该url地址的页面并显示。
    }

    private void initData() {
        mUrl = this.getArguments().getString(GO_TO_URL, "");
    }

    /**
     * 注意，重写WebViewClient的方法,super.xxx()请务必正确调用， 如果没有调用super.xxx(),则无法执行DefaultWebClient的方法
     * 可能会影响到AgentWeb自带提供的功能,尽可能调用super.xxx()来完成洋葱模型
     */
    protected com.just.agentweb.WebViewClient mWebViewClient = new com.just.agentweb.WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }
        };
    }

    protected com.just.agentweb.WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title)) {
                if (title.length() > 10) {
                    title = title.substring(0, 10).concat("...");
                }
            }
            setTitle(title);
        }
    };

    @Override
    public boolean onBackPressedSupport() {
        if (mAgentWeb != null && mAgentWeb.back()) {
            return true;
        }

        return super.onBackPressedSupport();
    }

    @Override
    public void onResume() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onResume();//恢复
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onPause(); //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mAgentWeb.getWebLifeCycle().onResume(); 恢复。
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroyView();
    }

    public static AgentWebFragment getInstance(String url) {
        AgentWebFragment fragment = new AgentWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GO_TO_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

}
