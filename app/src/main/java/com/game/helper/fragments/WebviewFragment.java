package com.game.helper.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.login.LoginFragment;
import com.game.helper.fragments.login.RegistFragment;
import com.game.helper.fragments.recharge.RechargeFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.CommonShareResults;
import com.game.helper.model.ShareInfoResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.ShareInfoRequestBody;
import com.game.helper.share.UMengShare;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.views.XReloadableStateContorller;
import com.jude.swipbackhelper.SwipeBackHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.LoadCallback;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.NetError;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.DIRECTORY_DCIM;

public class WebviewFragment extends XBaseFragment {
    public static final String TAG = WebviewFragment.class.getSimpleName();

    @BindView(R.id.generalize_root_layout)
    XReloadableStateContorller contentLayout;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.webview)
    WebView webView;
    String url;
    String title;
    String shareUrl;

    public static String PARAM_VIP = "";
    public static final String PARAM_URL = "url";
    public static final String PARAM_TITLE = "title";
    public static int requestCode = -1;

    public static WebviewFragment newInstance() {
        return new WebviewFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (!Kits.Empty.check(getArguments())) {
            url = (String) getArguments().getString(PARAM_URL, "");
            title = getArguments().getString(PARAM_TITLE, "");
            Log.d(TAG, "h5 : url = " + url + " / tittle = " + title);
        }
        initContentLayout();
        initRefreshLayout();
        initWebView();
        SwipeBackHelper.getCurrentPage(getActivity()).setDisallowInterceptTouchEvent(true);
    }

    private void initContentLayout() {
//        contentLayout.loadingView(View.inflate(context, R.layout.view_loading, null));
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                synCookieToWebView(url, SharedPreUtil.getSessionId());
                webView.loadUrl(url);
            }
        });

    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
//        webView.loadDataWithBaseURL("", "", "text/html", "utf-8", "");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (contentLayout != null) {
//                        if ("分享收益".equals(title) && Kits.Empty.check(shareUrl)) {
//                            Flowable<HttpResultModel<GeneralizeAccountInfoResultModel>> fr = DataService.getGeneralizeAccountInfo();
//                            RxLoadingUtils.subscribeWithReload(contentLayout, fr, bindToLifecycle(), new Consumer<HttpResultModel<GeneralizeAccountInfoResultModel>>() {
//                                @Override
//                                public void accept(HttpResultModel<GeneralizeAccountInfoResultModel> generalizeAccountInfoResultModelHttpResultModel) throws Exception {
//                                    contentLayout.showContent();
//                                    if (generalizeAccountInfoResultModelHttpResultModel.isSucceful()) {
//                                        shareUrl = generalizeAccountInfoResultModelHttpResultModel.data.getUrl();
//                                    }
//                                }
//                            }, null, null, true);
//                        } else
                        contentLayout.showContent();
                    }
                    if (webView != null)
                        url = webView.getUrl();
                } else {
                    if (contentLayout != null)
                        contentLayout.showLoading();
                }
            }
        });
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);
        webView.addJavascriptInterface(getHtmlObject(), "jsObj");

//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url); // 在当前的webview中跳转到新的url
//                //获取cookies
//                CookieManager cm = CookieManager.getInstance();
//                String cookies = cm.getCookie(url);
//                sp.edit().putString("cook", cookies).apply();
//                return true;
//            }
//        });
        Map extraHeaders = new HashMap<String, String>();
        extraHeaders.put("control-cache", "no-cache,private");
        extraHeaders.put("pragma", "no-cache,no-store");
        extraHeaders.put("expires", "0");
        synCookies(url, SharedPreUtil.getSessionId());
