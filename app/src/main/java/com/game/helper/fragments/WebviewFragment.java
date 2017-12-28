package com.game.helper.fragments;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.jude.swipbackhelper.SwipeBackHelper;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xstatecontroller.XStateController;

public class WebviewFragment extends XBaseFragment {
    public static final String TAG = WebviewFragment.class.getSimpleName();

    @BindView(R.id.generalize_root_layout)
    XStateController contentLayout;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.webview)
    WebView webView;
    String url;
    String title;

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
        }
        initContentLayout();
        initRefreshLayout();
        initWebView();
        SwipeBackHelper.getCurrentPage(getActivity()).setDisallowInterceptTouchEvent(true);
    }

    private void initContentLayout() {
        contentLayout.loadingView(View.inflate(context, R.layout.view_loading, null));
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
                webView.loadUrl(url);
            }
        });

    }

    private void initWebView() {
        webView.loadDataWithBaseURL("", "", "text/html", "utf-8", "");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (contentLayout != null)
                        contentLayout.showContent();
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

        webView.loadUrl(url);
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
}
