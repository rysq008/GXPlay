package com.zhny.zhny_app.activitys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zhny.zhny_app.R;
import com.zhny.zhny_app.activitys.BaseActivity.XBaseActivity;
import com.zhny.zhny_app.model.LoginBean;
import com.zhny.zhny_app.utils.ShareUtils;
import com.zhny.zhny_app.views.XReloadableStateContorller;

import butterknife.BindView;

public class WebActivity extends XBaseActivity {

    @BindView(R.id.wv_content)
    WebView wvContent;
    @BindView(R.id.swipeRefreshLayout_wv)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tip_info)
    XReloadableStateContorller tip_info;
    String url = "";
    LoginBean bean = ShareUtils.getLoginInfo();

    protected static final String APP_CACAHE_DIRNAME = "/webcache";

    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(webView.getContext());

            Dialog d = dialog.create();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };

    @Override
    public void initData(Bundle savedInstanceState) {
        url = "http://www.baidu.com";
        Log.e("url--", url);
        initWebview(url);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                wvContent.reload();
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public Object newP() {
        return null;
    }


    private void initWebview(String url) {
        if (Build.VERSION.SDK_INT >= 19) {
            wvContent.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            wvContent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        WebSettings webSetting = wvContent.getSettings();
        if (webSetting == null) {
            return;
        }
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //开启js功能
        webSetting.setJavaScriptEnabled(true);
        //允许js弹出窗
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        // 添加android->H5通讯接口
//        webview.addJavascriptInterface(new CallJavaInterface(mActivity, basicview_swiperefresh, mHandler), "android");
        wvContent.setWebChromeClient(webChromeClient);
        wvContent.setWebViewClient(webViewClient);
        wvContent.setDownloadListener(new MyWebViewDownLoadListener());
        wvContent.addJavascriptInterface(new Android2Js(), "android");

        webSetting.setAllowFileAccess(true);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webSetting.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSetting.setDatabaseEnabled(true);
        String cacheDirPath = this.getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME + url;
        // 设置数据库缓存路径
        webSetting.setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        webSetting.setAppCachePath(cacheDirPath);
        // 开启 Applicat ion Caches 功能
        webSetting.setAppCacheEnabled(true);
        webSetting.setAppCacheMaxSize(1024 * 1024 * 100);
        // 设置图片加载模式
        webSetting.setBlockNetworkImage(false);
        webSetting.setUseWideViewPort(false);
        // 设置监听
        webSetting.setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(webSetting.getMixedContentMode());
        }
        //设置webview内容不随系统字体大小改变
        webSetting.setTextZoom(100);
        wvContent.loadUrl(url);
    }


    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            tip_info.showLoading();
        }

        /**
         * 断网状态下回调，回调2次，onPageFinished前后
         * @param view
         * @param errorCode
         * @param description
         * @param failingUrl
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            tip_info.showError();
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
            sslErrorHandler.proceed();// 接受所有网站的证书
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
            tip_info.showContent();
            swipeRefreshLayout.setRefreshing(false);
        }
    };


    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override

        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (wvContent.canGoBack()) {
            if (wvContent.getUrl().equals(url)) {
            } else {
                wvContent.post(() -> {
                    wvContent.goBack();
                });
            }
        } else {
        }
    }

    public class Android2Js extends Object {
        @JavascriptInterface
        public void requestLocation(String hand_Result) {
        }

        @JavascriptInterface
        public void requestRefresh(boolean hand_Result) {
            swipeRefreshLayout.setEnabled(hand_Result);
        }

        @JavascriptInterface
        public void requestPhotoCapture(String hand_Result) {
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {

        }
    }
}
