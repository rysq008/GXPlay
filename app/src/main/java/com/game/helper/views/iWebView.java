package com.game.helper.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
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

    @SuppressLint("JavascriptInterface")
    public void init(final Context context) {
        //        synCookies(context, getUrl());
        try {
            getSettings().setJavaScriptEnabled(true);
            getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
            // 开启 DOM storage API 功能
            getSettings().setDomStorageEnabled(true);
            //开启 database storage API 功能
            getSettings().setDatabaseEnabled(true);
            //开启 Application Caches 功能
            getSettings().setAppCacheEnabled(true);

            // 设置可以支持缩放
            getSettings().setSupportZoom(false);
            // 扩大比例的缩放
            getSettings().setUseWideViewPort(false);
            WebSettings webSettings = getSettings();
            webSettings.setBuiltInZoomControls(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setSaveFormData(true);
            webSettings.setGeolocationEnabled(true);
            webSettings.setTextZoom(100);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDisplayZoomControls(false);
            requestFocus();
            setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
            webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDefaultTextEncodingName("utf-8");
            addJavascriptInterface(getHtmlObject(), "jsObj");

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

                public void onReceivedSslError(WebView view, android.webkit.SslErrorHandler handler, android.net.http.SslError error) {
                    /***
                     * 前两个是代表日期问题，第三个是webview的bug，最后一个是证书丢失，如果有这些问题，将忽略它，而不是什么情况都跳过SSL这一层，这样真的很不友好。所以 问题也就解决了。
                     */
                    if (error.getPrimaryError() == SslError.SSL_DATE_INVALID
                            || error.getPrimaryError() == SslError.SSL_EXPIRED
                            || error.getPrimaryError() == SslError.SSL_INVALID
                            || error.getPrimaryError() == SslError.SSL_UNTRUSTED) {
                        handler.proceed();
                    } else {
                        handler.cancel();
                    }
                    super.onReceivedSslError(view, handler, error);
                }

                ;
            });

            setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int progress) {
//                Activity.this.setTitle("Loading...");
//                Activity.this.setProgress(progress);
//                if (progress >= 80) {
//                    // Activity.this.setTitle("JsAndroid");
//                }
                }

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
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public void cleanCache(Context context) {
//        setCacheMode(WebSettings.LOAD_NO_CACHE);
        clearCache(true);
        context.deleteDatabase("WebView.db");
        context.deleteDatabase("WebViewCache.db");
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

    private Object getHtmlObject() {
        Object insertObj = new Object() {
            @JavascriptInterface
            public void HtmlcallJavaFinishJava() {
            }

            /**
             * 跳转登录
             * */
            @JavascriptInterface
            public String HtmlcallJava2() {
                return "0";
            }

            @JavascriptInterface
            public String HtmlcallJava3() {
                return "0";
            }

            /**
             * 跳转分享
             * */
            @JavascriptInterface
            public String HtmlcallJava4() {
                return "0";
            }

            @JavascriptInterface
            public void JavacallHtml() {
            }
        };
        return insertObj;
    }
}
