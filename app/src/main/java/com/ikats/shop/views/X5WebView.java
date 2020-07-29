package com.ikats.shop.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.GeolocationPermissions;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebIconDatabase;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.sdk.WebViewDatabase;

import java.util.HashMap;
import java.util.Map;

//重定义webview 这里继承的是X5WebView
public class X5WebView extends WebView {
    TextView title;
    private Context context;
    private WebViewClient client = new WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);

                Map<String, String> extraHeaders = new HashMap<String, String>();
                extraHeaders.put("Referer", "");//这里可换成自己域名 不写也没事
                view.loadUrl(url, extraHeaders);
                return false;
            } else {
                if (url.startsWith("weixin://wap/pay?")) {//这里拦截了支付 不跳转支付页面直接打开支付功能
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
                return true;
            }
        }
    };

    public X5WebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        this.context = arg0;
//        this.setWebViewClient(client);
        // this.setWebChromeClient(chromeClient);
        // WebStorage webStorage = WebStorage.getInstance();
        initWebViewSettings();
        this.getView().setClickable(true);
    }

    //这是一些设置文件  具体的意思 可以百度 或者翻看我之前的博客 这里设置可以打开图片 视频还有自适应的主要功能
    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    public X5WebView(Context arg0) {
        super(arg0);
        setBackgroundColor(85621);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        postDelayed(() -> {
            if (drawListener != null && getContentHeight() > 0 && getProgress() == 100)
                drawListener.onDrawCallBack(getUrl());
        }, 30);
    }

    public void setDrawListener(OnDrawListener drawListener) {
        this.drawListener = drawListener;
    }

    OnDrawListener drawListener;

    public interface OnDrawListener {
        void onDrawCallBack(String url);
    }

    private void openFileChooseProcess(Activity activity) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        activity.startActivityForResult(Intent.createChooser(i, "test"), 0);
    }

    private void openFileChooseProcess(Fragment fragment) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        fragment.startActivityForResult(Intent.createChooser(i, "test"), 0);
    }

    // 向webview发出信息
    private void enableX5FullscreenFunc() {

        if (getX5WebViewExtension() != null) {
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
        }
    }

    private void disableX5FullscreenFunc() {
        if (getX5WebViewExtension() != null) {
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
        }
    }

    private void enableLiteWndFunc() {
        if (getX5WebViewExtension() != null) {
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
        }
    }

    private void enablePageVideoFunc() {
        if (getX5WebViewExtension() != null) {
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
        }
    }

    //方法一：针对性删除
    public void clearCacheData(Context context) {
        //清除cookie
        CookieManager.getInstance().removeAllCookies(null);
        //清除storage相关缓存
        WebStorage.getInstance().deleteAllData();

        //清除用户密码信息
        WebViewDatabase.getInstance(context).clearUsernamePassword();
        //清除httpauth信息
        WebViewDatabase.getInstance(context).clearHttpAuthUsernamePassword();
        //清除表单数据
        WebViewDatabase.getInstance(context).clearFormData();
        //清除页面icon图标信息
        WebIconDatabase.getInstance().removeAllIcons();
        //删除地理位置授权，也可以删除某个域名的授权（参考接口类）
        GeolocationPermissions.getInstance().clearAll();
    }

    //方法二：一次性删除所有缓存
    public void clearAllData(Context context, boolean isClearCookie) {
        //清除cookie
        QbSdk.clearAllWebViewCache(context, isClearCookie);
    }

}