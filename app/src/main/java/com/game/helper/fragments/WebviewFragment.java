package com.game.helper.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.game.helper.model.GeneralizeAccountInfoResultModel;
import com.game.helper.net.DataService;
import com.game.helper.share.UMengShare;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.views.XReloadableStateContorller;
import com.jude.swipbackhelper.SwipeBackHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.Kits;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static android.app.Activity.RESULT_OK;

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
                        if ("分享收益".equals(title) && Kits.Empty.check(shareUrl)) {
                            Flowable<HttpResultModel<GeneralizeAccountInfoResultModel>> fr = DataService.getGeneralizeAccountInfo();
                            RxLoadingUtils.subscribeWithReload(contentLayout, fr, bindToLifecycle(), new Consumer<HttpResultModel<GeneralizeAccountInfoResultModel>>() {
                                @Override
                                public void accept(HttpResultModel<GeneralizeAccountInfoResultModel> generalizeAccountInfoResultModelHttpResultModel) throws Exception {
                                    contentLayout.showContent();
                                    if (generalizeAccountInfoResultModelHttpResultModel.isSucceful()) {
                                        shareUrl = generalizeAccountInfoResultModelHttpResultModel.data.getUrl();
                                    }
                                }
                            }, null, null, true);
                        } else
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
             jsObj.JavaCallBack(4,"分享");
             jsObj.JavaCallBack(5,"返回");
             jsObj.JavaCallBack(6,"会员升级");
             * @param code
             * @param message
             */
            @JavascriptInterface
            public void JavaCallBack(int code, String message) {
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
                                DetailFragmentsActivity.launchForResult(WebviewFragment.this, LoginFragment.newInstance(), 0, null, 0);
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UMengShare share = new UMengShare(getActivity());
                                CommonShareResults shareResults = new CommonShareResults(shareUrl, "推广收益", "", SharedPreUtil.getLoginUserInfo().icon);
                                share.shareLinkWithBoard(shareResults, null);
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
                }
            }
        };
        return insertObj;
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
            case 0:
                if (resultCode == RESULT_OK) {
                    getActivity().onBackPressed();
                    Bundle bundle = new Bundle();
                    bundle.putString(PARAM_URL, WebviewFragment.PARAM_VIP);
                    DetailFragmentsActivity.launch(getContext(), bundle, WebviewFragment.newInstance());
                }
                break;
        }
    }
}
