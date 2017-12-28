package com.game.helper.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.game.helper.utils.SharedPreUtil;

/**
 * Created by zr on 2017-10-13.
 */

public class iWebView extends WebView {
    public iWebView(Context context) {
        super(context);
    }

    public iWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public iWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(final Context context) {
        // 自适应屏幕
        getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        getSettings().setLoadWithOverviewMode(true);
        // 支持javascript
        getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        getSettings().setSupportZoom(false);
        // 设置出现缩放工具
        getSettings().setBuiltInZoomControls(false);
        // 扩大比例的缩放
        getSettings().setUseWideViewPort(false);
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView view, String url, final String message, final JsResult result) {
                new AlertDialog.Builder(context)
//                    .setTitle("App Titler")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (message.equals("确定要退出吗?")) {
                                            result.confirm();
                                        } else {
                                            result.confirm();
                                        }
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.cancel();
                                    }
                                })
                        .create()
                        .show();
                return true;
            }
        });
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); // 在当前的webview中跳转到新的url
                //获取cookies
                CookieManager cm = CookieManager.getInstance();
                String cookies = cm.getCookie(url);
                SharedPreUtil.saveCookies(cookies);
                return true;
            }
        });
//        synCookies(context, getUrl());
    }


    /**
     * 同步一下cookie
     */
    public void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, SharedPreUtil.getCookies());
        CookieSyncManager.getInstance().sync();
    }
}