//        synCookieToWebView(url, SharedPreUtil.getSessionId());
        webView.loadUrl(url.concat("?" + SharedPreUtil.getSessionId()), extraHeaders);
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (webView.canGoBack())
                    webView.goBack();
                else
                    getActivity().finish();
                return false;
            }
        });
    }

    private Object getHtmlObject() {
        Object insertObj = new Object() {

            /***
             *  jsObj.JavaCallBack(1,"注册");
             jsObj.JavaCallBack(2,"登录");
             jsObj.JavaCallBack(3,"充值");
             jsObj.JavaCallBack(4X,"分享");X表示类型
             jsObj.JavaCallBack(5,"返回");
             jsObj.JavaCallBack(6,"会员升级");
             jsObj.JavaCallBack(7,"保存图片");
             * @param code
             * @param message
             */
            @JavascriptInterface
            public void JavaCallBack(final int code, final String message) {
                switch (code) {
                    case 1:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DetailFragmentsActivity.launch(context, null, RegistFragment.newInstance());
                            }
                        });
                        break;
                    case 2:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DetailFragmentsActivity.launchForResult(WebviewFragment.this, LoginFragment.newInstance(), 0, null, requestCode);
                            }
                        });
                        break;
                    case 3:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DetailFragmentsActivity.launch(context, null, RechargeFragment.newInstance());
                            }
                        });
                        break;
                    case 4:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (requestCode == 1) {
                                    shareUrl = message;
                                }
                                Flowable<HttpResultModel<ShareInfoResults>> flowable = DataService.getApiShareInfoData(new ShareInfoRequestBody(code / 10 > 0 ? code % 10 : 1));
                                RxLoadingUtils.subscribeWithDialog(context, flowable, WebviewFragment.this.bindToLifecycle(), new Consumer<HttpResultModel<ShareInfoResults>>() {
                                    @Override
                                    public void accept(HttpResultModel<ShareInfoResults> shareInfoResultsHttpResultModel) throws Exception {
                                        if (!shareInfoResultsHttpResultModel.isSucceful()) {
                                            Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        UMengShare share = new UMengShare(getActivity());
                                        if (!Kits.Empty.check(shareInfoResultsHttpResultModel.data)) {
                                            CommonShareResults shareResults = new CommonShareResults(shareUrl, shareInfoResultsHttpResultModel.data.title, shareInfoResultsHttpResultModel.data.content, shareInfoResultsHttpResultModel.data.logo);
                                            share.shareLinkWithBoard(shareResults, null);
                                        }
                                    }
                                }, new Consumer<NetError>() {
                                    @Override
                                    public void accept(NetError netError) throws Exception {
                                        Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        break;
                    case 5:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (webView.canGoBack()) {
                                    webView.goBack();
                                } else {
                                    getActivity().finish();
                                }
                            }
                        });
                        break;
                    case 6:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bundle bundle = new Bundle();
                                bundle.putInt(RechargeFragment.VIP, 3);
                                DetailFragmentsActivity.launch(context, bundle, RechargeFragment.newInstance());
//                                webView.reload();
                            }
                        });
                        break;
                    case 7:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                saveBitmap(message);
                            }
                        });
                        break;
                }
            }
        };
        return insertObj;
    }

    private void saveBitmap(final String qr_url) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getRxPermissions().request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ILFactory.getLoader().loadNet(getContext(), qr_url, null, new LoadCallback() {
                                @Override
                                public void onLoadReady(Bitmap bitmap) {
                                    File imageSavePath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM);
                                    FileOutputStream outputStream = null;
                                    File imgFile = new File(imageSavePath, "G9_QR_CODE.jpg");
                                    try {
                                        outputStream = new FileOutputStream(imgFile);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                                        getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imgFile)));
                                        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, "save fail ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(context, "请打开权限!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 同步一下cookie
     */
    public void synCookies(Context context, String cookies) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, cookies);
        CookieSyncManager.getInstance().sync();
    }

    public boolean synCookies(String url, String cookie) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookie);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        String newCookie = cookieManager.getCookie(url);
        return TextUtils.isEmpty(newCookie) ? false : true;
    }

    private void synCookieToWebView(String url, String cookies) {
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        if (cookies != null) {
//            for (String cookie : cookies) {
            cm.setCookie(url, cookies);//注意端口号和域名，这种方式可以同步所有cookie，包括sessionid
//            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) webView.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
        getFocus();
    }

    //主界面获取焦点
    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // 监听到返回按钮点击事件
                    if (webView.canGoBack())
                        webView.goBack();
                    else
                        getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.clearCache(true);
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_webview_layout;
    }

    @Override
    public Object newP() {
        return null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0://from vip 从主页模块跳转过来的
                if (resultCode == RESULT_OK) {
                    getActivity().onBackPressed();
                    Bundle bundle = new Bundle();
                    bundle.putString(PARAM_URL, WebviewFragment.PARAM_VIP);
                    DetailFragmentsActivity.launch(getContext(), bundle, WebviewFragment.newInstance());
                }
                break;
            case 1://from generalize 从社区模块跳转过来的
                if (resultCode == RESULT_OK) {
                    getActivity().onBackPressed();
                    Bundle bundle = new Bundle();
                    bundle.putString(PARAM_URL, SharedPreUtil.getH5url().market_url.concat("?" + SharedPreUtil.getSessionId()));
                    bundle.putString(WebviewFragment.PARAM_TITLE, "分享收益");
                    DetailFragmentsActivity.launch(getContext(), bundle, WebviewFragment.newInstance());
                }
                break;
        }
    }
}
