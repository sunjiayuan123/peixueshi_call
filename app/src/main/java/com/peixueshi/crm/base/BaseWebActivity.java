package com.peixueshi.crm.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.R;

import butterknife.BindView;

/**
 * 公有的webview
 */
public class BaseWebActivity extends MyBaseArmActivity {

    @BindView(R.id.coupon_webview)
    WebView mWebView;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;


    private String url;
    private String title;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_coupon_web;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        toolbarTitle.setText(title);
        WebSettings ws = mWebView.getSettings();
        ws.setBuiltInZoomControls(false);// 隐藏缩放按钮
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);// 新加
        mWebView.setWebChromeClient(new WebChromeClient());
        //如果不设置WebViewClient，请求会跳转系统浏览器
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        mWebView.loadData(Html.fromHtml(url).toString(), "text/html", "UTF-8");
    }

}
